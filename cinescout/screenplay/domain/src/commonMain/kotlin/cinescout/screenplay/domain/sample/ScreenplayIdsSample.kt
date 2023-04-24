package cinescout.screenplay.domain.sample

import cinescout.screenplay.domain.model.ScreenplayIds
import cinescout.screenplay.domain.model.TmdbScreenplayId

object ScreenplayIdsSample {

    val Avatar3 = ScreenplayIds.Movie(
        tmdb = TmdbScreenplayIdSample.Avatar3,
        trakt = TraktScreenplayIdSample.Avatar3
    )

    val BreakingBad = ScreenplayIds.TvShow(
        tmdb = TmdbScreenplayIdSample.BreakingBad,
        trakt = TraktScreenplayIdSample.BreakingBad
    )

    val Dexter = ScreenplayIds.TvShow(
        tmdb = TmdbScreenplayIdSample.Dexter,
        trakt = TraktScreenplayIdSample.Dexter
    )

    val Grimm = ScreenplayIds.TvShow(
        tmdb = TmdbScreenplayIdSample.Grimm,
        trakt = TraktScreenplayIdSample.Grimm
    )

    val Inception = ScreenplayIds.Movie(
        tmdb = TmdbScreenplayIdSample.Inception,
        trakt = TraktScreenplayIdSample.Inception
    )

    val Sherlock = ScreenplayIds.TvShow(
        tmdb = TmdbScreenplayIdSample.Sherlock,
        trakt = TraktScreenplayIdSample.Sherlock
    )

    val TheWalkingDeadDeadCity = ScreenplayIds.TvShow(
        tmdb = TmdbScreenplayIdSample.TheWalkingDeadDeadCity,
        trakt = TraktScreenplayIdSample.TheWalkingDeadDeadCity
    )

    val TheWolfOfWallStreet = ScreenplayIds.Movie(
        tmdb = TmdbScreenplayIdSample.TheWolfOfWallStreet,
        trakt = TraktScreenplayIdSample.TheWolfOfWallStreet
    )

    val War = ScreenplayIds.Movie(
        tmdb = TmdbScreenplayIdSample.War,
        trakt = TraktScreenplayIdSample.War
    )

    fun TmdbScreenplayId.toIds() = when (this) {
        TmdbScreenplayIdSample.BreakingBad -> BreakingBad
        TmdbScreenplayIdSample.Dexter -> Dexter
        TmdbScreenplayIdSample.Grimm -> Grimm
        TmdbScreenplayIdSample.Inception -> Inception
        TmdbScreenplayIdSample.TheWolfOfWallStreet -> TheWolfOfWallStreet
        TmdbScreenplayIdSample.War -> War
        else -> error("Unknown tmdb id: $this")
    }
}
