package cinescout.rating.data.remote.res

import cinescout.rating.data.remote.model.Rating
import cinescout.rating.data.remote.sample.TraktRatingMetadataBodySample
import cinescout.screenplay.domain.sample.ScreenplaySample
import screenplay.data.remote.trakt.model.TraktContent
import screenplay.data.remote.trakt.model.TraktContentIds
import screenplay.data.remote.trakt.model.TraktContentType

object TraktRatingMetadataJson {

    val OneMovie = """
        [
            {
                "$Rating": ${TraktRatingMetadataBodySample.Inception.rating},
                "${TraktContentType.Movie}": {
                    "${TraktContent.Ids}": {
                        "${TraktContentIds.Tmdb}": ${ScreenplaySample.Inception.tmdbId.value},
                        "${TraktContentIds.Trakt}": ${ScreenplaySample.Inception.traktId.value}
                    }
                }
            }
        ]
    """.trimIndent()

    val OneMovieAndOneTvShow = """
        [
            {
                "$Rating": ${TraktRatingMetadataBodySample.Inception.rating},
                "${TraktContentType.Movie}": {
                    "${TraktContent.Ids}": {
                        "${TraktContentIds.Tmdb}": ${ScreenplaySample.Inception.tmdbId.value},
                        "${TraktContentIds.Trakt}": ${ScreenplaySample.Inception.traktId.value}
                    }
                }
            },
            {
                "$Rating": ${TraktRatingMetadataBodySample.BreakingBad.rating},
                "${TraktContentType.TvShow}": {
                    "${TraktContent.Ids}": {
                        "${TraktContentIds.Tmdb}": ${ScreenplaySample.BreakingBad.tmdbId.value},
                        "${TraktContentIds.Trakt}": ${ScreenplaySample.BreakingBad.traktId.value}
                    }
                }
            }
        ]
    """.trimIndent()

    val OneTvShow = """
        [
            {
                "$Rating": ${TraktRatingMetadataBodySample.BreakingBad.rating},
                "${TraktContentType.TvShow}": {
                    "${TraktContent.Ids}": {
                        "${TraktContentIds.Tmdb}": ${ScreenplaySample.BreakingBad.tmdbId.value},
                        "${TraktContentIds.Trakt}": ${ScreenplaySample.BreakingBad.traktId.value}
                    }
                }
            }
        ]
    """.trimIndent()
}
