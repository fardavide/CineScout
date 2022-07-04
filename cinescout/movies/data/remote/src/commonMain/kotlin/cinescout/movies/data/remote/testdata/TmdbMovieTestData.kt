package cinescout.movies.data.remote.testdata

import cinescout.movies.data.remote.model.TmdbMovie
import cinescout.movies.domain.testdata.TmdbMovieIdTestData

object TmdbMovieTestData {

    val Inception = TmdbMovie(TmdbMovieIdTestData.Inception, title = "Inception")
    val TheWolfOfWallStreet = TmdbMovie(TmdbMovieIdTestData.TheWolfOfWallStreet, title = "The Wolf of Wall Street")
}
