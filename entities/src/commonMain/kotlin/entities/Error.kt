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
     * Network connectivity is not available
     */
    object NoNetwork : NetworkError()

    /**
     * Requested url cannot be found.
     * 404 error
     */
    object NotFound : NetworkError()

    /**
     * Requested host is not reachable
     */
    object Unreachable : NetworkError()
}
