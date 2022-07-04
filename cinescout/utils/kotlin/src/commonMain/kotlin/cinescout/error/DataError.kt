package cinescout.error

import arrow.core.Either

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
    data class Remote<T>(val localData: Either<Local, T>, val networkError: NetworkError) : DataError
}
