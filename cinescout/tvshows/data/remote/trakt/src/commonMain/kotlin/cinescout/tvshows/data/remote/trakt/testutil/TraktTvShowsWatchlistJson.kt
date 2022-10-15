package cinescout.tvshows.data.remote.trakt.testutil

import cinescout.tvshows.data.remote.trakt.model.GetWatchlist
import cinescout.tvshows.domain.testdata.TvShowTestData

object TraktTvShowsWatchlistJson {

    val OneTvShow = """
        [
            {
                "${GetWatchlist.Result.TvShowType}": {
                    "${GetWatchlist.Result.Title}": "${TvShowTestData.Grimm.title}",
                    "${GetWatchlist.Result.Ids}": {
                        "${GetWatchlist.Result.Ids.Tmdb}": ${TvShowTestData.Grimm.tmdbId.value}
                    }
                }
            }
        ]
    """
}
