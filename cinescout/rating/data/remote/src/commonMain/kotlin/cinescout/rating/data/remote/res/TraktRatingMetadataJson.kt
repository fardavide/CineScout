package cinescout.rating.data.remote.res

import cinescout.rating.data.remote.model.Rating
import cinescout.rating.data.remote.sample.TraktRatingMetadataBodySample
import cinescout.screenplay.domain.sample.ScreenplaySample
import screenplay.data.remote.trakt.model.TraktMovieIds
import screenplay.data.remote.trakt.model.TraktScreenplay
import screenplay.data.remote.trakt.model.TraktScreenplayType
import screenplay.data.remote.trakt.model.TraktTvShowIds

object TraktRatingMetadataJson {

    val OneMovie = """
        [
            {
                "$Rating": ${TraktRatingMetadataBodySample.Inception.rating},
                "${TraktScreenplayType.Movie}": {
                    "${TraktScreenplay.Ids}": {
                        "${TraktMovieIds.Tmdb}": ${ScreenplaySample.Inception.tmdbId.value},
                        "${TraktMovieIds.Trakt}": "${ScreenplaySample.Inception.traktId.value}"
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
                        "${TraktMovieIds.Tmdb}": ${ScreenplaySample.Inception.tmdbId.value},
                        "${TraktMovieIds.Trakt}": "${ScreenplaySample.Inception.traktId.value}"
                    }
                }
            },
            {
                "$Rating": ${TraktRatingMetadataBodySample.BreakingBad.rating},
                "${TraktScreenplayType.TvShow}": {
                    "${TraktScreenplay.Ids}": {
                        "${TraktTvShowIds.Tmdb}": ${ScreenplaySample.BreakingBad.tmdbId.value},
                        "${TraktTvShowIds.Trakt}": "${ScreenplaySample.BreakingBad.traktId.value}"
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
                        "${TraktTvShowIds.Tmdb}": ${ScreenplaySample.BreakingBad.tmdbId.value},
                        "${TraktTvShowIds.Trakt}": "${ScreenplaySample.BreakingBad.traktId.value}"
                    }
                }
            }
        ]
    """.trimIndent()
}
