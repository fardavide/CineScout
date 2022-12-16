package cinescout.tvshows.data.remote.trakt.testutil

import cinescout.tvshows.data.remote.trakt.model.GetWatchlist
import cinescout.tvshows.domain.sample.TvShowSample

object TraktTvShowsWatchlistJson {

    val OneTvShow = """
        [
            {
                "${GetWatchlist.Result.TvShowType}": {
                    "${GetWatchlist.Result.Title}": "${TvShowSample.Grimm.title}",
                    "${GetWatchlist.Result.Ids}": {
                        "${GetWatchlist.Result.Ids.Tmdb}": ${TvShowSample.Grimm.tmdbId.value}
                    }
                }
            }
        ]
    """
}
