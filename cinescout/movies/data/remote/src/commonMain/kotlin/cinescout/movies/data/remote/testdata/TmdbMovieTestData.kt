package cinescout.movies.data.remote.testdata

import cinescout.movies.data.remote.model.TmdbMovie
import cinescout.movies.domain.testdata.MovieTestData

object TmdbMovieTestData {

    val Inception = TmdbMovie(
        id = MovieTestData.Inception.tmdbId,
        releaseDate = MovieTestData.Inception.releaseDate,
        title = MovieTestData.Inception.title
    )
    val TheWolfOfWallStreet = TmdbMovie(
        id = MovieTestData.TheWolfOfWallStreet.tmdbId,
        releaseDate = MovieTestData.TheWolfOfWallStreet.releaseDate,
        title = MovieTestData.TheWolfOfWallStreet.title
    )
}
