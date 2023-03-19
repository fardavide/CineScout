package cinescout.suggestions.domain.model

import cinescout.error.NetworkError

sealed interface SuggestionError {

    object NoSuggestions : SuggestionError

    data class Source(val networkError: NetworkError) : SuggestionError
}
