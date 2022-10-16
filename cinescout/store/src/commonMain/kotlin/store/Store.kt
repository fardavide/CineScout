package store

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import cinescout.error.DataError
import cinescout.model.NetworkOperation
import cinescout.utils.kotlin.ticker
import com.soywiz.klock.DateTime
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.combineTransform
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

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
inline fun <T : Any, KeyId : Any> StoreOwner.Store(
    key: StoreKey<KeyId>,
    refresh: Refresh = Refresh.Once,
    fetch: Fetcher<T>,
    crossinline read: () -> Flow<T?> = { flowOf(null) },
    crossinline write: suspend (T) -> Unit
): Store<T> = StoreImpl(
    buildStoreFlow(
        fetch = { fetch() },
        findFetchData = { getFetchData(key.value()) },
        insertFetchData = { data -> saveFetchData(key.value(), data) },
        read = { read() },
        refresh = refresh,
        write = { write(it) }
    )
)

fun <T : Any, KeyId : Any> StoreOwner.Store(
    key: StoreKey<KeyId>,
    refresh: Refresh = Refresh.Once,
    fetch: suspend () -> Either<NetworkOperation, T>,
    read: () -> Flow<T?> = { flowOf(null) },
    write: suspend (T) -> Unit
): Store<T> = Store(
    key = key,
    refresh = refresh,
    fetch = Fetcher.forOperation(fetch),
    read = read,
    write = write
)

interface Store<T> : Flow<Either<DataError, T>>

@PublishedApi
internal class StoreImpl<T> (internal val flow: Flow<Either<DataError, T>>) :
    Store<T>, Flow<Either<DataError, T>> by flow

@PublishedApi
internal fun <T> buildStoreFlow(
    fetch: suspend () -> Either<NetworkOperation, T>,
    findFetchData: suspend () -> FetchData?,
    insertFetchData: suspend (FetchData) -> Unit,
    read: () -> Flow<T?>,
    refresh: Refresh,
    write: suspend (T) -> Unit
): Flow<Either<DataError, T>> {

    fun readWithFetchData(): Flow<Either<DataError.Local.NoCache, T>> = read()
        .map { data ->
            when (findFetchData()) {
                null -> DataError.Local.NoCache.left()
                else -> data?.right() ?: DataError.Local.NoCache.left()
            }
        }

    suspend fun writeWithFetchData(t: T) {
        write.invoke(t)
        val fetchData = FetchData(dateTime = DateTime.now())
        insertFetchData(fetchData)
    }

    suspend fun FlowCollector<ConsumableData<T>?>.handleFetch() {
        val remoteDataEither = fetch()
        remoteDataEither.tap { remoteData ->
            writeWithFetchData(remoteData)
        }
        emit(ConsumableData.of(remoteDataEither))
    }

    val remoteFlow = when (refresh) {
        Refresh.IfNeeded -> flow {
            if (findFetchData() == null) {
                handleFetch()
            }
        }
        is Refresh.IfExpired -> flow {
            val fetchTimeMs = findFetchData()?.dateTime?.unixMillisLong ?: 0
            val expirationTimeMs = DateTime.now().unixMillisLong - refresh.validity.inWholeSeconds
            val isDataExpired = fetchTimeMs < expirationTimeMs
            if (isDataExpired) {
                handleFetch()
            }
        }
        Refresh.Never -> emptyFlow()
        Refresh.Once -> flow {
            handleFetch()
        }
        is Refresh.WithInterval -> ticker(refresh.interval) {
            handleFetch()
        }
    }
    return combineTransform(
        remoteFlow.onStart { emit(null) },
        readWithFetchData()
    ) { consumableRemoteData, localEither ->
        consumableRemoteData?.consume { remoteEither ->
            val result = remoteEither.fold(
                ifLeft = { networkOperation ->
                    when (networkOperation) {
                        is NetworkOperation.Error -> DataError.Remote(networkOperation.error).left()
                        is NetworkOperation.Skipped -> read().first()?.right() ?: DataError.Local.NoCache.left()
                    }
                },
                ifRight = { it.right() }
            )
            emit(result)
        }
        localEither
            .tap { local -> emit(local.right()) }
            .tapLeft { localError -> if (refresh is Refresh.Never) emit(localError.left()) }
    }.distinctUntilChanged()
}
