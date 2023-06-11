package screenplay.data.remote.trakt.res

import cinescout.screenplay.domain.sample.TmdbScreenplayIdSample
import cinescout.screenplay.domain.sample.TraktScreenplayIdSample
import screenplay.data.remote.trakt.model.TraktContent
import screenplay.data.remote.trakt.model.TraktContentIds

object TraktRecommendationsJson {

    val ThreeMovies = """
        [
            {
                "${TraktContent.Ids}": {
                    "${TraktContentIds.Tmdb}": ${TmdbScreenplayIdSample.Inception.value},
                    "${TraktContentIds.Trakt}": ${TraktScreenplayIdSample.Inception.value}
                }
            },
            {
                "${TraktContent.Ids}": {
                    "${TraktContentIds.Tmdb}": ${TmdbScreenplayIdSample.TheWolfOfWallStreet.value},
                    "${TraktContentIds.Trakt}": ${TraktScreenplayIdSample.TheWolfOfWallStreet.value}
                }
            },
            {
                "${TraktContent.Ids}": {
                    "${TraktContentIds.Tmdb}": ${TmdbScreenplayIdSample.War.value},
                    "${TraktContentIds.Trakt}": ${TraktScreenplayIdSample.War.value}
                }
            }
        ]
    """.trimIndent()

    val ThreeMoviesAndThreeTvShows = """
        [
            {
                "${TraktContent.Ids}": {
                    "${TraktContentIds.Tmdb}": ${TmdbScreenplayIdSample.Inception.value},
                    "${TraktContentIds.Trakt}": ${TraktScreenplayIdSample.Inception.value}
                }
            },
            {
                "${TraktContent.Ids}": {
                    "${TraktContentIds.Tmdb}": ${TmdbScreenplayIdSample.TheWolfOfWallStreet.value},
                    "${TraktContentIds.Trakt}": ${TraktScreenplayIdSample.TheWolfOfWallStreet.value}
                }
            },
            {
                "${TraktContent.Ids}": {
                    "${TraktContentIds.Tmdb}": ${TmdbScreenplayIdSample.War.value},
                    "${TraktContentIds.Trakt}": ${TraktScreenplayIdSample.War.value}
                }
            },
            {
                "${TraktContent.Ids}": {
                    "${TraktContentIds.Tmdb}": ${TmdbScreenplayIdSample.BreakingBad.value},
                    "${TraktContentIds.Trakt}": ${TraktScreenplayIdSample.BreakingBad.value}
                }
            },
            {
                "${TraktContent.Ids}": {
                    "${TraktContentIds.Tmdb}": ${TmdbScreenplayIdSample.Dexter.value},
                    "${TraktContentIds.Trakt}": ${TraktScreenplayIdSample.Dexter.value}
                }
            },
            {
                "${TraktContent.Ids}": {
                    "${TraktContentIds.Tmdb}": ${TmdbScreenplayIdSample.Grimm.value},
                    "${TraktContentIds.Trakt}": ${TraktScreenplayIdSample.Grimm.value}
                }
            }
        ]
    """.trimIndent()

    val ThreeTvShows = """
        [
            {
                "${TraktContent.Ids}": {
                    "${TraktContentIds.Tmdb}": ${TmdbScreenplayIdSample.BreakingBad.value},
                    "${TraktContentIds.Trakt}": ${TraktScreenplayIdSample.BreakingBad.value}
                }
            },
            {
                "${TraktContent.Ids}": {
                    "${TraktContentIds.Tmdb}": ${TmdbScreenplayIdSample.Dexter.value},
                    "${TraktContentIds.Trakt}": ${TraktScreenplayIdSample.Dexter.value}
                }
            },
            {
                "${TraktContent.Ids}": {
                    "${TraktContentIds.Tmdb}": ${TmdbScreenplayIdSample.Grimm.value},
                    "${TraktContentIds.Trakt}": ${TraktScreenplayIdSample.Grimm.value}
                }
            }
        ]
    """.trimIndent()
}
