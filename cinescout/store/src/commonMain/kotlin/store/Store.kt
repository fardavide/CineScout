package store

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import cinescout.error.DataError
import cinescout.error.NetworkError
import cinescout.utils.kotlin.ticker
import com.soywiz.klock.DateTime
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combineTransform
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.withContext

/**
 * Creates a flow that combines Local data and Remote data.
 * First emit Local data, only if available, then emits Local data updated from Remote or Remote errors
 *
 * @param key and unique identifier for the data.
 * @param refresh see [Refresh]
 * @param fetch lambda that returns Remote data
 * @param read lambda that returns a Flow of Local data
 * @param write lambda that saves Remote data to Local
 */
fun <T> StoreOwner.Store(
    key: StoreKey,
    refresh: Refresh = Refresh.Once,
    fetch: suspend () -> Either<NetworkError, T>,
    read: () -> Flow<T>,
    write: suspend (T) -> Unit
): Store<T> = StoreImpl(
    buildStoreFlow(
        dispatcher = dispatcher,
        findFetchData = { findFetchData(key) },
        insertFetchData = { data -> insertFetchData(key, data) },
        refresh = refresh,
        fetch = fetch,
        read = read,
        write = write
    )
)

@Deprecated("Use with key", ReplaceWith("Store(key, refresh, fetch, read, write"))
fun <T> Store(
    refresh: Refresh = Refresh.Once,
    fetch: suspend () -> Either<NetworkError, T>,
    read: () -> Flow<Either<DataError.Local, T>>,
    write: suspend (T) -> Unit
): Store<T> = StoreImpl(buildStoreFlow(refresh, fetch, read, write))

interface Store<T> : Flow<Either<DataError, T>>

internal class StoreImpl<T> (internal val flow: Flow<Either<DataError, T>>) :
    Store<T>, Flow<Either<DataError, T>> by flow

private fun <T> buildStoreFlow(
    refresh: Refresh,
    fetch: suspend () -> Either<NetworkError, T>,
    read: () -> Flow<Either<DataError.Local, T>>,
    write: suspend (T) -> Unit
): Flow<Either<DataError, T>> =
    buildStoreFlow(
        dispatcher = Dispatchers.Default,
        findFetchData = { null },
        insertFetchData = {},
        refresh = refresh,
        fetch = fetch,
        read = { read().map { (it as Either.Right<T>).value } },
        write = write
    )

@Suppress("LongParameterList")
private fun <T> buildStoreFlow(
    dispatcher: CoroutineDispatcher,
    fetch: suspend () -> Either<NetworkError, T>,
    findFetchData: suspend () -> FetchData?,
    insertFetchData: suspend (FetchData) -> Unit,
    read: () -> Flow<T>,
    refresh: Refresh,
    write: suspend (T) -> Unit
): Flow<Either<DataError, T>> {

    suspend fun writeWithFetchData(t: T) {
        withContext(dispatcher) {
            write.invoke(t)
            val fetchData = FetchData(dateTime = DateTime.now())
            insertFetchData(fetchData)
        }
    }

    fun readWithFetchData(): Flow<Either<DataError.Local.NoCache, T>> = read()
        .map { data ->
            when (findFetchData()) {
                null -> DataError.Local.NoCache.left()
                else -> data.right()
            }
        }

    val remoteFlow = when (refresh) {
        Refresh.IfNeeded -> flow {
            readWithFetchData().first().tapLeft {
                val remoteDataEither = fetch()
                remoteDataEither.tap { remoteData ->
                    writeWithFetchData(remoteData)
                }
                emit(ConsumableData.of(remoteDataEither))
            }
        }
        is Refresh.IfOlderThan -> flow {
            val fetchTimeMs = findFetchData()?.dateTime?.unixMillisLong ?: 0
            val expirationTimeMs = DateTime.now().unixMillisLong - refresh.interval.inWholeSeconds
            val isDataExpired = fetchTimeMs < expirationTimeMs
            if (isDataExpired) {
                val remoteDataEither = fetch()
                remoteDataEither.tap { remoteData ->
                    writeWithFetchData(remoteData)
                }
                emit(ConsumableData.of(remoteDataEither))
            }
        }
        Refresh.Never -> emptyFlow()
        Refresh.Once -> flow {
            val remoteDataEither = fetch()
            remoteDataEither.tap { remoteData ->
                writeWithFetchData(remoteData)
            }
            emit(ConsumableData.of(remoteDataEither))
        }
        is Refresh.WithInterval -> ticker<ConsumableData<T>?>(refresh.interval) {
            val remoteDataEither = fetch()
            remoteDataEither.tap { remoteData ->
                writeWithFetchData(remoteData)
            }
            emit(ConsumableData.of(remoteDataEither))
        }.onStart { emit(null) }
    }
    return combineTransform(
        remoteFlow.onStart { emit(null) },
        readWithFetchData()
    ) { consumableRemoteData, localEither ->
        consumableRemoteData?.consume { remoteEither ->
            val remote = remoteEither.mapLeft { networkError ->
                DataError.Remote(networkError = networkError)
            }
            emit(remote)
        }
        localEither
            .tap { local -> emit(local.right()) }
            .tapLeft { localError -> if (refresh is Refresh.Never) emit(localError.left()) }
    }.distinctUntilChanged()
}
