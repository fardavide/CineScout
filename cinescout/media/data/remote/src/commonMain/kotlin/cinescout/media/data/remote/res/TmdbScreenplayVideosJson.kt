package cinescout.media.data.remote.res

import cinescout.media.domain.sample.ScreenplayVideosSample
import cinescout.screenplay.domain.sample.TmdbScreenplayIdSample
import cinescout.media.data.remote.model.GetScreenplayVideosResponse as Response

object TmdbScreenplayVideosJson {

    val BreakingBad = """
        {
            "${Response.Id}": "${TmdbScreenplayIdSample.BreakingBad.value}",
            "${Response.Results}": [
                {
                    "${Response.Video.Id}": "${ScreenplayVideosSample.BreakingBad.videos[0].id.value}",
                    "${Response.Video.Key}": "${ScreenplayVideosSample.BreakingBad.videos[0].key}",
                    "${Response.Video.Name}": "${ScreenplayVideosSample.BreakingBad.videos[0].title}",
                    "${Response.Video.Site}": "${ScreenplayVideosSample.BreakingBad.videos[0].site.name}",
                    "${Response.Video.Size}": "1080",
                    "${Response.Video.Type}": "${ScreenplayVideosSample.BreakingBad.videos[0].type.name}"
                }
            ]
        }
    """.trimIndent()

    val Dexter = """
        {
            "${Response.Id}": "${TmdbScreenplayIdSample.Dexter.value}",
            "${Response.Results}": [
                {
                    "${Response.Video.Id}": "${ScreenplayVideosSample.Dexter.videos[0].id.value}",
                    "${Response.Video.Key}": "${ScreenplayVideosSample.Dexter.videos[0].key}",
                    "${Response.Video.Name}": "${ScreenplayVideosSample.Dexter.videos[0].title}",
                    "${Response.Video.Site}": "${ScreenplayVideosSample.Dexter.videos[0].site.name}",
                    "${Response.Video.Size}": "1080",
                    "${Response.Video.Type}": "${ScreenplayVideosSample.Dexter.videos[0].type.name}"
                }
            ]
        }
    """.trimIndent()

    val Grimm = """
        {
            "${Response.Id}": "${TmdbScreenplayIdSample.Grimm.value}",
            "${Response.Results}": [
                {
                    "${Response.Video.Id}": "${ScreenplayVideosSample.Grimm.videos[0].id.value}",
                    "${Response.Video.Key}": "${ScreenplayVideosSample.Grimm.videos[0].key}",
                    "${Response.Video.Name}": "${ScreenplayVideosSample.Grimm.videos[0].title}",
                    "${Response.Video.Site}": "${ScreenplayVideosSample.Grimm.videos[0].site.name}",
                    "${Response.Video.Size}": "1080",
                    "${Response.Video.Type}": "${ScreenplayVideosSample.Grimm.videos[0].type.name}"
                }
            ]
        }
    """.trimIndent()

    val Inception = """
        {
            "${Response.Id}": "${TmdbScreenplayIdSample.Inception.value}",
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
            "${Response.Id}": "${TmdbScreenplayIdSample.TheWolfOfWallStreet.value}",
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
            "${Response.Id}": "${TmdbScreenplayIdSample.War.value}",
            "${Response.Results}": []
        }
    """.trimIndent()
}
