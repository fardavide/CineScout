package screenplay.data.remote.trakt.test

import cinescout.screenplay.domain.sample.ScreenplaySample
import cinescout.screenplay.domain.sample.TmdbScreenplayIdSample
import screenplay.data.remote.trakt.model.TraktTvShowIds
import screenplay.data.remote.trakt.model.TraktTvShowMetadataBody

object TraktRecommendedTvShowsJson {

    val OneTvShow = """
        [
            {
                "${TraktTvShowMetadataBody.Ids}": {
                    "${TraktTvShowIds.Tmdb}": ${TmdbScreenplayIdSample.BreakingBad.value}
                },
                "${TraktTvShowMetadataBody.Title}": "${ScreenplaySample.BreakingBad.title}"
            }
        ]
    """.trimIndent()

    val TwoTvShows = """
        [
            {
                "${TraktTvShowMetadataBody.Ids}": {
                    "${TraktTvShowIds.Tmdb}": ${TmdbScreenplayIdSample.BreakingBad.value}
                },
                "${TraktTvShowMetadataBody.Title}": "${ScreenplaySample.BreakingBad.title}"
            },
            {
                "${TraktTvShowMetadataBody.Ids}": {
                    "${TraktTvShowIds.Tmdb}": ${TmdbScreenplayIdSample.Dexter.value}
                },
                "${TraktTvShowMetadataBody.Title}": "${ScreenplaySample.Dexter.title}"
            }
        ]
    """.trimIndent()

    val ThreeTvShows = """
        [
            {
                "${TraktTvShowMetadataBody.Ids}": {
                    "${TraktTvShowIds.Tmdb}": ${TmdbScreenplayIdSample.BreakingBad.value}
                },
                "${TraktTvShowMetadataBody.Title}": "${ScreenplaySample.BreakingBad.title}"
            },
            {
                "${TraktTvShowMetadataBody.Ids}": {
                    "${TraktTvShowIds.Tmdb}": ${TmdbScreenplayIdSample.Dexter.value}
                },
                "${TraktTvShowMetadataBody.Title}": "${ScreenplaySample.Dexter.title}"
            },
            {
                "${TraktTvShowMetadataBody.Ids}": {
                    "${TraktTvShowIds.Tmdb}": ${TmdbScreenplayIdSample.Grimm.value}
                },
                "${TraktTvShowMetadataBody.Title}": "${ScreenplaySample.Grimm.title}"
            }
        ]
    """.trimIndent()
}
