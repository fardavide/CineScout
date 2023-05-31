package screenplay.data.remote.trakt.res

import cinescout.screenplay.domain.sample.TmdbScreenplayIdSample
import cinescout.screenplay.domain.sample.TraktScreenplayIdSample
import screenplay.data.remote.trakt.model.TraktContentIds
import screenplay.data.remote.trakt.model.TraktScreenplay

object TraktRecommendationsJson {

    val ThreeMovies = """
        [
            {
                "${TraktScreenplay.Ids}": {
                    "${TraktContentIds.Tmdb}": ${TmdbScreenplayIdSample.Inception.value},
                    "${TraktContentIds.Trakt}": ${TraktScreenplayIdSample.Inception.value}
                }
            },
            {
                "${TraktScreenplay.Ids}": {
                    "${TraktContentIds.Tmdb}": ${TmdbScreenplayIdSample.TheWolfOfWallStreet.value},
                    "${TraktContentIds.Trakt}": ${TraktScreenplayIdSample.TheWolfOfWallStreet.value}
                }
            },
            {
                "${TraktScreenplay.Ids}": {
                    "${TraktContentIds.Tmdb}": ${TmdbScreenplayIdSample.War.value},
                    "${TraktContentIds.Trakt}": ${TraktScreenplayIdSample.War.value}
                }
            }
        ]
    """.trimIndent()

    val ThreeMoviesAndThreeTvShows = """
        [
            {
                "${TraktScreenplay.Ids}": {
                    "${TraktContentIds.Tmdb}": ${TmdbScreenplayIdSample.Inception.value},
                    "${TraktContentIds.Trakt}": ${TraktScreenplayIdSample.Inception.value}
                }
            },
            {
                "${TraktScreenplay.Ids}": {
                    "${TraktContentIds.Tmdb}": ${TmdbScreenplayIdSample.TheWolfOfWallStreet.value},
                    "${TraktContentIds.Trakt}": ${TraktScreenplayIdSample.TheWolfOfWallStreet.value}
                }
            },
            {
                "${TraktScreenplay.Ids}": {
                    "${TraktContentIds.Tmdb}": ${TmdbScreenplayIdSample.War.value},
                    "${TraktContentIds.Trakt}": ${TraktScreenplayIdSample.War.value}
                }
            },
            {
                "${TraktScreenplay.Ids}": {
                    "${TraktContentIds.Tmdb}": ${TmdbScreenplayIdSample.BreakingBad.value},
                    "${TraktContentIds.Trakt}": ${TraktScreenplayIdSample.BreakingBad.value}
                }
            },
            {
                "${TraktScreenplay.Ids}": {
                    "${TraktContentIds.Tmdb}": ${TmdbScreenplayIdSample.Dexter.value},
                    "${TraktContentIds.Trakt}": ${TraktScreenplayIdSample.Dexter.value}
                }
            },
            {
                "${TraktScreenplay.Ids}": {
                    "${TraktContentIds.Tmdb}": ${TmdbScreenplayIdSample.Grimm.value},
                    "${TraktContentIds.Trakt}": ${TraktScreenplayIdSample.Grimm.value}
                }
            }
        ]
    """.trimIndent()

    val ThreeTvShows = """
        [
            {
                "${TraktScreenplay.Ids}": {
                    "${TraktContentIds.Tmdb}": ${TmdbScreenplayIdSample.BreakingBad.value},
                    "${TraktContentIds.Trakt}": ${TraktScreenplayIdSample.BreakingBad.value}
                }
            },
            {
                "${TraktScreenplay.Ids}": {
                    "${TraktContentIds.Tmdb}": ${TmdbScreenplayIdSample.Dexter.value},
                    "${TraktContentIds.Trakt}": ${TraktScreenplayIdSample.Dexter.value}
                }
            },
            {
                "${TraktScreenplay.Ids}": {
                    "${TraktContentIds.Tmdb}": ${TmdbScreenplayIdSample.Grimm.value},
                    "${TraktContentIds.Trakt}": ${TraktScreenplayIdSample.Grimm.value}
                }
            }
        ]
    """.trimIndent()
}
