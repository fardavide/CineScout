package cinescout.utils.kotlin

import arrow.core.Either
import arrow.core.right
import cinescout.error.DataError
import cinescout.error.NetworkError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combineTransform

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
): Flow<Either<DataError.Remote<T>, T>> =
    combineTransform(
        ticker(DataRefreshInterval) {
            emit(null)
            val remoteDataEither = fetch()
            remoteDataEither.tap { remoteData ->
                write(remoteData)
            }
            emit(remoteDataEither)
        },
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
