package cinescout.movies.domain.testdata

import cinescout.movies.domain.model.Movie

object MovieTestData {

    val Inception = Movie(
        title = "Inception",
        tmdbId = TmdbMovieIdTestData.Inception
    )
    val TheWolfOfWallStreet = Movie(
        title = "The Wolf of Wall Street",
        tmdbId = TmdbMovieIdTestData.TheWolfOfWallStreet
    )
}
