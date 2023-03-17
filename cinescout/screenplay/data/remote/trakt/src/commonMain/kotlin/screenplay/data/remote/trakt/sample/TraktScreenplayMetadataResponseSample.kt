package screenplay.data.remote.trakt.sample

import screenplay.data.remote.trakt.model.TraktMoviesMetadataResponse
import screenplay.data.remote.trakt.model.TraktTvShowsMetadataResponse

object TraktScreenplayMetadataResponseSample {

    val OneMovie: TraktMoviesMetadataResponse = listOf(
        TraktScreenplayMetadataBodySample.Inception
    )

    val TwoMovies: TraktMoviesMetadataResponse = listOf(
        TraktScreenplayMetadataBodySample.Inception,
        TraktScreenplayMetadataBodySample.TheWolfOfWallStreet
    )

    val ThreeMovies: TraktMoviesMetadataResponse = listOf(
        TraktScreenplayMetadataBodySample.Inception,
        TraktScreenplayMetadataBodySample.TheWolfOfWallStreet,
        TraktScreenplayMetadataBodySample.War
    )

    val OneTvShow: TraktTvShowsMetadataResponse = listOf(
        TraktScreenplayMetadataBodySample.BreakingBad
    )

    val TwoTvShows: TraktTvShowsMetadataResponse = listOf(
        TraktScreenplayMetadataBodySample.BreakingBad,
        TraktScreenplayMetadataBodySample.Dexter
    )

    val ThreeTvShows: TraktTvShowsMetadataResponse = listOf(
        TraktScreenplayMetadataBodySample.BreakingBad,
        TraktScreenplayMetadataBodySample.Dexter,
        TraktScreenplayMetadataBodySample.Grimm
    )
}
