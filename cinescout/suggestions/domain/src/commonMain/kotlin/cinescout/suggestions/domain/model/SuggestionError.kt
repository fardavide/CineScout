package cinescout.suggestions.domain.model

import cinescout.error.DataError
import cinescout.error.NetworkError

sealed interface SuggestionError {

    object NoSuggestions : SuggestionError

    data class Source(val dataError: DataError) : SuggestionError {

        companion object {

            operator fun invoke(networkError: NetworkError) = Source(DataError.Remote(networkError))
        }
    }
}
