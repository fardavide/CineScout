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
        runtime = DatabaseMovieSample.Inception.runtime,
        tagline = DatabaseMovieSample.Inception.tagline,
        title = DatabaseMovieSample.Inception.title,
        tmdbId = DatabaseMovieSample.Inception.tmdbId,
        traktId = DatabaseMovieSample.Inception.traktId
    )

    val TheWolfOfWallStreet = DatabaseMovieWithPersonalRating(
        overview = DatabaseMovieSample.TheWolfOfWallStreet.overview,
        personalRating = 8.0,
        ratingAverage = DatabaseMovieSample.TheWolfOfWallStreet.ratingAverage,
        ratingCount = DatabaseMovieSample.TheWolfOfWallStreet.ratingCount,
        releaseDate = DatabaseMovieSample.TheWolfOfWallStreet.releaseDate,
        runtime = DatabaseMovieSample.TheWolfOfWallStreet.runtime,
        tagline = DatabaseMovieSample.TheWolfOfWallStreet.tagline,
        title = DatabaseMovieSample.TheWolfOfWallStreet.title,
        tmdbId = DatabaseMovieSample.TheWolfOfWallStreet.tmdbId,
        traktId = DatabaseMovieSample.TheWolfOfWallStreet.traktId
    )

    val War = DatabaseMovieWithPersonalRating(
        overview = DatabaseMovieSample.War.overview,
        personalRating = 6.0,
        ratingAverage = DatabaseMovieSample.War.ratingAverage,
        ratingCount = DatabaseMovieSample.War.ratingCount,
        releaseDate = DatabaseMovieSample.War.releaseDate,
        runtime = DatabaseMovieSample.War.runtime,
        tagline = DatabaseMovieSample.War.tagline,
        title = DatabaseMovieSample.War.title,
        tmdbId = DatabaseMovieSample.War.tmdbId,
        traktId = DatabaseMovieSample.War.traktId
    )
}
