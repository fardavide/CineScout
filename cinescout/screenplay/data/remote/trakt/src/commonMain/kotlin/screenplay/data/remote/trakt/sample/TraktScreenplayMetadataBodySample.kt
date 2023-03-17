package screenplay.data.remote.trakt.sample

import cinescout.screenplay.domain.sample.TmdbScreenplayIdSample
import screenplay.data.remote.trakt.model.TraktMovieIds
import screenplay.data.remote.trakt.model.TraktMovieMetadataBody
import screenplay.data.remote.trakt.model.TraktTvShowIds
import screenplay.data.remote.trakt.model.TraktTvShowMetadataBody

object TraktScreenplayMetadataBodySample {

    val BreakingBad = TraktTvShowMetadataBody(
        ids = TraktTvShowIds(
            tmdb = TmdbScreenplayIdSample.BreakingBad
        )
    )
    val Dexter = TraktTvShowMetadataBody(
        ids = TraktTvShowIds(
            tmdb = TmdbScreenplayIdSample.Dexter
        )
    )
    val Grimm = TraktTvShowMetadataBody(
        ids = TraktTvShowIds(
            tmdb = TmdbScreenplayIdSample.Grimm
        )
    )

    val Inception = TraktMovieMetadataBody(
        ids = TraktMovieIds(
            tmdb = TmdbScreenplayIdSample.Inception
        )
    )
    val TheWolfOfWallStreet = TraktMovieMetadataBody(
        ids = TraktMovieIds(
            tmdb = TmdbScreenplayIdSample.TheWolfOfWallStreet
        )
    )
    val War = TraktMovieMetadataBody(
        ids = TraktMovieIds(
            tmdb = TmdbScreenplayIdSample.War
        )
    )
}
