package cinescout.rating.data.remote.res

import cinescout.rating.data.remote.model.Rating
import cinescout.rating.data.remote.sample.TraktRatingMetadataBodySample
import cinescout.screenplay.domain.sample.ScreenplaySample
import screenplay.data.remote.trakt.model.TraktScreenplay
import screenplay.data.remote.trakt.model.TraktScreenplayIds
import screenplay.data.remote.trakt.model.TraktScreenplayType

object TraktRatingMetadataJson {

    val OneMovie = """
        [
            {
                "$Rating": ${TraktRatingMetadataBodySample.Inception.rating},
                "${TraktScreenplayType.Movie}": {
                    "${TraktScreenplay.Ids}": {
                        "${TraktScreenplayIds.Tmdb}": ${ScreenplaySample.Inception.tmdbId.value},
                        "${TraktScreenplayIds.Trakt}": "${ScreenplaySample.Inception.traktId.value}"
                    }
                }
            }
        ]
    """.trimIndent()

    val OneMovieAndOneTvShow = """
        [
            {
                "$Rating": ${TraktRatingMetadataBodySample.Inception.rating},
                "${TraktScreenplayType.Movie}": {
                    "${TraktScreenplay.Ids}": {
                        "${TraktScreenplayIds.Tmdb}": ${ScreenplaySample.Inception.tmdbId.value},
                        "${TraktScreenplayIds.Trakt}": "${ScreenplaySample.Inception.traktId.value}"
                    }
                }
            },
            {
                "$Rating": ${TraktRatingMetadataBodySample.BreakingBad.rating},
                "${TraktScreenplayType.TvShow}": {
                    "${TraktScreenplay.Ids}": {
                        "${TraktScreenplayIds.Tmdb}": ${ScreenplaySample.BreakingBad.tmdbId.value},
                        "${TraktScreenplayIds.Trakt}": "${ScreenplaySample.BreakingBad.traktId.value}"
                    }
                }
            }
        ]
    """.trimIndent()

    val OneTvShow = """
        [
            {
                "$Rating": ${TraktRatingMetadataBodySample.BreakingBad.rating},
                "${TraktScreenplayType.TvShow}": {
                    "${TraktScreenplay.Ids}": {
                        "${TraktScreenplayIds.Tmdb}": ${ScreenplaySample.BreakingBad.tmdbId.value},
                        "${TraktScreenplayIds.Trakt}": "${ScreenplaySample.BreakingBad.traktId.value}"
                    }
                }
            }
        ]
    """.trimIndent()
}
