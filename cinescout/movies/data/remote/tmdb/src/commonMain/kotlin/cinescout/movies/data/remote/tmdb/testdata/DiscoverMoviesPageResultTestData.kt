package cinescout.movies.data.remote.tmdb.testdata

import cinescout.movies.data.remote.sample.TmdbMovieSample
import cinescout.movies.data.remote.tmdb.model.DiscoverMovies

object DiscoverMoviesPageResultTestData {

    val Inception = DiscoverMovies.Response.PageResult(
        backdropPath = TmdbMovieSample.Inception.backdropPath,
        id = TmdbMovieSample.Inception.id,
        overview = TmdbMovieSample.Inception.overview,
        posterPath = TmdbMovieSample.Inception.posterPath,
        releaseDate = TmdbMovieSample.Inception.releaseDate,
        title = TmdbMovieSample.Inception.title,
        voteAverage = TmdbMovieSample.Inception.voteAverage,
        voteCount = TmdbMovieSample.Inception.voteCount
    )

    val TheWolfOfWallStreet = DiscoverMovies.Response.PageResult(
        backdropPath = TmdbMovieSample.TheWolfOfWallStreet.backdropPath,
        id = TmdbMovieSample.TheWolfOfWallStreet.id,
        overview = TmdbMovieSample.TheWolfOfWallStreet.overview,
        posterPath = TmdbMovieSample.TheWolfOfWallStreet.posterPath,
        releaseDate = TmdbMovieSample.TheWolfOfWallStreet.releaseDate,
        title = TmdbMovieSample.TheWolfOfWallStreet.title,
        voteAverage = TmdbMovieSample.TheWolfOfWallStreet.voteAverage,
        voteCount = TmdbMovieSample.TheWolfOfWallStreet.voteCount
    )
}
