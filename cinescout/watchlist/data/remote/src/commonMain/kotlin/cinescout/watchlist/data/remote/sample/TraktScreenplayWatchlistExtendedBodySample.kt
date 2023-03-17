package cinescout.watchlist.data.remote.sample

import cinescout.watchlist.data.remote.model.TraktMovieWatchlistExtendedBody
import cinescout.watchlist.data.remote.model.TraktTvShowWatchlistExtendedBody
import screenplay.data.remote.trakt.sample.TraktScreenplayExtendedBodySample

object TraktScreenplayWatchlistExtendedBodySample {

    val BreakingBad = TraktTvShowWatchlistExtendedBody(
        tvShow = TraktScreenplayExtendedBodySample.BreakingBad
    )

    val Dexter = TraktTvShowWatchlistExtendedBody(
        tvShow = TraktScreenplayExtendedBodySample.Dexter
    )

    val Grimm = TraktTvShowWatchlistExtendedBody(
        tvShow = TraktScreenplayExtendedBodySample.Grimm
    )

    val Inception = TraktMovieWatchlistExtendedBody(
        movie = TraktScreenplayExtendedBodySample.Inception
    )

    val TheWolfOfWallStreet = TraktMovieWatchlistExtendedBody(
        movie = TraktScreenplayExtendedBodySample.TheWolfOfWallStreet
    )

    val War = TraktMovieWatchlistExtendedBody(
        movie = TraktScreenplayExtendedBodySample.War
    )
}
