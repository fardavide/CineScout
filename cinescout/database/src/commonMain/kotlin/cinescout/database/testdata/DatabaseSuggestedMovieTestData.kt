package cinescout.database.testdata

import cinescout.database.SuggestedMovie

object DatabaseSuggestedMovieTestData {

    val Inception = SuggestedMovie(
        tmdbId = DatabaseMovieTestData.Inception.tmdbId,
        affinity = 95.0
    )

    val TheWolfOfWallStreet = SuggestedMovie(
        tmdbId = DatabaseMovieTestData.TheWolfOfWallStreet.tmdbId,
        affinity = 90.0
    )

    val War = SuggestedMovie(
        tmdbId = DatabaseMovieTestData.War.tmdbId,
        affinity = 80.0
    )
}
