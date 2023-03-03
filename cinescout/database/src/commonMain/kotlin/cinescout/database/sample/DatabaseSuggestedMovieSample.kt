package cinescout.database.sample

import cinescout.database.model.DatabaseSuggestedMovie
import cinescout.database.model.DatabaseSuggestionSource

object DatabaseSuggestedMovieSample {

    val Inception = DatabaseSuggestedMovie(
        tmdbId = DatabaseMovieSample.Inception.tmdbId,
        affinity = 90.0,
        source = DatabaseSuggestionSource.FromRated(9)
    )

    val TheWolfOfWallStreet = DatabaseSuggestedMovie(
        tmdbId = DatabaseMovieSample.TheWolfOfWallStreet.tmdbId,
        affinity = 80.0,
        source = DatabaseSuggestionSource.FromRated(8)
    )

    val War = DatabaseSuggestedMovie(
        tmdbId = DatabaseMovieSample.War.tmdbId,
        affinity = 70.0,
        source = DatabaseSuggestionSource.FromRated(7)
    )
}
