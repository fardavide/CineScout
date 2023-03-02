package cinescout.database.testdata

import cinescout.database.SuggestedMovie
import cinescout.database.model.DatabaseSuggestionSource

object DatabaseSuggestedMovieTestData {

    val Inception = SuggestedMovie(
        tmdbId = DatabaseMovieTestData.Inception.tmdbId,
        affinity = 90.0,
        source = DatabaseSuggestionSource.FromRated(9)
    )

    val TheWolfOfWallStreet = SuggestedMovie(
        tmdbId = DatabaseMovieTestData.TheWolfOfWallStreet.tmdbId,
        affinity = 80.0,
        source = DatabaseSuggestionSource.FromRated(8)
    )

    val War = SuggestedMovie(
        tmdbId = DatabaseMovieTestData.War.tmdbId,
        affinity = 70.0,
        source = DatabaseSuggestionSource.FromRated(7)
    )
}
