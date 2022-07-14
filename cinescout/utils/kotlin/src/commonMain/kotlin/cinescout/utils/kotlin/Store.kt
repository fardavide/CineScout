package cinescout.utils.kotlin

import arrow.core.Either
import arrow.core.continuations.either
import arrow.core.left
import arrow.core.right
import cinescout.error.DataError
import cinescout.error.NetworkError
import cinescout.model.PagedData
import cinescout.model.toPagedData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combineTransform
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.transform

/**
 * Creates a flow that combines Local data and Remote data and refresh each [DataRefreshInterval]
 * First emit Local data, only if available, then emits Local data updated from Remote or Remote errors that wrap
 *  a Local data
 *
 * @param fetch lambda that returns Remote data
 * @param read lambda that returns a Flow of Local data
 * @param write lambda that saves Remote data to Local
 */
fun <T> Store(
    fetch: suspend () -> Either<NetworkError, T>,
    read: () -> Flow<Either<DataError.Local, T>>,
    write: suspend (T) -> Unit
): Store<T> = StoreImpl(buildStoreFlow(fetch, read, write))

/**
 * Creates a flow that combines Local data and Remote data, when remote data is paged
 * @see PagedStore
 *
 * First emit Local data, only if available, then emits Local data updated from Remote or Remote errors that wrap
 *  a Local data
 *
 * @param fetch lambda that returns Remote data
 * @param read lambda that returns a Flow of Local data
 * @param write lambda that saves Remote data to Local
 */
fun <T> PagedStore(
    fetch: suspend (page: Int) -> Either<NetworkError, PagedData.Remote<T>>,
    read: () -> Flow<Either<DataError.Local, List<T>>>,
    write: suspend (List<T>) -> Unit
): PagedStore<T> = PagedStore(
    initialBookmark = 1,
    createNextBookmark = { _, currentBookmark: Int -> currentBookmark + 1 },
    fetch = fetch,
    read = read,
    write = write
)

/**
 * Creates a flow that combines Local data and Remote data, when remote data is paged
 * @see PagedStore
 *
 * First emit Local data, only if available, then emits Local data updated from Remote or Remote errors that wrap
 *  a Local data
 *
 * @param initialBookmark initial bookmark to start fetching from
 * @param createNextBookmark lambda that creates the next bookmark to fetch from
 * @param fetch lambda that returns Remote data
 * @param read lambda that returns a Flow of Local data
 * @param write lambda that saves Remote data to Local
 */
fun <T, B> PagedStore(
    initialBookmark: B,
    createNextBookmark: (lastData: PagedData<T>, currentBookmark: B) -> B,
    fetch: suspend (bookmark: B) -> Either<NetworkError, PagedData.Remote<T>>,
    read: () -> Flow<Either<DataError.Local, List<T>>>,
    write: suspend (List<T>) -> Unit
): PagedStore<T> {
    var bookmark = initialBookmark
    val loadMoreTrigger = MutableStateFlow(initialBookmark)
    val onLoadMore = { loadMoreTrigger.value = bookmark }
    var shouldLoadAll = false

    val flow = buildPagedStoreFlow(
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

interface Store<T> : Flow<Either<DataError.Remote<T>, T>>

interface PagedStore<T> : Store<PagedData<T>> {

    suspend fun getAll(): Either<DataError.Remote<List<T>>, List<T>>

    fun loadAll()

    fun loadMore()
}

private fun <T> buildStoreFlow(
    fetch: suspend () -> Either<NetworkError, T>,
    read: () -> Flow<Either<DataError.Local, T>>,
    write: suspend (T) -> Unit
): Flow<Either<DataError.Remote<T>, T>> =
    combineTransform(
        ticker<Either<NetworkError, T>?>(DataRefreshInterval) {
            val remoteDataEither = fetch()
            remoteDataEither.tap { remoteData ->
                write(remoteData)
            }
            emit(remoteDataEither)
        }.onStart { emit(null) },
        read()
    ) { remoteEither, localEither ->
        if (remoteEither != null) {
            val remote = remoteEither.mapLeft { networkError ->
                DataError.Remote(localData = localEither, networkError = networkError)
            }
            emit(remote)
        } else {
            localEither.tap { local -> emit(local.right()) }
        }
    }

private fun <T, B> buildPagedStoreFlow(
    fetch: suspend (bookmark: B) -> Either<NetworkError, PagedData.Remote<T>>,
    read: () -> Flow<Either<DataError.Local, List<T>>>,
    write: suspend (List<T>) -> Unit,
    loadMoreTrigger: Flow<B>
): Flow<Either<DataError.Remote<PagedData<T>>, PagedData<T>>> =
    combineTransform(
        loadMoreTrigger.transform<B, Either<NetworkError, PagedData.Remote<T>>?> { bookmark ->
            val remoteDataEither = fetch(bookmark)
            remoteDataEither.tap { remoteData ->
                write(remoteData.data)
            }
            emit(remoteDataEither)
        }.onStart { emit(null) },
        read().map { either -> either.map { list -> list.toPagedData() } }
    ) { remoteEither, localEither ->

        if (remoteEither != null) {
            val result = either {
                val remoteData = remoteEither
                    .mapLeft { networkError ->
                        @Suppress("USELESS_CAST") val local = localEither.map { it as PagedData<T> }
                        DataError.Remote(localData = local, networkError = networkError)
                    }
                    .bind()

                localEither.fold(
                    ifLeft = {
                        if (remoteData.isFirstPage()) remoteData
                        else throw AssertionError("Remote data is not first page, but there is no cached data") },
                    ifRight = { localData -> remoteData.copy(data = localData.data) }
                )
            }
            emit(result)
        } else {
            localEither.tap { local -> emit(local.right()) }
        }
    }

private class StoreImpl<T>(flow: Flow<Either<DataError.Remote<T>, T>>) :
    Store<T>, Flow<Either<DataError.Remote<T>, T>> by flow

private class PagedStoreImpl<T>(
    flow: Flow<Either<DataError.Remote<PagedData<T>>, PagedData<T>>>,
    private val onLoadMore: () -> Unit,
    private val onLoadAll: () -> Unit
) : PagedStore<T>, Flow<Either<DataError.Remote<PagedData<T>>, PagedData<T>>> by flow {

    override suspend fun getAll(): Either<DataError.Remote<List<T>>, List<T>> {
        onLoadAll()
        return this.transform { either ->
            either.tap { pagedData ->
                if (pagedData.isLastPage()) {
                    emit(pagedData.data.right())
                }
            }.tapLeft { error ->
                val localData = error.localData.map { it.data }
                emit(DataError.Remote(localData = localData, networkError = error.networkError).left())
            }
        }.first()
    }

    override fun loadAll() {
        onLoadAll()
    }

    override fun loadMore() {
        onLoadMore()
    }
}
