package cinescout.database.model

sealed interface DatabaseSuggestionSource {

    data class FromLiked(val title: String) : DatabaseSuggestionSource
    data class FromRated(val title: String, val rating: Int) : DatabaseSuggestionSource
    data class FromWatchlist(val title: String) : DatabaseSuggestionSource
    data class PersonalSuggestions(val provider: Provider) : DatabaseSuggestionSource {
        enum class Provider {
            Tmdb, Trakt
        }
    }
    object Popular : DatabaseSuggestionSource
    object Suggested : DatabaseSuggestionSource
    object Trending : DatabaseSuggestionSource
    object Upcoming : DatabaseSuggestionSource
}
