package cinescout.movies.domain.model

import cinescout.error.DataError

sealed interface SuggestionError {

    object NoSuggestions : SuggestionError

    data class Source(val dataError: DataError) : SuggestionError
}
