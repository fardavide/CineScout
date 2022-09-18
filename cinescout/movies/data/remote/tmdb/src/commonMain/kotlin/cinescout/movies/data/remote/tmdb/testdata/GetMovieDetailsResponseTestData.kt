package cinescout.movies.data.remote.tmdb.testdata

import cinescout.movies.data.remote.testdata.TmdbMovieTestData
import cinescout.movies.data.remote.tmdb.model.GetMovieDetails
import cinescout.movies.domain.testdata.MovieWithDetailsTestData

object GetMovieDetailsResponseTestData {

    val Inception = GetMovieDetails.Response(
        backdropPath = TmdbMovieTestData.Inception.backdropPath,
        genres = MovieWithDetailsTestData.Inception.genres.map { genre ->
            GetMovieDetails.Response.Genre(genre.id.value, genre.name)
        }.toList(),
        id = TmdbMovieTestData.Inception.id,
        overview = TmdbMovieTestData.Inception.overview,
        posterPath = TmdbMovieTestData.Inception.posterPath,
        releaseDate = TmdbMovieTestData.Inception.releaseDate,
        title = TmdbMovieTestData.Inception.title,
        voteAverage = TmdbMovieTestData.Inception.voteAverage,
        voteCount = TmdbMovieTestData.Inception.voteCount
    )

    val TheWolfOfWallStreet = GetMovieDetails.Response(
        backdropPath = TmdbMovieTestData.TheWolfOfWallStreet.backdropPath,
        genres = MovieWithDetailsTestData.TheWolfOfWallStreet.genres.map { genre ->
            GetMovieDetails.Response.Genre(genre.id.value, genre.name)
        }.toList(),
        id = TmdbMovieTestData.TheWolfOfWallStreet.id,
        overview = TmdbMovieTestData.TheWolfOfWallStreet.overview,
        posterPath = TmdbMovieTestData.TheWolfOfWallStreet.posterPath,
        releaseDate = TmdbMovieTestData.TheWolfOfWallStreet.releaseDate,
        title = TmdbMovieTestData.TheWolfOfWallStreet.title,
        voteAverage = TmdbMovieTestData.TheWolfOfWallStreet.voteAverage,
        voteCount = TmdbMovieTestData.TheWolfOfWallStreet.voteCount
    )
}
