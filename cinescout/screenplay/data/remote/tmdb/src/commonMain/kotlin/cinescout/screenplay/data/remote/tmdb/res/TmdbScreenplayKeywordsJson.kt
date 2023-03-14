package cinescout.screenplay.data.remote.tmdb.res

import cinescout.screenplay.data.remote.tmdb.model.TmdbScreenplay
import cinescout.screenplay.domain.sample.ScreenplayKeywordsSample
import cinescout.screenplay.domain.sample.TmdbScreenplayIdSample
import cinescout.screenplay.data.remote.tmdb.model.GetScreenplayKeywordsResponse as Response

object TmdbScreenplayKeywordsJson {

    val BreakingBad = """
        {
            "${TmdbScreenplay.Id}": "${TmdbScreenplayIdSample.BreakingBad.value}",
            "${Response.Results}": [
                {
                    "${Response.Keyword.Id}": "${ScreenplayKeywordsSample.BreakingBad.keywords[0].id.value}",
                    "${Response.Keyword.Name}": "${ScreenplayKeywordsSample.BreakingBad.keywords[0].name}"
                },
                {
                    "${Response.Keyword.Id}": "${ScreenplayKeywordsSample.BreakingBad.keywords[1].id.value}",
                    "${Response.Keyword.Name}": "${ScreenplayKeywordsSample.BreakingBad.keywords[1].name}"
                },
                {
                    "${Response.Keyword.Id}": "${ScreenplayKeywordsSample.BreakingBad.keywords[2].id.value}",
                    "${Response.Keyword.Name}": "${ScreenplayKeywordsSample.BreakingBad.keywords[2].name}"
                }
            ]
        }
    """.trimIndent()

    val Dexter = """
        {
            "${TmdbScreenplay.Id}": "${TmdbScreenplayIdSample.Dexter.value}",
            "${Response.Results}": [
                {
                    "${Response.Keyword.Id}": "${ScreenplayKeywordsSample.Dexter.keywords[0].id.value}",
                    "${Response.Keyword.Name}": "${ScreenplayKeywordsSample.Dexter.keywords[0].name}"
                },
                {
                    "${Response.Keyword.Id}": "${ScreenplayKeywordsSample.Dexter.keywords[1].id.value}",
                    "${Response.Keyword.Name}": "${ScreenplayKeywordsSample.Dexter.keywords[1].name}"
                },
                {
                    "${Response.Keyword.Id}": "${ScreenplayKeywordsSample.Dexter.keywords[2].id.value}",
                    "${Response.Keyword.Name}": "${ScreenplayKeywordsSample.Dexter.keywords[2].name}"
                }
            ]
        }
    """.trimIndent()

    val Grimm = """
        {
            "${TmdbScreenplay.Id}": "${TmdbScreenplayIdSample.Grimm.value}",
            "${Response.Results}": [
                {
                    "${Response.Keyword.Id}": "${ScreenplayKeywordsSample.Grimm.keywords[0].id.value}",
                    "${Response.Keyword.Name}": "${ScreenplayKeywordsSample.Grimm.keywords[0].name}"
                },
                {
                    "${Response.Keyword.Id}": "${ScreenplayKeywordsSample.Grimm.keywords[1].id.value}",
                    "${Response.Keyword.Name}": "${ScreenplayKeywordsSample.Grimm.keywords[1].name}"
                },
                {
                    "${Response.Keyword.Id}": "${ScreenplayKeywordsSample.Grimm.keywords[2].id.value}",
                    "${Response.Keyword.Name}": "${ScreenplayKeywordsSample.Grimm.keywords[2].name}"
                }
            ]
        }
    """.trimIndent()

    val Inception = """
        {
            "${TmdbScreenplay.Id}": "${TmdbScreenplayIdSample.Inception.value}",
            "${Response.Results}": [
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
            "${TmdbScreenplay.Id}": "${TmdbScreenplayIdSample.TheWolfOfWallStreet.value}",
            "${Response.Results}": [
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
            "${TmdbScreenplay.Id}": "${TmdbScreenplayIdSample.War.value}",
            "${Response.Results}": [
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
