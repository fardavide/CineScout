package cinescout.utils.kotlin

import arrow.core.Either
import arrow.core.right
import cinescout.error.DataError
import cinescout.error.NetworkError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combineTransform
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.transform

/**
 * Creates a flow that combines Local data and Remote data
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

fun <T, B> PagedStore(
    initialBookmark: B,
    createNextBookmark: (lastData: T, currentBookmark: B) -> B,
    fetch: suspend (bookmark: B) -> Either<NetworkError, T>,
    read: () -> Flow<Either<DataError.Local, T>>,
    write: suspend (T) -> Unit
): PagedStore<T> {
    var bookmark = initialBookmark
    var lastData: T? = null
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
            if (shouldLoadAll && lastData != data) {
                onLoadMore()
            }
            lastData = data
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

interface PagedStore<T> : Store<T> {

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
    fetch: suspend (bookmark: B) -> Either<NetworkError, T>,
    read: () -> Flow<Either<DataError.Local, T>>,
    write: suspend (T) -> Unit,
    loadMoreTrigger: Flow<B>
): Flow<Either<DataError.Remote<T>, T>> =
    combineTransform(
        loadMoreTrigger.transform<B, Either<NetworkError, T>?> { bookmark ->
            val remoteDataEither = fetch(bookmark)
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

private class StoreImpl<T>(flow: Flow<Either<DataError.Remote<T>, T>>) :
    Store<T>, Flow<Either<DataError.Remote<T>, T>> by flow

private class PagedStoreImpl<T>(
    flow: Flow<Either<DataError.Remote<T>, T>>,
    private val onLoadMore: () -> Unit,
    private val onLoadAll: () -> Unit
) :
    PagedStore<T>, Flow<Either<DataError.Remote<T>, T>> by flow {

    override fun loadAll() {
        onLoadAll()
    }

    override fun loadMore() {
        onLoadMore()
    }
}
