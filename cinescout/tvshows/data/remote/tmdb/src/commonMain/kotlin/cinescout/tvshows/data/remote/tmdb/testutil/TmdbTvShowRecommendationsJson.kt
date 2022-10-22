package cinescout.tvshows.data.remote.tmdb.testutil

import cinescout.tvshows.data.remote.tmdb.model.GetTvShowRecommendations

object TmdbTvShowRecommendationsJson {

    val TwoTvShows = """
    {
        "${GetTvShowRecommendations.Response.Page}": "1",
        "${GetTvShowRecommendations.Response.Results}": [
            ${TmdbTvShowJson.BreakingBad},
            ${TmdbTvShowJson.Grimm}
        ],
        "${GetTvShowRecommendations.Response.TotalPages}": "1",
        "${GetTvShowRecommendations.Response.TotalResults}": "1"
    }
    """
}
