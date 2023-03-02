package cinescout.database.model

sealed interface DatabaseSuggestionSource {

    object FromLiked : DatabaseSuggestionSource
    data class FromRated(val rating: Int) : DatabaseSuggestionSource
    object FromWatchlist : DatabaseSuggestionSource
    object PersonalSuggestions : DatabaseSuggestionSource
    object Popular : DatabaseSuggestionSource
    object Suggested : DatabaseSuggestionSource
    object Trending : DatabaseSuggestionSource
    object Upcoming : DatabaseSuggestionSource
}
