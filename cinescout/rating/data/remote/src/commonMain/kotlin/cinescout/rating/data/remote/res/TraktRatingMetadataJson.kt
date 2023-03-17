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
                        "${TraktMovieIds.Tmdb}": ${ScreenplaySample.Inception.tmdbId.value}
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
                        "${TraktMovieIds.Tmdb}": ${ScreenplaySample.Inception.tmdbId.value}
                    }
                }
            },
            {
                "$Rating": ${TraktRatingMetadataBodySample.BreakingBad.rating},
                "${TraktScreenplayType.TvShow}": {
                    "${TraktScreenplay.Ids}": {
                        "${TraktTvShowIds.Tmdb}": ${ScreenplaySample.BreakingBad.tmdbId.value}
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
                        "${TraktTvShowIds.Tmdb}": ${ScreenplaySample.BreakingBad.tmdbId.value}
                    }
                }
            }
        ]
    """.trimIndent()
}
