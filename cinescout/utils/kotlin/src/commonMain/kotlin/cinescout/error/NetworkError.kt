package cinescout.error

/**
 * Errors related to Network
 */
sealed interface NetworkError {

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
