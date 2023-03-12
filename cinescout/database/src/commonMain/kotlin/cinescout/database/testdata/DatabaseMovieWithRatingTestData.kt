package cinescout.database.testdata

import cinescout.database.model.DatabaseMovieWithPersonalRating
import cinescout.database.sample.DatabaseMovieSample

object DatabaseMovieWithRatingTestData {

    val Inception = DatabaseMovieWithPersonalRating(
        backdropPath = DatabaseMovieSample.Inception.backdropPath,
        overview = DatabaseMovieSample.Inception.overview,
        personalRating = 9.0,
        posterPath = DatabaseMovieSample.Inception.posterPath,
        ratingAverage = DatabaseMovieSample.Inception.ratingAverage,
        ratingCount = DatabaseMovieSample.Inception.ratingCount,
        releaseDate = DatabaseMovieSample.Inception.releaseDate,
        title = DatabaseMovieSample.Inception.title,
        tmdbId = DatabaseMovieSample.Inception.tmdbId
    )

    val TheWolfOfWallStreet = DatabaseMovieWithPersonalRating(
        backdropPath = DatabaseMovieSample.TheWolfOfWallStreet.backdropPath,
        overview = DatabaseMovieSample.TheWolfOfWallStreet.overview,
        personalRating = 8.0,
        posterPath = DatabaseMovieSample.TheWolfOfWallStreet.posterPath,
        ratingAverage = DatabaseMovieSample.TheWolfOfWallStreet.ratingAverage,
        ratingCount = DatabaseMovieSample.TheWolfOfWallStreet.ratingCount,
        releaseDate = DatabaseMovieSample.TheWolfOfWallStreet.releaseDate,
        title = DatabaseMovieSample.TheWolfOfWallStreet.title,
        tmdbId = DatabaseMovieSample.TheWolfOfWallStreet.tmdbId
    )

    val War = DatabaseMovieWithPersonalRating(
        backdropPath = DatabaseMovieSample.War.backdropPath,
        overview = DatabaseMovieSample.War.overview,
        personalRating = 6.0,
        posterPath = DatabaseMovieSample.War.posterPath,
        ratingAverage = DatabaseMovieSample.War.ratingAverage,
        ratingCount = DatabaseMovieSample.War.ratingCount,
        releaseDate = DatabaseMovieSample.War.releaseDate,
        title = DatabaseMovieSample.War.title,
        tmdbId = DatabaseMovieSample.War.tmdbId
    )
}