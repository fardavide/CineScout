package cinescout.movies.data.remote.tmdb.testutil

import cinescout.movies.data.remote.tmdb.model.GetMovieVideos.Response
import cinescout.movies.domain.testdata.ScreenplayVideosSample
import cinescout.screenplay.domain.sample.ScreenplayKeywordsSample

object TmdbMovieVideosJson {

    val Inception = """
        {
            "${Response.MovieId}": "${ScreenplayKeywordsSample.Inception.movieId.value}",
            "${Response.Results}": [
                {
                    "${Response.Video.Id}": "${ScreenplayVideosSample.Inception.videos[0].id.value}",
                    "${Response.Video.Key}": "${ScreenplayVideosSample.Inception.videos[0].key}",
                    "${Response.Video.Name}": "${ScreenplayVideosSample.Inception.videos[0].title}",
                    "${Response.Video.Site}": "${ScreenplayVideosSample.Inception.videos[0].site.name}",
                    "${Response.Video.Size}": "1080",
                    "${Response.Video.Type}": "${ScreenplayVideosSample.Inception.videos[0].type.name}"
                }
            ]
        }
    """.trimIndent()

    val TheWolfOfWallStreet = """
        {
            "${Response.MovieId}": "${ScreenplayKeywordsSample.TheWolfOfWallStreet.movieId.value}",
            "${Response.Results}": [
                {
                    "${Response.Video.Id}": "${ScreenplayVideosSample.TheWolfOfWallStreet.videos[0].id.value}",
                    "${Response.Video.Key}": "${ScreenplayVideosSample.TheWolfOfWallStreet.videos[0].key}",
                    "${Response.Video.Name}": "${ScreenplayVideosSample.TheWolfOfWallStreet.videos[0].title}",
                    "${Response.Video.Site}": "${ScreenplayVideosSample.TheWolfOfWallStreet.videos[0].site.name}",
                    "${Response.Video.Size}": "1080",
                    "${Response.Video.Type}": "${ScreenplayVideosSample.TheWolfOfWallStreet.videos[0].type.name}"
                }
            ]
        }
    """.trimIndent()

    val War = """
        {
            "${Response.MovieId}": "${ScreenplayKeywordsSample.War.movieId.value}",
            "${Response.Results}": []
        }
    """.trimIndent()
}
