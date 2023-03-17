package cinescout.watchlist.data.remote.sample

import cinescout.watchlist.data.remote.model.TraktMovieWatchlistMetadataBody
import cinescout.watchlist.data.remote.model.TraktTvShowWatchlistMetadataBody
import screenplay.data.remote.trakt.sample.TraktScreenplayMetadataBodySample

object TraktScreenplayWatchlistMetadataBodySample {

    val BreakingBad = TraktTvShowWatchlistMetadataBody(
        tvShow = TraktScreenplayMetadataBodySample.BreakingBad
    )

    val Dexter = TraktTvShowWatchlistMetadataBody(
        tvShow = TraktScreenplayMetadataBodySample.Dexter
    )

    val Grimm = TraktTvShowWatchlistMetadataBody(
        tvShow = TraktScreenplayMetadataBodySample.Grimm
    )

    val Inception = TraktMovieWatchlistMetadataBody(
        movie = TraktScreenplayMetadataBodySample.Inception
    )

    val TheWolfOfWallStreet = TraktMovieWatchlistMetadataBody(
        movie = TraktScreenplayMetadataBodySample.TheWolfOfWallStreet
    )

    val War = TraktMovieWatchlistMetadataBody(
        movie = TraktScreenplayMetadataBodySample.War
    )
}
