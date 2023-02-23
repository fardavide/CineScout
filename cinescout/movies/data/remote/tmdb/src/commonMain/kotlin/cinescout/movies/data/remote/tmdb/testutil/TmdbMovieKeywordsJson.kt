package cinescout.movies.data.remote.tmdb.testutil

import cinescout.movies.data.remote.tmdb.model.GetMovieKeywords.Response
import cinescout.movies.domain.sample.MovieKeywordsSample

object TmdbMovieKeywordsJson {

    val Inception = """
        {
            "${Response.MovieId}": "${MovieKeywordsSample.Inception.movieId.value}",
            "${Response.Keywords}": [
                {
                    "${Response.Keyword.Id}": "${MovieKeywordsSample.Inception.keywords[0].id.value}",
                    "${Response.Keyword.Name}": "${MovieKeywordsSample.Inception.keywords[0].name}"
                },
                {
                    "${Response.Keyword.Id}": "${MovieKeywordsSample.Inception.keywords[1].id.value}",
                    "${Response.Keyword.Name}": "${MovieKeywordsSample.Inception.keywords[1].name}"
                },
                {
                    "${Response.Keyword.Id}": "${MovieKeywordsSample.Inception.keywords[2].id.value}",
                    "${Response.Keyword.Name}": "${MovieKeywordsSample.Inception.keywords[2].name}"
                }
            ]
        }
    """.trimIndent()

    val TheWolfOfWallStreet = """
        {
            "${Response.MovieId}": "${MovieKeywordsSample.TheWolfOfWallStreet.movieId.value}",
            "${Response.Keywords}": [
                {
                    "${Response.Keyword.Id}": "${MovieKeywordsSample.TheWolfOfWallStreet.keywords[0].id.value}",
                    "${Response.Keyword.Name}": "${MovieKeywordsSample.TheWolfOfWallStreet.keywords[0].name}"
                },
                {
                    "${Response.Keyword.Id}": "${MovieKeywordsSample.TheWolfOfWallStreet.keywords[1].id.value}",
                    "${Response.Keyword.Name}": "${MovieKeywordsSample.TheWolfOfWallStreet.keywords[1].name}"
                },
                {
                    "${Response.Keyword.Id}": "${MovieKeywordsSample.TheWolfOfWallStreet.keywords[2].id.value}",
                    "${Response.Keyword.Name}": "${MovieKeywordsSample.TheWolfOfWallStreet.keywords[2].name}"
                }
            ]
        }
    """.trimIndent()

    val War = """
        {
            "${Response.MovieId}": "${MovieKeywordsSample.War.movieId.value}",
            "${Response.Keywords}": [
                {
                    "${Response.Keyword.Id}": "${MovieKeywordsSample.War.keywords[0].id.value}",
                    "${Response.Keyword.Name}": "${MovieKeywordsSample.War.keywords[0].name}"
                },
                {
                    "${Response.Keyword.Id}": "${MovieKeywordsSample.War.keywords[1].id.value}",
                    "${Response.Keyword.Name}": "${MovieKeywordsSample.War.keywords[1].name}"
                },
                {
                    "${Response.Keyword.Id}": "${MovieKeywordsSample.War.keywords[2].id.value}",
                    "${Response.Keyword.Name}": "${MovieKeywordsSample.War.keywords[2].name}"
                }
            ]
        }
    """.trimIndent()
}
