package store

import arrow.core.Either
import arrow.core.continuations.either
import arrow.core.left
import arrow.core.right
import cinescout.error.DataError
import cinescout.model.NetworkOperation
import co.touchlab.kermit.Logger
import com.soywiz.klock.DateTime
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combineTransform
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import store.builder.pagedDataOf
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
inline fun <T : Any, reified P : Paging.Page, KeyId : Any> StoreOwner.PagedStore(
    key: StoreKey<KeyId>,
    refresh: Refresh = Refresh.Once,
    initialPage: P = Initial(),
    crossinline createNextPage: (lastData: PagedData<T, P>, currentPage: P) -> P = { lastData, _ ->
        val nextPage = (lastData.paging + 1) as P
        Logger.v("Creating next page: $nextPage. Last data: $lastData")
        nextPage
    },
    fetch: PagedFetcher<T, P>,
    noinline read: () -> Flow<List<T>>,
    noinline write: suspend (List<T>) -> Unit,
    noinline delete: suspend (List<T>) -> Unit = {}
): PagedStore<T, Paging> {
    var currentPage = initialPage
    val loadMoreTrigger = MutableStateFlow(initialPage)
    val onLoadMore = { loadMoreTrigger.value = currentPage }
    var shouldLoadAll = false

    val flow = buildPagedStoreFlow(
        delete = delete,
        fetch = { fetch(currentPage).tap { data -> currentPage = createNextPage(data, currentPage) } },
        findFetchData = { paging -> getFetchData(key.paged(paging).value()) },
        insertFetchData = { paging, data -> saveFetchData(key.paged(paging).value(), data) },
        initialPage = initialPage,
        read = read,
        refresh = refresh,
        write = write,
        skipFetch = { paging ->
            pagedDataOf<T, P>(paging = paging).also { currentPage = createNextPage(it, currentPage) }
        },
        loadMoreTrigger = loadMoreTrigger
    ).onEach { either ->
        either.tap { data ->
            if (shouldLoadAll && data.isLastPage().not() && data.paging !is Paging.Unknown) {
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

inline fun <T : Any, reified P : Paging.Page, KeyId : Any> StoreOwner.PagedStore(
    key: StoreKey<KeyId>,
    refresh: Refresh = Refresh.Once,
    initialPage: P = Initial(),
    crossinline createNextPage: (lastData: PagedData<T, P>, currentPage: P) -> P = { lastData, _ ->
        val nextPage = (lastData.paging + 1) as P
        Logger.v("Creating next page: $nextPage. Last data: $lastData")
        nextPage
    },
    crossinline fetch: suspend (page: P) -> Either<NetworkOperation, PagedData.Remote<T, P>>,
    noinline read: () -> Flow<List<T>>,
    noinline write: suspend (List<T>) -> Unit,
    noinline delete: suspend (List<T>) -> Unit = {}
): PagedStore<T, Paging> = PagedStore(
    key = key,
    refresh = refresh,
    initialPage = initialPage,
    createNextPage = createNextPage,
    fetch = PagedFetcher.forOperation(fetch),
    read = read,
    write = write,
    delete = delete
)

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

@PublishedApi
@Suppress("ComplexMethod", "LongParameterList")
internal fun <T, P : Paging.Page> buildPagedStoreFlow(
    delete: suspend (List<T>) -> Unit,
    fetch: suspend (paging: P) -> Either<NetworkOperation, PagedData.Remote<T, P>>,
    findFetchData: suspend (paging: P) -> FetchData?,
    initialPage: P,
    insertFetchData: suspend (paging: P, FetchData) -> Unit,
    loadMoreTrigger: Flow<P>,
    read: () -> Flow<List<T>>,
    refresh: Refresh,
    skipFetch: (P) -> PagedData.Remote<T, P>,
    write: suspend (List<T>) -> Unit
): Flow<Either<DataError.Remote, PagedData<T, Paging>>> {
    val allRemoteData = mutableListOf<T>()
    var hasFetchedLastPage = false

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
        if (hasFetchedLastPage) {
            delete(read().first() - allRemoteData.toSet())
        }
    }

    suspend fun FlowCollector<ConsumableData<PagedData.Remote<T, P>>?>.handleFetch(paging: P) {
        val remoteDataEither = fetch(paging)
        remoteDataEither.tap { remoteData ->
            allRemoteData += remoteData.data
            hasFetchedLastPage = remoteData.isLastPage()
            writeWithFetchData(remoteData)
        }
        emit(ConsumableData.of(remoteDataEither))
    }

    fun remoteFlow(paging: P): Flow<ConsumableData<PagedData.Remote<T, P>>?> = when (refresh) {
        Refresh.IfNeeded -> flow {
            if (paging.page == 1 || findFetchData(paging) == null) {
                handleFetch(paging)
            } else {
                emit(ConsumableData.of(skipFetch(paging).right()))
            }
        }
        is Refresh.IfExpired -> flow {
            val fetchTimeMs = findFetchData(paging)?.dateTime?.unixMillisLong ?: 0
            val expirationTimeMs = DateTime.now().unixMillisLong - refresh.validity.inWholeSeconds
            val isDataExpired = fetchTimeMs < expirationTimeMs
            if (paging.page == 1 || isDataExpired) {
                handleFetch(paging)
            } else {
                emit(ConsumableData.of(skipFetch(paging).right()))
            }
        }
        Refresh.Never -> emptyFlow()
        Refresh.Once, is Refresh.WithInterval -> flow {
            handleFetch(paging)
        }
    }

    return combineTransform(
        loadMoreTrigger.flatMapConcat { paging -> remoteFlow(paging) }
            .onStart { emit(null) },
        readWithFetchData()
    ) { consumableRemoteData, localEither ->

        val remoteEither = consumableRemoteData?.consume()
        if (remoteEither != null) {
            val remoteResult = either {
                val remoteData = remoteEither.bind()

                localEither.fold(
                    ifLeft = {
                        if (remoteData.isFirstPage()) remoteData
                        else throw AssertionError("Remote data is not first page, but there is no cached data")
                    },
                    ifRight = { localData ->
                        remoteData.copy(data = localData.data)
                    }
                )
            }
            val result = remoteResult.fold(
                ifLeft = { networkOperation ->
                    when (networkOperation) {
                        is NetworkOperation.Error -> DataError.Remote(networkOperation.error).left()
                        is NetworkOperation.Skipped -> read().first().toPagedData().right()
                    }
                },
                ifRight = { remote -> remote.right() }
            )
            emit(result)
        } else {
            localEither.tap { local -> emit(local.right()) }
        }
    }.distinctUntilChanged()
}
