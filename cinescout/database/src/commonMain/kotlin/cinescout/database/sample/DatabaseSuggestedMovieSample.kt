package cinescout.database.sample

import cinescout.database.SuggestedMovie
import cinescout.database.model.DatabaseSuggestionSource

object DatabaseSuggestedMovieSample {

    val Inception = SuggestedMovie(
        tmdbId = DatabaseMovieSample.Inception.tmdbId,
        affinity = 90.0,
        source = DatabaseSuggestionSource.FromRated(9)
    )

    val TheWolfOfWallStreet = SuggestedMovie(
        tmdbId = DatabaseMovieSample.TheWolfOfWallStreet.tmdbId,
        affinity = 80.0,
        source = DatabaseSuggestionSource.FromRated(8)
    )

    val War = SuggestedMovie(
        tmdbId = DatabaseMovieSample.War.tmdbId,
        affinity = 70.0,
        source = DatabaseSuggestionSource.FromRated(7)
    )
}
