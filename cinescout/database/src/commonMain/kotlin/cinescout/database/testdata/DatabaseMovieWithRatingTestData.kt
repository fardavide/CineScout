package cinescout.database.testdata

import cinescout.database.model.DatabaseMovieWithPersonalRating

object DatabaseMovieWithRatingTestData {

    val Inception = DatabaseMovieWithPersonalRating(
        backdropPath = DatabaseMovieTestData.Inception.backdropPath,
        overview = DatabaseMovieTestData.Inception.overview,
        personalRating = 9.0,
        posterPath = DatabaseMovieTestData.Inception.posterPath,
        ratingAverage = DatabaseMovieTestData.Inception.ratingAverage,
        ratingCount = DatabaseMovieTestData.Inception.ratingCount,
        releaseDate = DatabaseMovieTestData.Inception.releaseDate,
        title = DatabaseMovieTestData.Inception.title,
        tmdbId = DatabaseMovieTestData.Inception.tmdbId
    )

    val TheWolfOfWallStreet = DatabaseMovieWithPersonalRating(
        backdropPath = DatabaseMovieTestData.TheWolfOfWallStreet.backdropPath,
        overview = DatabaseMovieTestData.TheWolfOfWallStreet.overview,
        personalRating = 8.0,
        posterPath = DatabaseMovieTestData.TheWolfOfWallStreet.posterPath,
        ratingAverage = DatabaseMovieTestData.TheWolfOfWallStreet.ratingAverage,
        ratingCount = DatabaseMovieTestData.TheWolfOfWallStreet.ratingCount,
        releaseDate = DatabaseMovieTestData.TheWolfOfWallStreet.releaseDate,
        title = DatabaseMovieTestData.TheWolfOfWallStreet.title,
        tmdbId = DatabaseMovieTestData.TheWolfOfWallStreet.tmdbId
    )

    val War = DatabaseMovieWithPersonalRating(
        backdropPath = DatabaseMovieTestData.War.backdropPath,
        overview = DatabaseMovieTestData.War.overview,
        personalRating = 6.0,
        posterPath = DatabaseMovieTestData.War.posterPath,
        ratingAverage = DatabaseMovieTestData.War.ratingAverage,
        ratingCount = DatabaseMovieTestData.War.ratingCount,
        releaseDate = DatabaseMovieTestData.War.releaseDate,
        title = DatabaseMovieTestData.War.title,
        tmdbId = DatabaseMovieTestData.War.tmdbId
    )
}
