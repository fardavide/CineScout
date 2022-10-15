package cinescout.movies.data.remote.tmdb.testutil

import cinescout.movies.data.remote.tmdb.model.GetMovieKeywords.Response
import cinescout.movies.domain.testdata.MovieKeywordsTestData

object TmdbMovieKeywordsJson {

    val Inception = """
        {
            "${Response.MovieId}": "${MovieKeywordsTestData.Inception.movieId.value}",
            "${Response.Keywords}": [
                {
                    "${Response.Keyword.Id}": "${MovieKeywordsTestData.Inception.keywords[0].id.value}",
                    "${Response.Keyword.Name}": "${MovieKeywordsTestData.Inception.keywords[0].name}"
                },
                {
                    "${Response.Keyword.Id}": "${MovieKeywordsTestData.Inception.keywords[1].id.value}",
                    "${Response.Keyword.Name}": "${MovieKeywordsTestData.Inception.keywords[1].name}"
                },
                {
                    "${Response.Keyword.Id}": "${MovieKeywordsTestData.Inception.keywords[2].id.value}",
                    "${Response.Keyword.Name}": "${MovieKeywordsTestData.Inception.keywords[2].name}"
                }
            ]
        }
    """

    val TheWolfOfWallStreet = """
        {
            "${Response.MovieId}": "${MovieKeywordsTestData.TheWolfOfWallStreet.movieId.value}",
            "${Response.Keywords}": [
                {
                    "${Response.Keyword.Id}": "${MovieKeywordsTestData.TheWolfOfWallStreet.keywords[0].id.value}",
                    "${Response.Keyword.Name}": "${MovieKeywordsTestData.TheWolfOfWallStreet.keywords[0].name}"
                },
                {
                    "${Response.Keyword.Id}": "${MovieKeywordsTestData.TheWolfOfWallStreet.keywords[1].id.value}",
                    "${Response.Keyword.Name}": "${MovieKeywordsTestData.TheWolfOfWallStreet.keywords[1].name}"
                },
                {
                    "${Response.Keyword.Id}": "${MovieKeywordsTestData.TheWolfOfWallStreet.keywords[2].id.value}",
                    "${Response.Keyword.Name}": "${MovieKeywordsTestData.TheWolfOfWallStreet.keywords[2].name}"
                }
            ]
        }
    """
}
