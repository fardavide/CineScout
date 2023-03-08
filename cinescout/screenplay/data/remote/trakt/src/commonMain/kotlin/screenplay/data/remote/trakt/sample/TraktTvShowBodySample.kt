package screenplay.data.remote.trakt.sample

import cinescout.screenplay.domain.sample.ScreenplaySample
import cinescout.screenplay.domain.sample.TmdbScreenplayIdSample
import screenplay.data.remote.trakt.model.TraktTvShowBody

object TraktTvShowBodySample {

    val BreakingBad = TraktTvShowBody(
        ids = TraktTvShowBody.Ids(
            tmdb = TmdbScreenplayIdSample.BreakingBad
        ),
        title = ScreenplaySample.BreakingBad.title
    )
    val Dexter = TraktTvShowBody(
        ids = TraktTvShowBody.Ids(
            tmdb = TmdbScreenplayIdSample.Dexter
        ),
        title = ScreenplaySample.Dexter.title
    )
    val Grimm = TraktTvShowBody(
        ids = TraktTvShowBody.Ids(
            tmdb = TmdbScreenplayIdSample.Grimm
        ),
        title = ScreenplaySample.Grimm.title
    )
}
