package cinescout.movies.data.remote.tmdb.testdata

import cinescout.movies.data.remote.sample.TmdbMovieSample
import cinescout.movies.data.remote.tmdb.model.GetMovieDetails
import cinescout.movies.domain.sample.MovieWithDetailsSample

object GetMovieDetailsResponseTestData {

    val Inception = GetMovieDetails.Response(
        backdropPath = TmdbMovieSample.Inception.backdropPath,
        genres = MovieWithDetailsSample.Inception.genres.map { genre ->
            GetMovieDetails.Response.Genre(genre.id.value, genre.name)
        }.toList(),
        id = TmdbMovieSample.Inception.id,
        overview = TmdbMovieSample.Inception.overview,
        posterPath = TmdbMovieSample.Inception.posterPath,
        releaseDate = TmdbMovieSample.Inception.releaseDate,
        title = TmdbMovieSample.Inception.title,
        voteAverage = TmdbMovieSample.Inception.voteAverage,
        voteCount = TmdbMovieSample.Inception.voteCount
    )

    val TheWolfOfWallStreet = GetMovieDetails.Response(
        backdropPath = TmdbMovieSample.TheWolfOfWallStreet.backdropPath,
        genres = MovieWithDetailsSample.TheWolfOfWallStreet.genres.map { genre ->
            GetMovieDetails.Response.Genre(genre.id.value, genre.name)
        }.toList(),
        id = TmdbMovieSample.TheWolfOfWallStreet.id,
        overview = TmdbMovieSample.TheWolfOfWallStreet.overview,
        posterPath = TmdbMovieSample.TheWolfOfWallStreet.posterPath,
        releaseDate = TmdbMovieSample.TheWolfOfWallStreet.releaseDate,
        title = TmdbMovieSample.TheWolfOfWallStreet.title,
        voteAverage = TmdbMovieSample.TheWolfOfWallStreet.voteAverage,
        voteCount = TmdbMovieSample.TheWolfOfWallStreet.voteCount
    )
}
