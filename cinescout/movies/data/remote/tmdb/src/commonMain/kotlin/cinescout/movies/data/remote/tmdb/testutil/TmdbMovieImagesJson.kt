package cinescout.movies.data.remote.tmdb.testutil

import cinescout.movies.data.remote.tmdb.model.GetMovieImages.Response
import cinescout.screenplay.domain.sample.ScreenplayImagesSample
import cinescout.screenplay.domain.sample.ScreenplayKeywordsSample

object TmdbMovieImagesJson {

    val Inception = """
        {
            "${Response.MovieId}": "${ScreenplayKeywordsSample.Inception.movieId.value}",
            "${Response.Backdrops}": [
                {
                    "${Response.FilePath}": "${ScreenplayImagesSample.Inception.posters[0].path}"
                },
                {
                    "${Response.FilePath}": "${ScreenplayImagesSample.Inception.posters[1].path}"
                },
                {
                    "${Response.FilePath}": "${ScreenplayImagesSample.Inception.posters[2].path}"
                }
            ],
            "${Response.Posters}": [
                {
                    "${Response.FilePath}": "${ScreenplayImagesSample.Inception.backdrops[0].path}"
                },
                {
                    "${Response.FilePath}": "${ScreenplayImagesSample.Inception.backdrops[1].path}"
                },
                {
                    "${Response.FilePath}": "${ScreenplayImagesSample.Inception.backdrops[2].path}"
                }
            ]
        }
    """.trimIndent()

    val TheWolfOfWallStreet = """
        {
            "${Response.MovieId}": "${ScreenplayKeywordsSample.TheWolfOfWallStreet.movieId.value}",
            "${Response.Backdrops}": [
                {
                    "${Response.FilePath}": "${ScreenplayImagesSample.TheWolfOfWallStreet.posters[0].path}"
                },
                {
                    "${Response.FilePath}": "${ScreenplayImagesSample.TheWolfOfWallStreet.posters[1].path}"
                },
                {
                    "${Response.FilePath}": "${ScreenplayImagesSample.TheWolfOfWallStreet.posters[2].path}"
                }
            ],
            "${Response.Posters}": [
                {
                    "${Response.FilePath}": "${ScreenplayImagesSample.TheWolfOfWallStreet.backdrops[0].path}"
                },
                {
                    "${Response.FilePath}": "${ScreenplayImagesSample.TheWolfOfWallStreet.backdrops[1].path}"
                },
                {
                    "${Response.FilePath}": "${ScreenplayImagesSample.TheWolfOfWallStreet.backdrops[2].path}"
                }
            ]
        }
    """.trimIndent()

    val War = """
        {
            "${Response.MovieId}": "${ScreenplayKeywordsSample.War.movieId.value}",
            "${Response.Backdrops}": [
                {
                    "${Response.FilePath}": "${ScreenplayImagesSample.War.posters[0].path}"
                },
                {
                    "${Response.FilePath}": "${ScreenplayImagesSample.War.posters[1].path}"
                },
                {
                    "${Response.FilePath}": "${ScreenplayImagesSample.War.posters[2].path}"
                }
            ],
            "${Response.Posters}": [
                {
                    "${Response.FilePath}": "${ScreenplayImagesSample.War.backdrops[0].path}"
                },
                {
                    "${Response.FilePath}": "${ScreenplayImagesSample.War.backdrops[1].path}"
                },
                {
                    "${Response.FilePath}": "${ScreenplayImagesSample.War.backdrops[2].path}"
                }
            ]
        }
    """.trimIndent()
}
