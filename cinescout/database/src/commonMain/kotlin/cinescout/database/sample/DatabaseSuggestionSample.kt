package cinescout.database.sample

import cinescout.database.model.DatabaseSuggestion
import cinescout.database.model.DatabaseSuggestionSource

object DatabaseSuggestionSample {

    val BreakingBad = DatabaseSuggestion(
        traktId = DatabaseTraktScreenplayIdSample.BreakingBad,
        tmdbId = DatabaseTmdbScreenplayIdSample.BreakingBad,
        affinity = 90.0,
        source = DatabaseSuggestionSource.FromRated(title = "", 9)
    )

    val Dexter = DatabaseSuggestion(
        traktId = DatabaseTraktScreenplayIdSample.Dexter,
        tmdbId = DatabaseTmdbScreenplayIdSample.Dexter,
        affinity = 80.0,
        source = DatabaseSuggestionSource.FromRated(title = "", 8)
    )

    val Grimm = DatabaseSuggestion(
        traktId = DatabaseTraktScreenplayIdSample.Grimm,
        tmdbId = DatabaseTmdbScreenplayIdSample.Grimm,
        affinity = 70.0,
        source = DatabaseSuggestionSource.FromRated(title = "", 7)
    )

    val Inception = DatabaseSuggestion(
        traktId = DatabaseTraktScreenplayIdSample.Inception,
        tmdbId = DatabaseTmdbScreenplayIdSample.Inception,
        affinity = 90.0,
        source = DatabaseSuggestionSource.FromRated(title = "", 9)
    )

    val TheWolfOfWallStreet = DatabaseSuggestion(
        traktId = DatabaseTraktScreenplayIdSample.TheWolfOfWallStreet,
        tmdbId = DatabaseTmdbScreenplayIdSample.TheWolfOfWallStreet,
        affinity = 80.0,
        source = DatabaseSuggestionSource.FromRated(title = "", 8)
    )

    val War = DatabaseSuggestion(
        traktId = DatabaseTraktScreenplayIdSample.War,
        tmdbId = DatabaseTmdbScreenplayIdSample.War,
        affinity = 70.0,
        source = DatabaseSuggestionSource.FromRated(title = "", 7)
    )
}
