package cinescout.watchlist.data.remote.res

import cinescout.screenplay.domain.sample.ScreenplaySample
import screenplay.data.remote.trakt.model.TraktContent
import screenplay.data.remote.trakt.model.TraktContentIds
import screenplay.data.remote.trakt.model.TraktContentType

object TraktWatchlistMetadataJson {

    val OneMovie = """
        [
            {
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
                "${TraktContentType.Movie}": {
                    "${TraktContent.Ids}": {
                        "${TraktContentIds.Tmdb}": ${ScreenplaySample.Inception.tmdbId.value},
                        "${TraktContentIds.Trakt}": ${ScreenplaySample.Inception.traktId.value}
                    }
                }
            },
            {
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
