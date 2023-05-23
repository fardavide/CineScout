package screenplay.data.remote.trakt.res

import cinescout.screenplay.domain.sample.TmdbScreenplayIdSample
import cinescout.screenplay.domain.sample.TraktScreenplayIdSample
import screenplay.data.remote.trakt.model.TraktScreenplay
import screenplay.data.remote.trakt.model.TraktScreenplayIds

object TraktRecommendationsJson {

    val ThreeMovies = """
        [
            {
                "${TraktScreenplay.Ids}": {
                    "${TraktScreenplayIds.Tmdb}": ${TmdbScreenplayIdSample.Inception.value},
                    "${TraktScreenplayIds.Trakt}": ${TraktScreenplayIdSample.Inception.value}
                }
            },
            {
                "${TraktScreenplay.Ids}": {
                    "${TraktScreenplayIds.Tmdb}": ${TmdbScreenplayIdSample.TheWolfOfWallStreet.value},
                    "${TraktScreenplayIds.Trakt}": ${TraktScreenplayIdSample.TheWolfOfWallStreet.value}
                }
            },
            {
                "${TraktScreenplay.Ids}": {
                    "${TraktScreenplayIds.Tmdb}": ${TmdbScreenplayIdSample.War.value},
                    "${TraktScreenplayIds.Trakt}": ${TraktScreenplayIdSample.War.value}
                }
            }
        ]
    """.trimIndent()

    val ThreeMoviesAndThreeTvShows = """
        [
            {
                "${TraktScreenplay.Ids}": {
                    "${TraktScreenplayIds.Tmdb}": ${TmdbScreenplayIdSample.Inception.value},
                    "${TraktScreenplayIds.Trakt}": ${TraktScreenplayIdSample.Inception.value}
                }
            },
            {
                "${TraktScreenplay.Ids}": {
                    "${TraktScreenplayIds.Tmdb}": ${TmdbScreenplayIdSample.TheWolfOfWallStreet.value},
                    "${TraktScreenplayIds.Trakt}": ${TraktScreenplayIdSample.TheWolfOfWallStreet.value}
                }
            },
            {
                "${TraktScreenplay.Ids}": {
                    "${TraktScreenplayIds.Tmdb}": ${TmdbScreenplayIdSample.War.value},
                    "${TraktScreenplayIds.Trakt}": ${TraktScreenplayIdSample.War.value}
                }
            },
            {
                "${TraktScreenplay.Ids}": {
                    "${TraktScreenplayIds.Tmdb}": ${TmdbScreenplayIdSample.BreakingBad.value},
                    "${TraktScreenplayIds.Trakt}": ${TraktScreenplayIdSample.BreakingBad.value}
                }
            },
            {
                "${TraktScreenplay.Ids}": {
                    "${TraktScreenplayIds.Tmdb}": ${TmdbScreenplayIdSample.Dexter.value},
                    "${TraktScreenplayIds.Trakt}": ${TraktScreenplayIdSample.Dexter.value}
                }
            },
            {
                "${TraktScreenplay.Ids}": {
                    "${TraktScreenplayIds.Tmdb}": ${TmdbScreenplayIdSample.Grimm.value},
                    "${TraktScreenplayIds.Trakt}": ${TraktScreenplayIdSample.Grimm.value}
                }
            }
        ]
    """.trimIndent()

    val ThreeTvShows = """
        [
            {
                "${TraktScreenplay.Ids}": {
                    "${TraktScreenplayIds.Tmdb}": ${TmdbScreenplayIdSample.BreakingBad.value},
                    "${TraktScreenplayIds.Trakt}": ${TraktScreenplayIdSample.BreakingBad.value}
                }
            },
            {
                "${TraktScreenplay.Ids}": {
                    "${TraktScreenplayIds.Tmdb}": ${TmdbScreenplayIdSample.Dexter.value},
                    "${TraktScreenplayIds.Trakt}": ${TraktScreenplayIdSample.Dexter.value}
                }
            },
            {
                "${TraktScreenplay.Ids}": {
                    "${TraktScreenplayIds.Tmdb}": ${TmdbScreenplayIdSample.Grimm.value},
                    "${TraktScreenplayIds.Trakt}": ${TraktScreenplayIdSample.Grimm.value}
                }
            }
        ]
    """.trimIndent()
}
