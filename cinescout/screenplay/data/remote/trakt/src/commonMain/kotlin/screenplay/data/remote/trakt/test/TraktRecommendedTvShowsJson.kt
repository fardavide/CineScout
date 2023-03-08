package screenplay.data.remote.trakt.test

import cinescout.screenplay.domain.sample.ScreenplaySample
import cinescout.screenplay.domain.sample.TmdbScreenplayIdSample
import screenplay.data.remote.trakt.model.TraktTvShowBody

object TraktRecommendedTvShowsJson {

    val OneTvShow = """
        [
            {
                "${TraktTvShowBody.Ids}": {
                    "${TraktTvShowBody.Ids.Tmdb}": ${TmdbScreenplayIdSample.BreakingBad.value}
                },
                "${TraktTvShowBody.Title}": "${ScreenplaySample.BreakingBad.title}"
            }
        ]
    """.trimIndent()

    val TwoTvShows = """
        [
            {
                "${TraktTvShowBody.Ids}": {
                    "${TraktTvShowBody.Ids.Tmdb}": ${TmdbScreenplayIdSample.BreakingBad.value}
                },
                "${TraktTvShowBody.Title}": "${ScreenplaySample.BreakingBad.title}"
            },
            {
                "${TraktTvShowBody.Ids}": {
                    "${TraktTvShowBody.Ids.Tmdb}": ${TmdbScreenplayIdSample.Dexter.value}
                },
                "${TraktTvShowBody.Title}": "${ScreenplaySample.Dexter.title}"
            }
        ]
    """.trimIndent()

    val ThreeTvShows = """
        [
            {
                "${TraktTvShowBody.Ids}": {
                    "${TraktTvShowBody.Ids.Tmdb}": ${TmdbScreenplayIdSample.BreakingBad.value}
                },
                "${TraktTvShowBody.Title}": "${ScreenplaySample.BreakingBad.title}"
            },
            {
                "${TraktTvShowBody.Ids}": {
                    "${TraktTvShowBody.Ids.Tmdb}": ${TmdbScreenplayIdSample.Dexter.value}
                },
                "${TraktTvShowBody.Title}": "${ScreenplaySample.Dexter.title}"
            },
            {
                "${TraktTvShowBody.Ids}": {
                    "${TraktTvShowBody.Ids.Tmdb}": ${TmdbScreenplayIdSample.Grimm.value}
                },
                "${TraktTvShowBody.Title}": "${ScreenplaySample.Grimm.title}"
            }
        ]
    """.trimIndent()
}
