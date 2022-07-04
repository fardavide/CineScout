package cinescout.error

sealed interface DataError {

    sealed interface Local : DataError {

        object NoCache: DataError.Local
    }

    data class Remote(val networkError: NetworkError) : DataError
}
