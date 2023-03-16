package cinescout.database.testdata

import cinescout.database.model.DatabaseMovieWithPersonalRating
import cinescout.database.sample.DatabaseMovieSample

object DatabaseMovieWithRatingSample {

    val Inception = DatabaseMovieWithPersonalRating(
        overview = DatabaseMovieSample.Inception.overview,
        personalRating = 9.0,
        ratingAverage = DatabaseMovieSample.Inception.ratingAverage,
        ratingCount = DatabaseMovieSample.Inception.ratingCount,
        releaseDate = DatabaseMovieSample.Inception.releaseDate,
        title = DatabaseMovieSample.Inception.title,
        tmdbId = DatabaseMovieSample.Inception.tmdbId
    )

    val TheWolfOfWallStreet = DatabaseMovieWithPersonalRating(
        overview = DatabaseMovieSample.TheWolfOfWallStreet.overview,
        personalRating = 8.0,
        ratingAverage = DatabaseMovieSample.TheWolfOfWallStreet.ratingAverage,
        ratingCount = DatabaseMovieSample.TheWolfOfWallStreet.ratingCount,
        releaseDate = DatabaseMovieSample.TheWolfOfWallStreet.releaseDate,
        title = DatabaseMovieSample.TheWolfOfWallStreet.title,
        tmdbId = DatabaseMovieSample.TheWolfOfWallStreet.tmdbId
    )

    val War = DatabaseMovieWithPersonalRating(
        overview = DatabaseMovieSample.War.overview,
        personalRating = 6.0,
        ratingAverage = DatabaseMovieSample.War.ratingAverage,
        ratingCount = DatabaseMovieSample.War.ratingCount,
        releaseDate = DatabaseMovieSample.War.releaseDate,
        title = DatabaseMovieSample.War.title,
        tmdbId = DatabaseMovieSample.War.tmdbId
    )
}