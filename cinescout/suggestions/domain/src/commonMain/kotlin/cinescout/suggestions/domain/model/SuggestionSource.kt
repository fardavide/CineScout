package cinescout.suggestions.domain.model

import cinescout.screenplay.domain.model.Rating

sealed interface SuggestionSource {

    data class FromLiked(val title: String) : SuggestionSource
    data class FromRated(val title: String, val rating: Rating) : SuggestionSource
    data class FromWatchlist(val title: String) : SuggestionSource
    data class PersonalSuggestions(val provider: Provider) : SuggestionSource {

        enum class Provider(val value: String) {
            Tmdb("Tmdb"), Trakt("Trakt")
        }
    }
    object Popular : SuggestionSource
    object Suggested : SuggestionSource
    object Trending : SuggestionSource
    object Upcoming : SuggestionSource
}
