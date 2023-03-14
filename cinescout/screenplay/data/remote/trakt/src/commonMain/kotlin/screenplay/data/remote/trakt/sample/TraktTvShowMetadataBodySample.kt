package screenplay.data.remote.trakt.sample

import cinescout.screenplay.domain.sample.ScreenplaySample
import cinescout.screenplay.domain.sample.TmdbScreenplayIdSample
import screenplay.data.remote.trakt.model.TraktTvShowIds
import screenplay.data.remote.trakt.model.TraktTvShowMetadataBody

object TraktTvShowMetadataBodySample {

    val BreakingBad = TraktTvShowMetadataBody(
        ids = TraktTvShowIds(
            tmdb = TmdbScreenplayIdSample.BreakingBad
        ),
        title = ScreenplaySample.BreakingBad.title
    )
    val Dexter = TraktTvShowMetadataBody(
        ids = TraktTvShowIds(
            tmdb = TmdbScreenplayIdSample.Dexter
        ),
        title = ScreenplaySample.Dexter.title
    )
    val Grimm = TraktTvShowMetadataBody(
        ids = TraktTvShowIds(
            tmdb = TmdbScreenplayIdSample.Grimm
        ),
        title = ScreenplaySample.Grimm.title
    )
}
