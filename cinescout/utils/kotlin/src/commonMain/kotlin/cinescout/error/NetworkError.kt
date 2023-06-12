package cinescout.error

import co.touchlab.kermit.Logger

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

fun Logger.Companion.e(networkError: NetworkError) {
    e(networkError.toString())
}
