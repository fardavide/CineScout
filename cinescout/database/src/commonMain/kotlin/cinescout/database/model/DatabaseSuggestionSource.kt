package cinescout.database.model

sealed interface DatabaseSuggestionSource {

    data class FromLiked(val title: String) : DatabaseSuggestionSource
    data class FromRated(val title: String, val rating: Int) : DatabaseSuggestionSource
    data class FromWatchlist(val title: String) : DatabaseSuggestionSource
    object PersonalSuggestions : DatabaseSuggestionSource
    object Popular : DatabaseSuggestionSource
    object Suggested : DatabaseSuggestionSource
    object Trending : DatabaseSuggestionSource
    object Upcoming : DatabaseSuggestionSource
}
