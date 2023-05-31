package cinescout.watchlist.data.remote.res

import cinescout.screenplay.domain.sample.ScreenplaySample
import screenplay.data.remote.trakt.model.TraktContentIds
import screenplay.data.remote.trakt.model.TraktScreenplay
import screenplay.data.remote.trakt.model.TraktScreenplayType

object TraktWatchlistMetadataJson {

    val OneMovie = """
        [
            {
                "${TraktScreenplayType.Movie}": {
                    "${TraktScreenplay.Ids}": {
                        "${TraktContentIds.Tmdb}": ${ScreenplaySample.Inception.tmdbId.value},
                        "${TraktContentIds.Trakt}": "${ScreenplaySample.Inception.traktId.value}"
                    }
                }
            }
        ]
    """.trimIndent()

    val OneMovieAndOneTvShow = """
        [
            {
                "${TraktScreenplayType.Movie}": {
                    "${TraktScreenplay.Ids}": {
                        "${TraktContentIds.Tmdb}": ${ScreenplaySample.Inception.tmdbId.value},
                        "${TraktContentIds.Trakt}": "${ScreenplaySample.Inception.traktId.value}"
                    }
                }
            },
            {
                "${TraktScreenplayType.TvShow}": {
                    "${TraktScreenplay.Ids}": {
                        "${TraktContentIds.Tmdb}": ${ScreenplaySample.BreakingBad.tmdbId.value},
                        "${TraktContentIds.Trakt}": "${ScreenplaySample.BreakingBad.traktId.value}"
                    }
                }
            }
        ]
    """.trimIndent()

    val OneTvShow = """
        [
            {
                "${TraktScreenplayType.TvShow}": {
                    "${TraktScreenplay.Ids}": {
                        "${TraktContentIds.Tmdb}": ${ScreenplaySample.BreakingBad.tmdbId.value},
                        "${TraktContentIds.Trakt}": "${ScreenplaySample.BreakingBad.traktId.value}"
                    }
                }
            }
        ]
    """.trimIndent()
}
