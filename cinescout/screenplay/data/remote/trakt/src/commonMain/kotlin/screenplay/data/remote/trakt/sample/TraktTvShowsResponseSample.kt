package screenplay.data.remote.trakt.sample

import screenplay.data.remote.trakt.model.TraktTvShowsMetadataResponse

object TraktTvShowsResponseSample {

    val OneTvShow: TraktTvShowsMetadataResponse = listOf(
        TraktTvShowBodySample.BreakingBad
    )

    val TwoTvShows: TraktTvShowsMetadataResponse = listOf(
        TraktTvShowBodySample.BreakingBad,
        TraktTvShowBodySample.Dexter
    )

    val ThreeTvShows: TraktTvShowsMetadataResponse = listOf(
        TraktTvShowBodySample.BreakingBad,
        TraktTvShowBodySample.Dexter,
        TraktTvShowBodySample.Grimm
    )
}
