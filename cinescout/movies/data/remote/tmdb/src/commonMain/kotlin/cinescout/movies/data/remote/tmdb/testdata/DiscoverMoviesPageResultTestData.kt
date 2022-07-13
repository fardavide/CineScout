package cinescout.movies.data.remote.tmdb.testdata

import cinescout.movies.data.remote.testdata.TmdbMovieTestData
import cinescout.movies.data.remote.tmdb.model.DiscoverMovies

object DiscoverMoviesPageResultTestData {

    val Inception = DiscoverMovies.Response.PageResult(
        id = TmdbMovieTestData.Inception.id,
        releaseDate = TmdbMovieTestData.Inception.releaseDate,
        title = TmdbMovieTestData.Inception.title
    )

    val TheWolfOfWallStreet = DiscoverMovies.Response.PageResult(
        id = TmdbMovieTestData.TheWolfOfWallStreet.id,
        releaseDate = TmdbMovieTestData.TheWolfOfWallStreet.releaseDate,
        title = TmdbMovieTestData.TheWolfOfWallStreet.title
    )
}
