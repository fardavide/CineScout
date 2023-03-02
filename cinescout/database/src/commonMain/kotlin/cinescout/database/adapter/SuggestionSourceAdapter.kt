package cinescout.database.adapter

import app.cash.sqldelight.ColumnAdapter
import cinescout.database.model.DatabaseSuggestionSource

val SuggestionSourceAdapter = object : ColumnAdapter<DatabaseSuggestionSource, String> {

    override fun decode(databaseValue: String) =
        when (val type = databaseValue.substringBefore(TypeValueSeparator)) {
            "FromLiked" -> DatabaseSuggestionSource.FromLiked
            "FromRated" -> DatabaseSuggestionSource.FromRated(databaseValue.substringAfter(TypeValueSeparator).toInt())
            "FromWatchlist" -> DatabaseSuggestionSource.FromWatchlist
            "PersonalSuggestions" -> DatabaseSuggestionSource.PersonalSuggestions
            "Popular" -> DatabaseSuggestionSource.Popular
            "Suggested" -> DatabaseSuggestionSource.Suggested
            "Trending" -> DatabaseSuggestionSource.Trending
            "Upcoming" -> DatabaseSuggestionSource.Upcoming
            else -> error("Unknown type: $type")
        }

    override fun encode(value: DatabaseSuggestionSource) = when (value) {
        DatabaseSuggestionSource.FromLiked -> "FromLiked"
        is DatabaseSuggestionSource.FromRated -> "FromRated$TypeValueSeparator${value.rating}"
        DatabaseSuggestionSource.FromWatchlist -> "FromWatchlist"
        DatabaseSuggestionSource.PersonalSuggestions -> "PersonalSuggestions"
        DatabaseSuggestionSource.Popular -> "Popular"
        DatabaseSuggestionSource.Suggested -> "Suggested"
        DatabaseSuggestionSource.Trending -> "Trending"
        DatabaseSuggestionSource.Upcoming -> "Upcoming"
    }
}

private const val TypeValueSeparator = "_"
