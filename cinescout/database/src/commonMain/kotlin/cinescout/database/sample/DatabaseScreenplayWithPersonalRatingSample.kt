package cinescout.database.sample

import cinescout.database.model.DatabaseScreenplayWithPersonalRating

object DatabaseScreenplayWithPersonalRatingSample {

    val BreakingBad = DatabaseScreenplayWithPersonalRating(
        firstAirDate = DatabaseTvShowSample.BreakingBad.firstAirDate,
        movieTmdbId = null,
        overview = DatabaseTvShowSample.BreakingBad.overview,
        personalRating = 9,
        ratingAverage = DatabaseTvShowSample.BreakingBad.ratingAverage,
        ratingCount = DatabaseTvShowSample.BreakingBad.ratingCount,
        releaseDate = null,
        title = DatabaseTvShowSample.BreakingBad.title,
        tvShowTmdbId = DatabaseTvShowSample.BreakingBad.tmdbId
    )

    val Grimm = DatabaseScreenplayWithPersonalRating(
        firstAirDate = DatabaseTvShowSample.Grimm.firstAirDate,
        movieTmdbId = null,
        overview = DatabaseTvShowSample.Grimm.overview,
        personalRating = 8,
        ratingAverage = DatabaseTvShowSample.Grimm.ratingAverage,
        ratingCount = DatabaseTvShowSample.Grimm.ratingCount,
        releaseDate = null,
        title = DatabaseTvShowSample.Grimm.title,
        tvShowTmdbId = DatabaseTvShowSample.Grimm.tmdbId
    )

    val Inception = DatabaseScreenplayWithPersonalRating(
        firstAirDate = null,
        movieTmdbId = DatabaseMovieSample.Inception.tmdbId,
        overview = DatabaseMovieSample.Inception.overview,
        personalRating = 9,
        ratingAverage = DatabaseMovieSample.Inception.ratingAverage,
        ratingCount = DatabaseMovieSample.Inception.ratingCount,
        releaseDate = DatabaseMovieSample.Inception.releaseDate,
        title = DatabaseMovieSample.Inception.title,
        tvShowTmdbId = null
    )

    val TheWolfOfWallStreet = DatabaseScreenplayWithPersonalRating(
        firstAirDate = null,
        movieTmdbId = DatabaseMovieSample.TheWolfOfWallStreet.tmdbId,
        overview = DatabaseMovieSample.TheWolfOfWallStreet.overview,
        personalRating = 8,
        ratingAverage = DatabaseMovieSample.TheWolfOfWallStreet.ratingAverage,
        ratingCount = DatabaseMovieSample.TheWolfOfWallStreet.ratingCount,
        releaseDate = DatabaseMovieSample.TheWolfOfWallStreet.releaseDate,
        title = DatabaseMovieSample.TheWolfOfWallStreet.title,
        tvShowTmdbId = null
    )

    val War = DatabaseScreenplayWithPersonalRating(
        firstAirDate = null,
        movieTmdbId = DatabaseMovieSample.War.tmdbId,
        overview = DatabaseMovieSample.War.overview,
        personalRating = 7,
        ratingAverage = DatabaseMovieSample.War.ratingAverage,
        ratingCount = DatabaseMovieSample.War.ratingCount,
        releaseDate = DatabaseMovieSample.War.releaseDate,
        title = DatabaseMovieSample.War.title,
        tvShowTmdbId = null
    )
}
