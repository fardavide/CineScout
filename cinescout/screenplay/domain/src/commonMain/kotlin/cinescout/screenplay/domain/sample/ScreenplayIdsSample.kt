package cinescout.screenplay.domain.sample

import cinescout.screenplay.domain.model.ids.EpisodeIds
import cinescout.screenplay.domain.model.ids.MovieIds
import cinescout.screenplay.domain.model.ids.TmdbScreenplayId
import cinescout.screenplay.domain.model.ids.TvShowIds

object ScreenplayIdsSample {

    val Avatar3 = MovieIds(
        tmdb = TmdbScreenplayIdSample.Avatar3,
        trakt = TraktScreenplayIdSample.Avatar3
    )

    val BreakingBad = TvShowIds(
        tmdb = TmdbScreenplayIdSample.BreakingBad,
        trakt = TraktScreenplayIdSample.BreakingBad
    )

    val BreakingBad_s1e1 = EpisodeIds(
        tmdb = TmdbScreenplayIdSample.BreakingBad_s1e1,
        trakt = TraktScreenplayIdSample.BreakingBad_s1e1
    )

    val BreakingBad_s1e2 = EpisodeIds(
        tmdb = TmdbScreenplayIdSample.BreakingBad_s1e2,
        trakt = TraktScreenplayIdSample.BreakingBad_s1e2
    )

    val Dexter = TvShowIds(
        tmdb = TmdbScreenplayIdSample.Dexter,
        trakt = TraktScreenplayIdSample.Dexter
    )

    val Grimm = TvShowIds(
        tmdb = TmdbScreenplayIdSample.Grimm,
        trakt = TraktScreenplayIdSample.Grimm
    )

    val Inception = MovieIds(
        tmdb = TmdbScreenplayIdSample.Inception,
        trakt = TraktScreenplayIdSample.Inception
    )

    val Sherlock = TvShowIds(
        tmdb = TmdbScreenplayIdSample.Sherlock,
        trakt = TraktScreenplayIdSample.Sherlock
    )

    val TheWalkingDeadDeadCity = TvShowIds(
        tmdb = TmdbScreenplayIdSample.TheWalkingDeadDeadCity,
        trakt = TraktScreenplayIdSample.TheWalkingDeadDeadCity
    )

    val TheWolfOfWallStreet = MovieIds(
        tmdb = TmdbScreenplayIdSample.TheWolfOfWallStreet,
        trakt = TraktScreenplayIdSample.TheWolfOfWallStreet
    )

    val War = MovieIds(
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
