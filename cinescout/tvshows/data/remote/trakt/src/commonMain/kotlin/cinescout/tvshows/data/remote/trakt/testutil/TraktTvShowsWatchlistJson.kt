package cinescout.tvshows.data.remote.trakt.testutil

import cinescout.tvshows.domain.testdata.TvShowTestData

object TraktTvShowsWatchlistJson {

    val OneTvShow = """
        [
            {
                "movie": {
                    "title": "${TvShowTestData.Grimm.title}",
                    "ids": {
                        "tmdb": ${TvShowTestData.Grimm.tmdbId.value}
                    }
                }
            }
        ]
    """
}
