package cinescout.media.data.remote.res

import cinescout.media.data.remote.model.ScreenplayImagesBody
import cinescout.media.domain.sample.ScreenplayImagesSample
import cinescout.screenplay.domain.sample.TmdbScreenplayIdSample
import cinescout.media.data.remote.model.GetScreenplayImagesResponse as Response

object TmdbScreenplayImagesJson {

    val BreakingBad = """
        {
            "${Response.Id}": "${TmdbScreenplayIdSample.BreakingBad.value}",
            "${Response.Images}": {
                "${ScreenplayImagesBody.Backdrops}": [
                    { "${ScreenplayImagesBody.FilePath}": "${ScreenplayImagesSample.BreakingBad.backdrops[0].path}" },
                    { "${ScreenplayImagesBody.FilePath}": "${ScreenplayImagesSample.BreakingBad.backdrops[1].path}" },
                    { "${ScreenplayImagesBody.FilePath}": "${ScreenplayImagesSample.BreakingBad.backdrops[2].path}" }
                ],
                "${ScreenplayImagesBody.Posters}": [
                    { "${ScreenplayImagesBody.FilePath}": "${ScreenplayImagesSample.BreakingBad.posters[0].path}" },
                    { "${ScreenplayImagesBody.FilePath}": "${ScreenplayImagesSample.BreakingBad.posters[1].path}" },
                    { "${ScreenplayImagesBody.FilePath}": "${ScreenplayImagesSample.BreakingBad.posters[2].path}" }
                ]
            }
        }
    """.trimIndent()

    val Dexter = """
        {
            "${Response.BackdropPath}": "${ScreenplayImagesSample.Dexter.backdrops[0].path}",
            "${Response.Id}": "${TmdbScreenplayIdSample.Dexter.value}",
            "${Response.Images}": {
                "${ScreenplayImagesBody.Backdrops}": [
                    { "${ScreenplayImagesBody.FilePath}": "${ScreenplayImagesSample.Dexter.backdrops[0].path}" },
                    { "${ScreenplayImagesBody.FilePath}": "${ScreenplayImagesSample.Dexter.backdrops[1].path}" },
                    { "${ScreenplayImagesBody.FilePath}": "${ScreenplayImagesSample.Dexter.backdrops[2].path}" }
                ],
                "${ScreenplayImagesBody.Posters}": [
                    { "${ScreenplayImagesBody.FilePath}": "${ScreenplayImagesSample.Dexter.posters[0].path}" },
                    { "${ScreenplayImagesBody.FilePath}": "${ScreenplayImagesSample.Dexter.posters[1].path}" },
                    { "${ScreenplayImagesBody.FilePath}": "${ScreenplayImagesSample.Dexter.posters[2].path}" }
                ]
            },
            "${Response.PosterPath}": "${ScreenplayImagesSample.Dexter.posters[0].path}"
        }
    """.trimIndent()

    val Grimm = """
        {
            "${Response.Id}": "${TmdbScreenplayIdSample.Grimm.value}",
            "${Response.Images}": {
                "${ScreenplayImagesBody.Backdrops}": [
                    { "${ScreenplayImagesBody.FilePath}": "${ScreenplayImagesSample.Grimm.backdrops[0].path}" },
                    { "${ScreenplayImagesBody.FilePath}": "${ScreenplayImagesSample.Grimm.backdrops[1].path}" },
                    { "${ScreenplayImagesBody.FilePath}": "${ScreenplayImagesSample.Grimm.backdrops[2].path}" }
                ]
            }
        }
    """.trimIndent()

    val Inception = """
        {
            "${Response.BackdropPath}": "${ScreenplayImagesSample.Inception.backdrops[0].path}",
            "${Response.Id}": "${TmdbScreenplayIdSample.Inception.value}",
            "${Response.Images}": {
                "${ScreenplayImagesBody.Backdrops}": [
                    { "${ScreenplayImagesBody.FilePath}": "${ScreenplayImagesSample.Inception.backdrops[0].path}" },
                    { "${ScreenplayImagesBody.FilePath}": "${ScreenplayImagesSample.Inception.backdrops[1].path}" },
                    { "${ScreenplayImagesBody.FilePath}": "${ScreenplayImagesSample.Inception.backdrops[2].path}" }
                ],
                "${ScreenplayImagesBody.Posters}": [
                    { "${ScreenplayImagesBody.FilePath}": "${ScreenplayImagesSample.Inception.posters[0].path}" },
                    { "${ScreenplayImagesBody.FilePath}": "${ScreenplayImagesSample.Inception.posters[1].path}" },
                    { "${ScreenplayImagesBody.FilePath}": "${ScreenplayImagesSample.Inception.posters[2].path}" }
                ]
            },
            "${Response.PosterPath}": "${ScreenplayImagesSample.Inception.posters[0].path}"
        }
    """.trimIndent()

    val TheWolfOfWallStreet = """
        {
            "${Response.Id}": "${TmdbScreenplayIdSample.TheWolfOfWallStreet.value}",
            "${Response.Images}": {
                "${ScreenplayImagesBody.Backdrops}": [
                    { "${ScreenplayImagesBody.FilePath}": 
                        "${ScreenplayImagesSample.TheWolfOfWallStreet.backdrops[0].path}" },
                    { "${ScreenplayImagesBody.FilePath}": 
                        "${ScreenplayImagesSample.TheWolfOfWallStreet.backdrops[1].path}" },
                    { "${ScreenplayImagesBody.FilePath}": 
                        "${ScreenplayImagesSample.TheWolfOfWallStreet.backdrops[2].path}" }
                ]
            }
        }
    """.trimIndent()

    val War = """
        {
            "${Response.Id}": "${TmdbScreenplayIdSample.War.value}",
            "${Response.Images}": {
                "${ScreenplayImagesBody.Backdrops}": [
                    { "${ScreenplayImagesBody.FilePath}": "${ScreenplayImagesSample.War.backdrops[0].path}" },
                    { "${ScreenplayImagesBody.FilePath}": "${ScreenplayImagesSample.War.backdrops[1].path}" },
                    { "${ScreenplayImagesBody.FilePath}": "${ScreenplayImagesSample.War.backdrops[2].path}" }
                ]
            }
        }
    """.trimIndent()
}
