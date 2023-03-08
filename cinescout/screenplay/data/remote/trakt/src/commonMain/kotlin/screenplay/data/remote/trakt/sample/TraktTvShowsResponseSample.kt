package screenplay.data.remote.trakt.sample

import screenplay.data.remote.trakt.model.TraktTvShowsResponse

object TraktTvShowsResponseSample {

    val OneTvShow: TraktTvShowsResponse = listOf(
        TraktTvShowBodySample.BreakingBad
    )

    val TwoTvShows: TraktTvShowsResponse = listOf(
        TraktTvShowBodySample.BreakingBad,
        TraktTvShowBodySample.Dexter
    )

    val ThreeTvShows: TraktTvShowsResponse = listOf(
        TraktTvShowBodySample.BreakingBad,
        TraktTvShowBodySample.Dexter,
        TraktTvShowBodySample.Grimm
    )
}
