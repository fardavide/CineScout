package cinescout.database.sample

import cinescout.database.model.DatabaseMovieIds
import cinescout.database.model.DatabaseTvShowIds

object DatabaseScreenplayIdsSample {

    val BreakingBad = DatabaseTvShowIds(
        tmdb = DatabaseTmdbScreenplayIdSample.BreakingBad,
        trakt = DatabaseTraktScreenplayIdSample.BreakingBad
    )
    val Dexter = DatabaseTvShowIds(
        tmdb = DatabaseTmdbScreenplayIdSample.Dexter,
        trakt = DatabaseTraktScreenplayIdSample.Dexter
    )
    val Grimm = DatabaseTvShowIds(
        tmdb = DatabaseTmdbScreenplayIdSample.Grimm,
        trakt = DatabaseTraktScreenplayIdSample.Grimm
    )
    val Inception = DatabaseMovieIds(
        tmdb = DatabaseTmdbScreenplayIdSample.Inception,
        trakt = DatabaseTraktScreenplayIdSample.Inception
    )
    val Memento = DatabaseMovieIds(
        tmdb = DatabaseTmdbScreenplayIdSample.Memento,
        trakt = DatabaseTraktScreenplayIdSample.Memento
    )
    val TheWolfOfWallStreet = DatabaseMovieIds(
        tmdb = DatabaseTmdbScreenplayIdSample.TheWolfOfWallStreet,
        trakt = DatabaseTraktScreenplayIdSample.TheWolfOfWallStreet
    )
    val TVPatrolNorthernLuzon = DatabaseTvShowIds(
        tmdb = DatabaseTmdbScreenplayIdSample.TVPatrolNorthernLuzon,
        trakt = DatabaseTraktScreenplayIdSample.TVPatrolNorthernLuzon
    )
    val War = DatabaseMovieIds(
        tmdb = DatabaseTmdbScreenplayIdSample.War,
        trakt = DatabaseTraktScreenplayIdSample.War
    )
}
