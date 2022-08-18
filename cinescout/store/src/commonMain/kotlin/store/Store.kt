package store

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import cinescout.error.DataError
import cinescout.error.NetworkError
import cinescout.utils.kotlin.ticker
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combineTransform
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onStart

/**
 * Creates a flow that combines Local data and Remote data.
 * First emit Local data, only if available, then emits Local data updated from Remote or Remote errors
 *
 * @param refresh:
 *  * If [Refresh.Once] refresh each [DefaultRefreshInterval]
 *  * If [Refresh.IfNeeded] refresh only if there is no local data
 *  * If [Refresh.Never] do not refresh
 *  Default is [Refresh.Once]
 * @param fetch lambda that returns Remote data
 * @param read lambda that returns a Flow of Local data
 * @param write lambda that saves Remote data to Local
 */
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
): Flow<Either<DataError, T>> {
    val remoteFlow = when (refresh) {
        Refresh.IfNeeded -> flow {
            emit(null)
            read().first().tapLeft {
                val remoteDataEither = fetch()
                remoteDataEither.tap { remoteData ->
                    write(remoteData)
                }
                emit(ConsumableData.of(remoteDataEither))
            }
        }
        Refresh.Never -> flowOf(null)
        Refresh.Once -> flow {
            emit(null)
            val remoteDataEither = fetch()
            remoteDataEither.tap { remoteData ->
                write(remoteData)
            }
            emit(ConsumableData.of(remoteDataEither))
        }
        is Refresh.WithInterval -> ticker<ConsumableData<T>?>(refresh.interval) {
            val remoteDataEither = fetch()
            remoteDataEither.tap { remoteData ->
                write(remoteData)
            }
            emit(ConsumableData.of(remoteDataEither))
        }.onStart { emit(null) }
    }
    return combineTransform(
        remoteFlow,
        read()
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
