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
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart

/**
 * Creates a flow that combines Local data and Remote data.
 * First emit Local data, only if available, then emits Local data updated from Remote or Remote errors
 *
 * @param key and unique identifier for the data.
 * @param refresh see [Refresh]
 * @param fetcher [Fetcher] that returns a Flow of Remote data
 * @param reader [Reader] that returns a Flow of Local data
 * @param write lambda that saves Remote data to Local
 * @param delete lambda that deletes Local data
 */
inline fun <T : Any, KeyId : Any> StoreOwner.Store(
    key: StoreKey<KeyId>,
    refresh: Refresh = Refresh.Once,
    fetcher: Fetcher<T>,
    reader: Reader<T> = Reader.Empty(),
    crossinline write: suspend (T) -> Unit,
    noinline delete: suspend (T) -> Unit = {}
): Store<T> = StoreImpl(
    buildStoreFlow(
        delete = delete,
        fetcher = fetcher,
        findFetchData = { getFetchData(key.value()) },
        insertFetchData = { data -> saveFetchData(key.value(), data) },
        reader = reader,
        refresh = refresh,
        write = { write(it) }
    )
)

fun <T : Any, KeyId : Any> StoreOwner.Store(
    key: StoreKey<KeyId>,
    refresh: Refresh = Refresh.Once,
    fetch: suspend () -> Either<NetworkOperation, T>,
    read: (() -> Flow<T?>)? = null,
    write: suspend (T) -> Unit,
    delete: suspend (T) -> Unit = {}
): Store<T> = Store(
    delete = delete,
    key = key,
    refresh = refresh,
    fetcher = Fetcher.forOperation(fetch),
    reader = Reader.fromSource(read),
    write = write
)

interface Store<T> : Flow<Either<DataError, T>>

@PublishedApi
internal class StoreImpl<T>(internal val flow: Flow<Either<DataError, T>>) :
    Store<T>, Flow<Either<DataError, T>> by flow

@PublishedApi
@Suppress("CyclomaticComplexMethod", "LongParameterList")
internal fun <T : Any> buildStoreFlow(
    delete: suspend (T) -> Unit,
    fetcher: Fetcher<T>,
    findFetchData: suspend () -> FetchData?,
    insertFetchData: suspend (FetchData) -> Unit,
    reader: Reader<T>,
    refresh: Refresh,
    write: suspend (T) -> Unit
): Flow<Either<DataError, T>> {
    var allRemoteData: List<*>? = null

    fun readWithFetchData(): Flow<Either<DataError.Local.NoCache, T>> = reader.flow
        .map { data ->
            when (findFetchData()) {
                null -> DataError.Local.NoCache.left()
                else -> data?.right() ?: DataError.Local.NoCache.left()
            }
        }

    suspend fun writeWithFetchData(t: T) {
        val fetchData = FetchData(dateTime = DateTime.now())
        insertFetchData(fetchData)
        write(t)
    }

    suspend fun FlowCollector<ConsumableData<T>?>.handleFetch() {
        val flow = fetcher.flow.onEach { remoteDataEither ->
            remoteDataEither.onRight { remoteData ->
                if (remoteData is List<*>) {
                    allRemoteData = remoteData
                }
                writeWithFetchData(remoteData)
            }
        }.map { ConsumableData.of(it) }.onCompletion {
            val localData = reader.flow.first()
            if (allRemoteData != null && localData is List<*>) {
                val toDelete = (localData as List<Any?>).filterNot { element ->
                    element in allRemoteData as List<Any?>
                }
                @Suppress("UNCHECKED_CAST")
                delete(toDelete as T)
            }
        }
        emitAll(flow)
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
        localEither
            .onRight { local -> emit(local.right()) }
            .onLeft { localError -> if (refresh is Refresh.Never) emit(localError.left()) }
        consumableRemoteData?.consume { remoteEither ->
            remoteEither.onLeft { networkOperation ->
                val data = when (networkOperation) {
                    is NetworkOperation.Error -> DataError.Remote(networkOperation.error).left()
                    is NetworkOperation.Skipped -> reader.flow.first()?.right() ?: DataError.Local.NoCache.left()
                }
                emit(data)
            }.onRight { remoteData ->
                if (reader is Reader.Empty) {
                    emit(remoteData.right())
                }
            }
        }
    }.distinctUntilChanged()
}
