package cinescout.screenplay.data.remote.tmdb.res

import cinescout.screenplay.data.remote.tmdb.model.TmdbPage

object TmdbMovieRecommendationsJson {

    val TwoMovies = """
        {
            "${TmdbPage.Page}": "1",
            "${TmdbPage.Results}": [
                ${TmdbMovieJson.Inception},
                ${TmdbMovieJson.TheWolfOfWallStreet}
            ],
            "${TmdbPage.TotalPages}": "1",
            "${TmdbPage.TotalResults}": "1"
        }
    """.trimIndent()
}
