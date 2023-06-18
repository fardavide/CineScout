package cinescout.database.sample

import cinescout.database.model.DatabaseScreenplayWithPersonalRating

object DatabaseScreenplayWithPersonalRatingSample {

    val BreakingBad = DatabaseScreenplayWithPersonalRating(
        airedEpisodes = DatabaseTvShowSample.BreakingBad.airedEpisodes,
        firstAirDate = DatabaseTvShowSample.BreakingBad.firstAirDate,
        movieTmdbId = null,
        movieTraktId = null,
        overview = DatabaseTvShowSample.BreakingBad.overview,
        personalRating = 9,
        ratingAverage = DatabaseTvShowSample.BreakingBad.ratingAverage,
        ratingCount = DatabaseTvShowSample.BreakingBad.ratingCount,
        releaseDate = null,
        runtime = DatabaseTvShowSample.BreakingBad.runtime,
        status = DatabaseTvShowSample.BreakingBad.status,
        tagline = null,
        title = DatabaseTvShowSample.BreakingBad.title,
        tvShowTmdbId = DatabaseTvShowSample.BreakingBad.tmdbId,
        tvShowTraktId = DatabaseTvShowSample.BreakingBad.traktId
    )

    val Grimm = DatabaseScreenplayWithPersonalRating(
        airedEpisodes = DatabaseTvShowSample.Grimm.airedEpisodes,
        firstAirDate = DatabaseTvShowSample.Grimm.firstAirDate,
        movieTmdbId = null,
        movieTraktId = null,
        overview = DatabaseTvShowSample.Grimm.overview,
        personalRating = 8,
        ratingAverage = DatabaseTvShowSample.Grimm.ratingAverage,
        ratingCount = DatabaseTvShowSample.Grimm.ratingCount,
        releaseDate = null,
        runtime = DatabaseTvShowSample.Grimm.runtime,
        status = DatabaseTvShowSample.Grimm.status,
        tagline = null,
        title = DatabaseTvShowSample.Grimm.title,
        tvShowTmdbId = DatabaseTvShowSample.Grimm.tmdbId,
        tvShowTraktId = DatabaseTvShowSample.Grimm.traktId
    )

    val Inception = DatabaseScreenplayWithPersonalRating(
        airedEpisodes = null,
        firstAirDate = null,
        movieTmdbId = DatabaseMovieSample.Inception.tmdbId,
        movieTraktId = DatabaseMovieSample.Inception.traktId,
        overview = DatabaseMovieSample.Inception.overview,
        personalRating = 9,
        ratingAverage = DatabaseMovieSample.Inception.ratingAverage,
        ratingCount = DatabaseMovieSample.Inception.ratingCount,
        releaseDate = DatabaseMovieSample.Inception.releaseDate,
        runtime = DatabaseMovieSample.Inception.runtime,
        status = null,
        tagline = DatabaseMovieSample.Inception.tagline,
        title = DatabaseMovieSample.Inception.title,
        tvShowTmdbId = null,
        tvShowTraktId = null
    )

    val TheWolfOfWallStreet = DatabaseScreenplayWithPersonalRating(
        airedEpisodes = null,
        firstAirDate = null,
        movieTmdbId = DatabaseMovieSample.TheWolfOfWallStreet.tmdbId,
        movieTraktId = DatabaseMovieSample.TheWolfOfWallStreet.traktId,
        overview = DatabaseMovieSample.TheWolfOfWallStreet.overview,
        personalRating = 8,
        ratingAverage = DatabaseMovieSample.TheWolfOfWallStreet.ratingAverage,
        ratingCount = DatabaseMovieSample.TheWolfOfWallStreet.ratingCount,
        releaseDate = DatabaseMovieSample.TheWolfOfWallStreet.releaseDate,
        runtime = DatabaseMovieSample.TheWolfOfWallStreet.runtime,
        status = null,
        tagline = DatabaseMovieSample.TheWolfOfWallStreet.tagline,
        title = DatabaseMovieSample.TheWolfOfWallStreet.title,
        tvShowTmdbId = null,
        tvShowTraktId = null
    )

    val War = DatabaseScreenplayWithPersonalRating(
        airedEpisodes = null,
        firstAirDate = null,
        movieTmdbId = DatabaseMovieSample.War.tmdbId,
        movieTraktId = DatabaseMovieSample.War.traktId,
        overview = DatabaseMovieSample.War.overview,
        personalRating = 7,
        ratingAverage = DatabaseMovieSample.War.ratingAverage,
        ratingCount = DatabaseMovieSample.War.ratingCount,
        releaseDate = DatabaseMovieSample.War.releaseDate,
        runtime = DatabaseMovieSample.War.runtime,
        status = null,
        tagline = DatabaseMovieSample.War.tagline,
        title = DatabaseMovieSample.War.title,
        tvShowTmdbId = null,
        tvShowTraktId = null
    )
}
