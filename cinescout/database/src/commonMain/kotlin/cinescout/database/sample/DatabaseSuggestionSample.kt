package cinescout.database.sample

import cinescout.database.model.DatabaseSuggestion
import cinescout.database.model.DatabaseSuggestionSource

object DatabaseSuggestionSample {

    val BreakingBad = DatabaseSuggestion(
        tmdbId = DatabaseTmdbScreenplayIdSample.BreakingBad,
        affinity = 90.0,
        source = DatabaseSuggestionSource.FromRated(title = "", 9)
    )

    val Dexter = DatabaseSuggestion(
        tmdbId = DatabaseTmdbScreenplayIdSample.Dexter,
        affinity = 80.0,
        source = DatabaseSuggestionSource.FromRated(title = "", 8)
    )

    val Grimm = DatabaseSuggestion(
        tmdbId = DatabaseTmdbScreenplayIdSample.Grimm,
        affinity = 70.0,
        source = DatabaseSuggestionSource.FromRated(title = "", 7)
    )

    val Inception = DatabaseSuggestion(
        tmdbId = DatabaseTmdbScreenplayIdSample.Inception,
        affinity = 90.0,
        source = DatabaseSuggestionSource.FromRated(title = "", 9)
    )

    val TheWolfOfWallStreet = DatabaseSuggestion(
        tmdbId = DatabaseTmdbScreenplayIdSample.TheWolfOfWallStreet,
        affinity = 80.0,
        source = DatabaseSuggestionSource.FromRated(title = "", 8)
    )

    val War = DatabaseSuggestion(
        tmdbId = DatabaseTmdbScreenplayIdSample.War,
        affinity = 70.0,
        source = DatabaseSuggestionSource.FromRated(title = "", 7)
    )
}
