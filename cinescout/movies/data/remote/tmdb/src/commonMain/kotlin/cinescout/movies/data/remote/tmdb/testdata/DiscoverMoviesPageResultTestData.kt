package cinescout.movies.data.remote.tmdb.testdata

import cinescout.movies.data.remote.testdata.TmdbMovieTestData
import cinescout.movies.data.remote.tmdb.model.DiscoverMovies

object DiscoverMoviesPageResultTestData {

    val Inception = DiscoverMovies.Response.PageResult(
        backdropPath = TmdbMovieTestData.Inception.backdropPath,
        id = TmdbMovieTestData.Inception.id,
        posterPath = TmdbMovieTestData.Inception.posterPath,
        releaseDate = TmdbMovieTestData.Inception.releaseDate,
        title = TmdbMovieTestData.Inception.title,
        voteAverage = TmdbMovieTestData.Inception.voteAverage,
        voteCount = TmdbMovieTestData.Inception.voteCount
    )

    val TheWolfOfWallStreet = DiscoverMovies.Response.PageResult(
        backdropPath = TmdbMovieTestData.TheWolfOfWallStreet.backdropPath,
        id = TmdbMovieTestData.TheWolfOfWallStreet.id,
        posterPath = TmdbMovieTestData.TheWolfOfWallStreet.posterPath,
        releaseDate = TmdbMovieTestData.TheWolfOfWallStreet.releaseDate,
        title = TmdbMovieTestData.TheWolfOfWallStreet.title,
        voteAverage = TmdbMovieTestData.TheWolfOfWallStreet.voteAverage,
        voteCount = TmdbMovieTestData.TheWolfOfWallStreet.voteCount
    )
}
