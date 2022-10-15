package cinescout.tvshows.data.remote.tmdb.testutil

import cinescout.tvshows.data.remote.tmdb.model.GetTvShowWatchlist

object TmdbTvShowsWatchlistJson {

    val OneTvShow = """
        {
            "${GetTvShowWatchlist.Response.Page}": "1",
            "${GetTvShowWatchlist.Response.Results}": [
                ${TmdbTvShowJson.Grimm} 
            ],
            "${GetTvShowWatchlist.Response.TotalPages}": "1",
            "${GetTvShowWatchlist.Response.TotalResults}": "1"
        }
    """
}
