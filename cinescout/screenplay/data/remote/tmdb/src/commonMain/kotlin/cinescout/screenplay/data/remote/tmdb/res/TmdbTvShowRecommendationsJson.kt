package cinescout.screenplay.data.remote.tmdb.res

import cinescout.screenplay.data.remote.tmdb.model.TmdbPage

object TmdbTvShowRecommendationsJson {

    val TwoTvShows = """
        {
            "${TmdbPage.Page}": "1",
            "${TmdbPage.Results}": [
                ${TmdbTvShowJson.BreakingBad},
                ${TmdbTvShowJson.Grimm}
            ],
            "${TmdbPage.TotalPages}": "1",
            "${TmdbPage.TotalResults}": "1"
        }
    """.trimIndent()
}
