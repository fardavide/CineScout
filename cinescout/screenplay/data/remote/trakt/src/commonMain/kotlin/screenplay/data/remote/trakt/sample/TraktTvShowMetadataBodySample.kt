package screenplay.data.remote.trakt.sample

import cinescout.screenplay.domain.sample.TmdbScreenplayIdSample
import screenplay.data.remote.trakt.model.TraktTvShowIds
import screenplay.data.remote.trakt.model.TraktTvShowMetadataBody

object TraktTvShowMetadataBodySample {

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
}
