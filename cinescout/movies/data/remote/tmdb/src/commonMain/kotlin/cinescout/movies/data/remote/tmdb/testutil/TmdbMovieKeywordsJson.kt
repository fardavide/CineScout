package cinescout.movies.data.remote.tmdb.testutil

import cinescout.movies.data.remote.tmdb.model.GetMovieKeywords.Response
import cinescout.screenplay.domain.sample.ScreenplayKeywordsSample

object TmdbMovieKeywordsJson {

    val Inception = """
        {
            "${Response.MovieId}": "${ScreenplayKeywordsSample.Inception.movieId.value}",
            "${Response.Keywords}": [
                {
                    "${Response.Keyword.Id}": "${ScreenplayKeywordsSample.Inception.keywords[0].id.value}",
                    "${Response.Keyword.Name}": "${ScreenplayKeywordsSample.Inception.keywords[0].name}"
                },
                {
                    "${Response.Keyword.Id}": "${ScreenplayKeywordsSample.Inception.keywords[1].id.value}",
                    "${Response.Keyword.Name}": "${ScreenplayKeywordsSample.Inception.keywords[1].name}"
                },
                {
                    "${Response.Keyword.Id}": "${ScreenplayKeywordsSample.Inception.keywords[2].id.value}",
                    "${Response.Keyword.Name}": "${ScreenplayKeywordsSample.Inception.keywords[2].name}"
                }
            ]
        }
    """.trimIndent()

    val TheWolfOfWallStreet = """
        {
            "${Response.MovieId}": "${ScreenplayKeywordsSample.TheWolfOfWallStreet.movieId.value}",
            "${Response.Keywords}": [
                {
                    "${Response.Keyword.Id}": "${ScreenplayKeywordsSample.TheWolfOfWallStreet.keywords[0].id.value}",
                    "${Response.Keyword.Name}": "${ScreenplayKeywordsSample.TheWolfOfWallStreet.keywords[0].name}"
                },
                {
                    "${Response.Keyword.Id}": "${ScreenplayKeywordsSample.TheWolfOfWallStreet.keywords[1].id.value}",
                    "${Response.Keyword.Name}": "${ScreenplayKeywordsSample.TheWolfOfWallStreet.keywords[1].name}"
                },
                {
                    "${Response.Keyword.Id}": "${ScreenplayKeywordsSample.TheWolfOfWallStreet.keywords[2].id.value}",
                    "${Response.Keyword.Name}": "${ScreenplayKeywordsSample.TheWolfOfWallStreet.keywords[2].name}"
                }
            ]
        }
    """.trimIndent()

    val War = """
        {
            "${Response.MovieId}": "${ScreenplayKeywordsSample.War.movieId.value}",
            "${Response.Keywords}": [
                {
                    "${Response.Keyword.Id}": "${ScreenplayKeywordsSample.War.keywords[0].id.value}",
                    "${Response.Keyword.Name}": "${ScreenplayKeywordsSample.War.keywords[0].name}"
                },
                {
                    "${Response.Keyword.Id}": "${ScreenplayKeywordsSample.War.keywords[1].id.value}",
                    "${Response.Keyword.Name}": "${ScreenplayKeywordsSample.War.keywords[1].name}"
                },
                {
                    "${Response.Keyword.Id}": "${ScreenplayKeywordsSample.War.keywords[2].id.value}",
                    "${Response.Keyword.Name}": "${ScreenplayKeywordsSample.War.keywords[2].name}"
                }
            ]
        }
    """.trimIndent()
}
