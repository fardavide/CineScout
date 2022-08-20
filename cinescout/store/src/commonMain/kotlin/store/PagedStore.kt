package store

import arrow.core.Either
import arrow.core.continuations.either
import arrow.core.left
import arrow.core.right
import cinescout.error.DataError
import cinescout.error.NetworkError
import com.soywiz.klock.DateTime
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combineTransform
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.transform
import store.builder.toPagedData

/**
 * Creates a flow that combines Local data and Remote data, when remote data is paged
 * @see PagedStore
 *
 * First emit Local data, only if available, then emits Local data updated from Remote or Remote errors that wrap
 *  a Local data
 *
 * @param key and unique identifier for the data.
 * @param refresh see [Refresh]
 * @param initialPage initial bookmark to start fetching from
 * @param createNextPage lambda that creates the next bookmark to fetch from
 * @param fetch lambda that returns Remote data
 * @param read lambda that returns a Flow of Local data
 * @param write lambda that saves Remote data to Local
 */
@BuilderInference
inline fun <T : Any, reified P : Paging.Page, KeyId : Any> StoreOwner.PagedStore(
    key: StoreKey<T, KeyId>,
    refresh: Refresh = Refresh.Once,
    initialPage: P = Initial(),
    crossinline createNextPage: (lastData: PagedData<T, P>, currentPage: P) -> P =
        { lastData, currentPage -> lastData.paging.withPage(currentPage.page + 1) as P },
    crossinline fetch: suspend (page: P) -> Either<NetworkError, PagedData.Remote<T, P>>,
    noinline read: () -> Flow<List<T>>,
    noinline write: suspend (List<T>) -> Unit
): PagedStore<T, Paging> {
    var currentPage = initialPage
    val loadMoreTrigger = MutableStateFlow(initialPage)
    val onLoadMore = { loadMoreTrigger.value = currentPage }
    var shouldLoadAll = false

    val flow = buildPagedStoreFlow(
        fetch = { fetch(currentPage).tap { data -> currentPage = createNextPage(data, currentPage) } },
        findFetchData = { paging -> getFetchData(key.paged(paging).value()) },
        insertFetchData = { paging, data -> saveFetchData(key.paged(paging).value(), data) },
        initialPage = initialPage,
        read = read,
        refresh = refresh,
        write = write,
        loadMoreTrigger = loadMoreTrigger
    ).onEach { either ->
        either.tap { data ->
            if (shouldLoadAll && data.isLastPage().not()) {
                onLoadMore()
            }
        }
    }
    return PagedStoreImpl(
        flow = flow,
        onLoadMore = onLoadMore,
        onLoadAll = {
            shouldLoadAll = true
            onLoadMore()
        }
    )
}

@BuilderInference
fun <T, B, PI : Paging.Page, PO : Paging> PagedStore(
    initialBookmark: B,
    createNextBookmark: (lastData: PagedData<T, PI>, currentBookmark: B) -> B,
    fetch: suspend (bookmark: B) -> Either<NetworkError, PagedData.Remote<T, PI>>,
    read: () -> Flow<Either<DataError.Local, List<T>>>,
    write: suspend (List<T>) -> Unit
): PagedStore<T, PO> {
    var bookmark = initialBookmark
    val loadMoreTrigger = MutableStateFlow(initialBookmark)
    val onLoadMore = { loadMoreTrigger.value = bookmark }
    var shouldLoadAll = false

    val flow = buildPagedStoreFlow<T, B, PI, PO>(
        fetch = { fetch(bookmark).tap { data -> bookmark = createNextBookmark(data, bookmark) } },
        read = read,
        write = write,
        loadMoreTrigger = loadMoreTrigger
    ).onEach { either ->
        either.tap { data ->
            if (shouldLoadAll && data.isLastPage().not()) {
                onLoadMore()
            }
        }
    }
    return PagedStoreImpl(
        flow = flow,
        onLoadMore = onLoadMore,
        onLoadAll = {
            shouldLoadAll = true
            onLoadMore()
        }
    )
}

interface PagedStore<T, P : Paging> : Store<PagedData<T, P>> {

    fun filterIntermediatePages(): Flow<Either<DataError, PagedData<T, P>>> =
        loadAll()
            .filter { either ->
                either.fold(
                    ifLeft = { false },
                    ifRight = { pagedData -> pagedData.isLastPage() }
                )
            }

    suspend fun getAll(): Either<DataError, List<T>>

    fun loadAll(): PagedStore<T, P>

    fun loadMore(): PagedStore<T, P>
}

@PublishedApi
internal class PagedStoreImpl<T, P : Paging>(
    internal val flow: Flow<Either<DataError, PagedData<T, P>>>,
    internal val onLoadMore: () -> Unit,
    internal val onLoadAll: () -> Unit
) : PagedStore<T, P>, Flow<Either<DataError, PagedData<T, P>>> by flow {

    override suspend fun getAll(): Either<DataError, List<T>> =
        loadAll().filterIntermediatePages().first().map { it.data }

    override fun loadAll(): PagedStore<T, P> {
        onLoadAll()
        return this
    }

    override fun loadMore(): PagedStore<T, P> {
        onLoadMore()
        return this
    }
}

private fun <T, B, PI : Paging.Page, PO : Paging> buildPagedStoreFlow(
    fetch: suspend (bookmark: B) -> Either<NetworkError, PagedData.Remote<T, PI>>,
    read: () -> Flow<Either<DataError.Local, List<T>>>,
    write: suspend (List<T>) -> Unit,
    loadMoreTrigger: Flow<B>
): Flow<Either<DataError.Remote, PagedData<T, PO>>> =
    combineTransform(
        loadMoreTrigger.transform<B, ConsumableData<PagedData.Remote<T, PI>>?> { bookmark ->
            val remoteDataEither = fetch(bookmark)
            remoteDataEither.tap { remoteData ->
                write(remoteData.data)
            }
            emit(ConsumableData.of(remoteDataEither))
        }.onStart { emit(null) },
        read().map { either -> either.map { list -> list.toPagedData() } }
    ) { consumableRemoteData, localEither ->

        val remoteEither = consumableRemoteData?.consume()
        if (remoteEither != null) {
            val result = either {
                val remoteData = remoteEither
                    .mapLeft(DataError::Remote)
                    .bind()

                localEither.fold(
                    ifLeft = {
                        if (remoteData.isFirstPage()) remoteData
                        else throw AssertionError("Remote data is not first page, but there is no cached data")
                    },
                    ifRight = { localData -> remoteData.copy(data = localData.data) }
                )
            }
            emit(result as Either<DataError.Remote, PagedData<T, PO>>)
        } else {
            localEither.tap { local -> emit(local.right() as Either<DataError.Remote, PagedData<T, PO>>) }
        }
    }

@PublishedApi
@Suppress("LongParameterList")
internal fun <T, P : Paging.Page> buildPagedStoreFlow(
    fetch: suspend (paging: P) -> Either<NetworkError, PagedData.Remote<T, P>>,
    findFetchData: suspend (paging: P) -> FetchData?,
    initialPage: P,
    insertFetchData: suspend (paging: P, FetchData) -> Unit,
    loadMoreTrigger: Flow<P>,
    read: () -> Flow<List<T>>,
    refresh: Refresh,
    write: suspend (List<T>) -> Unit
): Flow<Either<DataError.Remote, PagedData<T, Paging>>> {

    fun readWithFetchData(): Flow<Either<DataError.Local.NoCache, PagedData.Local<T>>> = read()
        .map { data ->
            when (findFetchData(initialPage)) {
                null -> DataError.Local.NoCache.left()
                else -> data.toPagedData().right()
            }
        }

    suspend fun writeWithFetchData(pagedData: PagedData.Remote<T, P>) {
        write.invoke(pagedData.data)
        val fetchData = FetchData(dateTime = DateTime.now())
        insertFetchData(pagedData.paging, fetchData)
    }

    suspend fun FlowCollector<ConsumableData<PagedData.Remote<T, P>>?>.handleFetch(paging: P) {
        val remoteDataEither = fetch(paging)
        remoteDataEither.tap { remoteData ->
            writeWithFetchData(remoteData)
        }
        emit(ConsumableData.of(remoteDataEither))
    }

    fun remoteFlow(paging: P): Flow<ConsumableData<PagedData.Remote<T, P>>?> = when (refresh) {
        Refresh.IfNeeded -> flow {
            if (findFetchData(paging) == null) {
                handleFetch(paging)
            }
        }
        is Refresh.IfExpired -> flow {
            val fetchTimeMs = findFetchData(paging)?.dateTime?.unixMillisLong ?: 0
            val expirationTimeMs = DateTime.now().unixMillisLong - refresh.validity.inWholeSeconds
            val isDataExpired = fetchTimeMs < expirationTimeMs
            if (isDataExpired) {
                handleFetch(paging)
            }
        }
        Refresh.Never -> emptyFlow()
        Refresh.Once, is Refresh.WithInterval -> flow {
            handleFetch(paging)
        }
    }

    return combineTransform(
        loadMoreTrigger.flatMapLatest { paging -> remoteFlow(paging) }
            .onStart { emit(null) },
        readWithFetchData()
    ) { consumableRemoteData, localEither ->

        val remoteEither = consumableRemoteData?.consume()
        if (remoteEither != null) {
            val result = either {
                val remoteData = remoteEither
                    .mapLeft(DataError::Remote)
                    .bind()

                localEither.fold(
                    ifLeft = {
                        if (remoteData.isFirstPage()) remoteData
                        else throw AssertionError("Remote data is not first page, but there is no cached data")
                    },
                    ifRight = { localData -> remoteData.copy(data = localData.data) }
                )
            }
            emit(result as Either<DataError.Remote, PagedData<T, P>>)
        } else {
            localEither.tap { local -> emit(local.right()) }
        }
    }.distinctUntilChanged()
}
