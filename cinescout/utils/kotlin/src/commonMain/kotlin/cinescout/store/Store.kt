package cinescout.store

import arrow.core.Either
import arrow.core.continuations.either
import arrow.core.left
import arrow.core.right
import cinescout.error.DataError
import cinescout.error.NetworkError
import cinescout.utils.kotlin.DataRefreshInterval
import cinescout.utils.kotlin.ticker
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

interface Store<T> : Flow<Either<DataError.Remote, T>>

interface PagedStore<T> : Store<PagedData<T>> {

    suspend fun getAll(): Either<DataError.Remote, List<T>>

    fun loadAll(): PagedStore<T>

    fun loadMore(): PagedStore<T>
}

@Suppress("UNCHECKED_CAST")
fun <T, R> Store<T>.map(
    transform: (Either<DataError.Remote, T>) -> Either<DataError.Remote, R>
): Store<R> = with(this as StoreImpl<T>) {
    StoreImpl(flow.map(transform))
}

@Suppress("UNCHECKED_CAST")
fun <T, R> PagedStore<T>.map(
    transform: (Either<DataError.Remote, PagedData<T>>) ->
    Either<DataError.Remote, PagedData<R>>
): PagedStore<R> = with(this as PagedStoreImpl<T>) {
    return PagedStoreImpl(flow.map(transform), onLoadMore, onLoadAll)
}

private fun <T> buildStoreFlow(
    fetch: suspend () -> Either<NetworkError, T>,
    read: () -> Flow<Either<DataError.Local, T>>,
    write: suspend (T) -> Unit
): Flow<Either<DataError.Remote, T>> {
    return combineTransform(
        ticker<ConsumableData<T>?>(DataRefreshInterval) {
            val remoteDataEither = fetch()
            remoteDataEither.tap { remoteData ->
                write(remoteData)
            }
            emit(ConsumableData.of(remoteDataEither))
        }.onStart { emit(null) },
        read()
    ) { consumableRemoteData, localEither ->
        consumableRemoteData?.consume { remoteEither ->
            val remote = remoteEither.mapLeft { networkError ->
                DataError.Remote(networkError = networkError)
            }
            emit(remote)
        }
        localEither.tap { local -> emit(local.right()) }
    }
}

private fun <T, B> buildPagedStoreFlow(
    fetch: suspend (bookmark: B) -> Either<NetworkError, PagedData.Remote<T>>,
    read: () -> Flow<Either<DataError.Local, List<T>>>,
    write: suspend (List<T>) -> Unit,
    loadMoreTrigger: Flow<B>
): Flow<Either<DataError.Remote, PagedData<T>>> =
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
                        val local = localEither.map {
                            @Suppress("USELESS_CAST")
                            it as PagedData<T>
                        }
                        DataError.Remote(networkError = networkError)
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


internal class StoreImpl<T>( internal val flow: Flow<Either<DataError.Remote, T>>) :
    Store<T>, Flow<Either<DataError.Remote, T>> by flow


internal class PagedStoreImpl<T>(
    internal val flow: Flow<Either<DataError.Remote, PagedData<T>>>,
    internal val onLoadMore: () -> Unit,
    internal val onLoadAll: () -> Unit
) : PagedStore<T>, Flow<Either<DataError.Remote, PagedData<T>>> by flow {

    override suspend fun getAll(): Either<DataError.Remote, List<T>> {
        onLoadAll()
        return this.transform { either ->
            either.tap { pagedData ->
                if (pagedData.isLastPage()) {
                    emit(pagedData.data.right())
                }
            }.tapLeft { error ->
                emit(DataError.Remote(networkError = error.networkError).left())
            }
        }.first()
    }

    override fun loadAll(): PagedStore<T> {
        onLoadAll()
        return this
    }

    override fun loadMore(): PagedStore<T> {
        onLoadMore()
        return this
    }
}
