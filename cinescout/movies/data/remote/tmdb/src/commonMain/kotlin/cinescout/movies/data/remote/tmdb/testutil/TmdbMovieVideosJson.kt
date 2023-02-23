package cinescout.movies.data.remote.tmdb.testutil

import cinescout.movies.data.remote.tmdb.model.GetMovieVideos.Response
import cinescout.movies.domain.sample.MovieKeywordsSample
import cinescout.movies.domain.testdata.MovieVideosTestData

object TmdbMovieVideosJson {

    val Inception = """
        {
            "${Response.MovieId}": "${MovieKeywordsSample.Inception.movieId.value}",
            "${Response.Results}": [
                {
                    "${Response.Video.Id}": "${MovieVideosTestData.Inception.videos[0].id.value}",
                    "${Response.Video.Key}": "${MovieVideosTestData.Inception.videos[0].key}",
                    "${Response.Video.Name}": "${MovieVideosTestData.Inception.videos[0].title}",
                    "${Response.Video.Site}": "${MovieVideosTestData.Inception.videos[0].site.name}",
                    "${Response.Video.Size}": "1080",
                    "${Response.Video.Type}": "${MovieVideosTestData.Inception.videos[0].type.name}"
                }
            ]
        }
    """.trimIndent()

    val TheWolfOfWallStreet = """
        {
            "${Response.MovieId}": "${MovieKeywordsSample.TheWolfOfWallStreet.movieId.value}",
            "${Response.Results}": [
                {
                    "${Response.Video.Id}": "${MovieVideosTestData.TheWolfOfWallStreet.videos[0].id.value}",
                    "${Response.Video.Key}": "${MovieVideosTestData.TheWolfOfWallStreet.videos[0].key}",
                    "${Response.Video.Name}": "${MovieVideosTestData.TheWolfOfWallStreet.videos[0].title}",
                    "${Response.Video.Site}": "${MovieVideosTestData.TheWolfOfWallStreet.videos[0].site.name}",
                    "${Response.Video.Size}": "1080",
                    "${Response.Video.Type}": "${MovieVideosTestData.TheWolfOfWallStreet.videos[0].type.name}"
                }
            ]
        }
    """.trimIndent()

    val War = """
        {
            "${Response.MovieId}": "${MovieKeywordsSample.War.movieId.value}",
            "${Response.Results}": []
        }
    """.trimIndent()
}
