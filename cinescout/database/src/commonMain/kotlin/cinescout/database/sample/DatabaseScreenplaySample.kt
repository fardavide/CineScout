package cinescout.database.sample

import cinescout.database.model.DatabaseScreenplay

object DatabaseScreenplaySample {

    val BreakingBad = DatabaseScreenplay(
        firstAirDate = DatabaseTvShowSample.BreakingBad.firstAirDate,
        movieTmdbId = null,
        overview = DatabaseTvShowSample.BreakingBad.overview,
        ratingAverage = DatabaseTvShowSample.BreakingBad.ratingAverage,
        ratingCount = DatabaseTvShowSample.BreakingBad.ratingCount,
        releaseDate = null,
        title = DatabaseTvShowSample.BreakingBad.title,
        tvShowTmdbId = DatabaseTvShowSample.BreakingBad.tmdbId
    )

    val Grimm = DatabaseScreenplay(
        firstAirDate = DatabaseTvShowSample.Grimm.firstAirDate,
        movieTmdbId = null,
        overview = DatabaseTvShowSample.Grimm.overview,
        ratingAverage = DatabaseTvShowSample.Grimm.ratingAverage,
        ratingCount = DatabaseTvShowSample.Grimm.ratingCount,
        releaseDate = null,
        title = DatabaseTvShowSample.Grimm.title,
        tvShowTmdbId = DatabaseTvShowSample.Grimm.tmdbId
    )

    val Inception = DatabaseScreenplay(
        firstAirDate = null,
        movieTmdbId = DatabaseMovieSample.Inception.tmdbId,
        overview = DatabaseMovieSample.Inception.overview,
        ratingAverage = DatabaseMovieSample.Inception.ratingAverage,
        ratingCount = DatabaseMovieSample.Inception.ratingCount,
        releaseDate = DatabaseMovieSample.Inception.releaseDate,
        title = DatabaseMovieSample.Inception.title,
        tvShowTmdbId = null
    )

    val Memento = DatabaseScreenplay(
        firstAirDate = null,
        movieTmdbId = DatabaseMovieSample.Memento.tmdbId,
        overview = DatabaseMovieSample.Memento.overview,
        ratingAverage = DatabaseMovieSample.Memento.ratingAverage,
        ratingCount = DatabaseMovieSample.Memento.ratingCount,
        releaseDate = DatabaseMovieSample.Memento.releaseDate,
        title = DatabaseMovieSample.Memento.title,
        tvShowTmdbId = null
    )

    val TheWolfOfWallStreet = DatabaseScreenplay(
        firstAirDate = null,
        movieTmdbId = DatabaseMovieSample.TheWolfOfWallStreet.tmdbId,
        overview = DatabaseMovieSample.TheWolfOfWallStreet.overview,
        ratingAverage = DatabaseMovieSample.TheWolfOfWallStreet.ratingAverage,
        ratingCount = DatabaseMovieSample.TheWolfOfWallStreet.ratingCount,
        releaseDate = DatabaseMovieSample.TheWolfOfWallStreet.releaseDate,
        title = DatabaseMovieSample.TheWolfOfWallStreet.title,
        tvShowTmdbId = null
    )

    val TVPatrolNorthernLuzon = DatabaseScreenplay(
        firstAirDate = DatabaseTvShowSample.TVPatrolNorthernLuzon.firstAirDate,
        movieTmdbId = null,
        overview = DatabaseTvShowSample.TVPatrolNorthernLuzon.overview,
        ratingAverage = DatabaseTvShowSample.TVPatrolNorthernLuzon.ratingAverage,
        ratingCount = DatabaseTvShowSample.TVPatrolNorthernLuzon.ratingCount,
        releaseDate = null,
        title = DatabaseTvShowSample.TVPatrolNorthernLuzon.title,
        tvShowTmdbId = DatabaseTvShowSample.TVPatrolNorthernLuzon.tmdbId
    )

    val War = DatabaseScreenplay(
        firstAirDate = null,
        movieTmdbId = DatabaseMovieSample.War.tmdbId,
        overview = DatabaseMovieSample.War.overview,
        ratingAverage = DatabaseMovieSample.War.ratingAverage,
        ratingCount = DatabaseMovieSample.War.ratingCount,
        releaseDate = DatabaseMovieSample.War.releaseDate,
        title = DatabaseMovieSample.War.title,
        tvShowTmdbId = null
    )
}
