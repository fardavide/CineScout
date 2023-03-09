package cinescout.database.adapter

import app.cash.sqldelight.ColumnAdapter
import cinescout.database.model.DatabaseSuggestionSource

val SuggestionSourceAdapter = object : ColumnAdapter<DatabaseSuggestionSource, String> {

    override fun decode(databaseValue: String) =
        when (val type = databaseValue.substringBefore(ValueSeparator)) {
            "FromLiked" -> DatabaseSuggestionSource.FromLiked(
                title = databaseValue.substringAfter(ValueSeparator)
            )
            "FromRated" -> DatabaseSuggestionSource.FromRated(
                title = databaseValue.substringAfter(ValueSeparator).substringBeforeLast(ValueSeparator),
                rating = databaseValue.substringAfterLast(ValueSeparator).toInt()
            )
            "FromWatchlist" -> DatabaseSuggestionSource.FromWatchlist(
                title = databaseValue.substringAfter(ValueSeparator)
            )
            "PersonalSuggestions" -> DatabaseSuggestionSource.PersonalSuggestions
            "Popular" -> DatabaseSuggestionSource.Popular
            "Suggested" -> DatabaseSuggestionSource.Suggested
            "Trending" -> DatabaseSuggestionSource.Trending
            "Upcoming" -> DatabaseSuggestionSource.Upcoming
            else -> error("Unknown type: $type")
        }

    override fun encode(value: DatabaseSuggestionSource) = when (value) {
        is DatabaseSuggestionSource.FromLiked -> "FromLiked$ValueSeparator${value.title}"
        is DatabaseSuggestionSource.FromRated -> "FromRated$ValueSeparator${value.title}$ValueSeparator${value.rating}"
        is DatabaseSuggestionSource.FromWatchlist -> "FromWatchlist$ValueSeparator${value.title}"
        is DatabaseSuggestionSource.PersonalSuggestions -> "PersonalSuggestions"
        DatabaseSuggestionSource.Popular -> "Popular"
        DatabaseSuggestionSource.Suggested -> "Suggested"
        DatabaseSuggestionSource.Trending -> "Trending"
        DatabaseSuggestionSource.Upcoming -> "Upcoming"
    }
}

private const val ValueSeparator = "_"
