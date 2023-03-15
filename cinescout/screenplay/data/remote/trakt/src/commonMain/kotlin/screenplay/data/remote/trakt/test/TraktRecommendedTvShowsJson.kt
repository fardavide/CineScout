package screenplay.data.remote.trakt.test

import cinescout.screenplay.domain.sample.TmdbScreenplayIdSample
import screenplay.data.remote.trakt.model.TraktScreenplay
import screenplay.data.remote.trakt.model.TraktTvShowIds

object TraktRecommendedTvShowsJson {

    val OneTvShow = """
        [
            {
                "${TraktScreenplay.Ids}": {
                    "${TraktTvShowIds.Tmdb}": ${TmdbScreenplayIdSample.BreakingBad.value}
                }
            }
        ]
    """.trimIndent()

    val TwoTvShows = """
        [
            {
                "${TraktScreenplay.Ids}": {
                    "${TraktTvShowIds.Tmdb}": ${TmdbScreenplayIdSample.BreakingBad.value}
                }
            },
            {
                "${TraktScreenplay.Ids}": {
                    "${TraktTvShowIds.Tmdb}": ${TmdbScreenplayIdSample.Dexter.value}
                }
            }
        ]
    """.trimIndent()

    val ThreeTvShows = """
        [
            {
                "${TraktScreenplay.Ids}": {
                    "${TraktTvShowIds.Tmdb}": ${TmdbScreenplayIdSample.BreakingBad.value}
                }
            },
            {
                "${TraktScreenplay.Ids}": {
                    "${TraktTvShowIds.Tmdb}": ${TmdbScreenplayIdSample.Dexter.value}
                }
            },
            {
                "${TraktScreenplay.Ids}": {
                    "${TraktTvShowIds.Tmdb}": ${TmdbScreenplayIdSample.Grimm.value}
                }
            }
        ]
    """.trimIndent()
}
