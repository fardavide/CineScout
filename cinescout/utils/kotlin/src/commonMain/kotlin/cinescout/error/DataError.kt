package cinescout.error

/**
 * Errors related to Data
 */
sealed interface DataError {

    /**
     * Errors related to Local persistence
     */
    sealed interface Local : DataError {

        /**
         * There's no cached value in the local persistence
         */
        object NoCache : Local
    }

    /**
     * Error fetching date from Remote source
     */
    data class Remote(val networkError: NetworkError) : DataError
}

fun DataError(networkError: NetworkError) = DataError.Remote(networkError)
