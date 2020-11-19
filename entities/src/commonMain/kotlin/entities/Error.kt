package entities

/**
 * Base interface of errors
 */
interface Error

/**
 * Unknown error
 */
object UnknownError : Error

/**
 * Errors related to Network
 */
sealed class NetworkError : Error {

    /**
     * Request is forbidden
     * 403 error
     */
    object Forbidden : NetworkError()

    /**
     * Network connectivity is not available
     */
    object NoNetwork : NetworkError()

    /**
     * Requested url cannot be found.
     * 404 error
     */
    object NotFound : NetworkError()

    /**
     * Server has encountered an error.
     * 500 error
     */
    object Internal : NetworkError()

    /**
     * Request is not authorized
     * 401 error
     */
    object Unauthorized : NetworkError()

    /**
     * Requested host is not reachable
     */
    object Unreachable : NetworkError()
}

/**
 * Resource is not found in local cache
 */
object MissingCache : Error

/**
 * Error relate to a resource, it could be from [Local] or [Network]
 */
sealed class ResourceError : Error {
    data class Local(private val e: MissingCache): ResourceError()
    data class Network(val error: NetworkError): ResourceError()
}
