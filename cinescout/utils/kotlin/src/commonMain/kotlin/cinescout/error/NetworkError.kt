package cinescout.error

import arrow.core.Either
import arrow.core.handleErrorWith
import arrow.core.left
import arrow.core.right

/**
 * Errors related to Network
 */
sealed interface NetworkError {

    /**
     * Bad request
     * 400 error
     */
    object BadRequest : NetworkError

    /**
     * Request is forbidden
     * 403 error
     */
    object Forbidden : NetworkError

    /**
     * Network connectivity is not available
     */
    object NoNetwork : NetworkError

    /**
     * Requested url cannot be found.
     * 404 error
     */
    object NotFound : NetworkError

    /**
     * Server has encountered an error.
     * 500 error
     */
    object Internal : NetworkError

    /**
     * Request is not authorized
     * 401 error
     */
    object Unauthorized : NetworkError

    /**
     * Requested host is not reachable
     */
    object Unreachable : NetworkError

    /**
     * Unknown error
     * 520 error
     */
    object Unknown : NetworkError
}

fun <T> Either<NetworkError, List<T>>.handleNotFoundAsEmptyList(): Either<NetworkError, List<T>> =
    handleErrorWith { networkError ->
        when (networkError) {
            NetworkError.NotFound -> emptyList<T>().right()
            else -> networkError.left()
        }
    }
