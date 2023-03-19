package cinescout.suggestions.domain.model

import cinescout.error.DataError
import cinescout.error.NetworkError

sealed interface SuggestionError {

    object NoSuggestions : SuggestionError

    data class Source(val networkError: NetworkError) : SuggestionError {

        @Deprecated("Use networkError instead")
        val dataError = DataError(networkError)

        companion object {

            operator fun invoke(dataError: DataError.Remote) = Source(dataError.networkError)
        }
    }
}
