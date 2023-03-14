package cinescout.media.data.remote.res

import cinescout.media.domain.sample.ScreenplayImagesSample
import cinescout.screenplay.domain.sample.TmdbScreenplayIdSample
import cinescout.media.data.remote.model.GetScreenplayImagesResponse as Response

object TmdbScreenplayImagesJson {

    val BreakingBad = """
        {
            "${Response.Id}": "${TmdbScreenplayIdSample.BreakingBad.value}",
            "${Response.Backdrops}": [
                { "${Response.FilePath}": "${ScreenplayImagesSample.BreakingBad.backdrops[0].path}" },
                { "${Response.FilePath}": "${ScreenplayImagesSample.BreakingBad.backdrops[1].path}" },
                { "${Response.FilePath}": "${ScreenplayImagesSample.BreakingBad.backdrops[2].path}" }
            ],
            "${Response.Posters}": [
                { "${Response.FilePath}": "${ScreenplayImagesSample.BreakingBad.posters[0].path}" },
                { "${Response.FilePath}": "${ScreenplayImagesSample.BreakingBad.posters[1].path}" },
                { "${Response.FilePath}": "${ScreenplayImagesSample.BreakingBad.posters[2].path}" }
            ]
        }
    """.trimIndent()

    val Dexter = """
        {
            "${Response.Id}": "${TmdbScreenplayIdSample.Dexter.value}",
            "${Response.Backdrops}": [
                { "${Response.FilePath}": "${ScreenplayImagesSample.Dexter.backdrops[0].path}" },
                { "${Response.FilePath}": "${ScreenplayImagesSample.Dexter.backdrops[1].path}" },
                { "${Response.FilePath}": "${ScreenplayImagesSample.Dexter.backdrops[2].path}" }
            ]
        }
    """.trimIndent()

    val Grimm = """
        {
            "${Response.Id}": "${TmdbScreenplayIdSample.Grimm.value}",
            "${Response.Backdrops}": [
                { "${Response.FilePath}": "${ScreenplayImagesSample.Grimm.backdrops[0].path}" },
                { "${Response.FilePath}": "${ScreenplayImagesSample.Grimm.backdrops[1].path}" },
                { "${Response.FilePath}": "${ScreenplayImagesSample.Grimm.backdrops[2].path}" }
            ]
        }
    """.trimIndent()

    val Inception = """
        {
            "${Response.Id}": "${TmdbScreenplayIdSample.Inception.value}",
            "${Response.Backdrops}": [
                { "${Response.FilePath}": "${ScreenplayImagesSample.Inception.backdrops[0].path}" },
                { "${Response.FilePath}": "${ScreenplayImagesSample.Inception.backdrops[1].path}" },
                { "${Response.FilePath}": "${ScreenplayImagesSample.Inception.backdrops[2].path}" }
            ],
            "${Response.Posters}": [
                { "${Response.FilePath}": "${ScreenplayImagesSample.Inception.posters[0].path}" },
                { "${Response.FilePath}": "${ScreenplayImagesSample.Inception.posters[1].path}" },
                { "${Response.FilePath}": "${ScreenplayImagesSample.Inception.posters[2].path}" }
            ]
        }
    """.trimIndent()

    val TheWolfOfWallStreet = """
        {
            "${Response.Id}": "${TmdbScreenplayIdSample.TheWolfOfWallStreet.value}",
            "${Response.Backdrops}": [
                { "${Response.FilePath}": "${ScreenplayImagesSample.TheWolfOfWallStreet.backdrops[0].path}" },
                { "${Response.FilePath}": "${ScreenplayImagesSample.TheWolfOfWallStreet.backdrops[1].path}" },
                { "${Response.FilePath}": "${ScreenplayImagesSample.TheWolfOfWallStreet.backdrops[2].path}" }
            ]
        }
    """.trimIndent()

    val War = """
        {
            "${Response.Id}": "${TmdbScreenplayIdSample.War.value}",
            "${Response.Backdrops}": [
                { "${Response.FilePath}": "${ScreenplayImagesSample.War.backdrops[0].path}" },
                { "${Response.FilePath}": "${ScreenplayImagesSample.War.backdrops[1].path}" },
                { "${Response.FilePath}": "${ScreenplayImagesSample.War.backdrops[2].path}" }
            ]
        }
    """.trimIndent()
}
