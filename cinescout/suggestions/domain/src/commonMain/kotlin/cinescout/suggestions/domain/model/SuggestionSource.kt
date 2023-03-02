package cinescout.suggestions.domain.model

import cinescout.common.model.Rating

sealed interface SuggestionSource {

    object FromLiked : SuggestionSource
    data class FromRated(val rating: Rating) : SuggestionSource
    object FromWatchlist : SuggestionSource
    object PersonalSuggestions : SuggestionSource
    object Popular : SuggestionSource
    object Suggested : SuggestionSource
    object Trending : SuggestionSource
    object Upcoming : SuggestionSource
}
