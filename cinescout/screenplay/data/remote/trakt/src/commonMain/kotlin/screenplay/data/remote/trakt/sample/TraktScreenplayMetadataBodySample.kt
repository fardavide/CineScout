package screenplay.data.remote.trakt.sample

import cinescout.screenplay.domain.sample.TmdbScreenplayIdSample
import cinescout.screenplay.domain.sample.TraktScreenplayIdSample
import screenplay.data.remote.trakt.model.TraktMovieIds
import screenplay.data.remote.trakt.model.TraktMovieMetadataBody
import screenplay.data.remote.trakt.model.TraktTvShowIds
import screenplay.data.remote.trakt.model.TraktTvShowMetadataBody

object TraktScreenplayMetadataBodySample {

    val BreakingBad = TraktTvShowMetadataBody(
        ids = TraktTvShowIds(
            tmdb = TmdbScreenplayIdSample.BreakingBad,
            trakt = TraktScreenplayIdSample.BreakingBad
        )
    )
    val Dexter = TraktTvShowMetadataBody(
        ids = TraktTvShowIds(
            tmdb = TmdbScreenplayIdSample.Dexter,
            trakt = TraktScreenplayIdSample.Dexter
        )
    )
    val Grimm = TraktTvShowMetadataBody(
        ids = TraktTvShowIds(
            tmdb = TmdbScreenplayIdSample.Grimm,
            trakt = TraktScreenplayIdSample.Grimm
        )
    )

    val Inception = TraktMovieMetadataBody(
        ids = TraktMovieIds(
            tmdb = TmdbScreenplayIdSample.Inception,
            trakt = TraktScreenplayIdSample.Inception
        )
    )
    val TheWolfOfWallStreet = TraktMovieMetadataBody(
        ids = TraktMovieIds(
            tmdb = TmdbScreenplayIdSample.TheWolfOfWallStreet,
            trakt = TraktScreenplayIdSample.TheWolfOfWallStreet
        )
    )
    val War = TraktMovieMetadataBody(
        ids = TraktMovieIds(
            tmdb = TmdbScreenplayIdSample.War,
            trakt = TraktScreenplayIdSample.War
        )
    )
}
