package screenplay.data.remote.trakt.sample

import screenplay.data.remote.trakt.model.TraktTvShowsMetadataResponse

object TraktTvShowsMetadataResponseSample {

    val OneTvShow: TraktTvShowsMetadataResponse = listOf(
        TraktTvShowMetadataBodySample.BreakingBad
    )

    val TwoTvShows: TraktTvShowsMetadataResponse = listOf(
        TraktTvShowMetadataBodySample.BreakingBad,
        TraktTvShowMetadataBodySample.Dexter
    )

    val ThreeTvShows: TraktTvShowsMetadataResponse = listOf(
        TraktTvShowMetadataBodySample.BreakingBad,
        TraktTvShowMetadataBodySample.Dexter,
        TraktTvShowMetadataBodySample.Grimm
    )
}
