package cinescout.watchlist.data.remote.res

import cinescout.screenplay.domain.sample.ScreenplaySample
import screenplay.data.remote.trakt.model.TraktMovieIds
import screenplay.data.remote.trakt.model.TraktScreenplay
import screenplay.data.remote.trakt.model.TraktScreenplayType
import screenplay.data.remote.trakt.model.TraktTvShowIds

object TraktWatchlistMetadataJson {

    val OneMovie = """
        [
            {
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
                "${TraktScreenplayType.Movie}": {
                    "${TraktScreenplay.Ids}": {
                        "${TraktMovieIds.Tmdb}": ${ScreenplaySample.Inception.tmdbId.value},
                        "${TraktMovieIds.Trakt}": "${ScreenplaySample.Inception.traktId.value}"
                    }
                }
            },
            {
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
