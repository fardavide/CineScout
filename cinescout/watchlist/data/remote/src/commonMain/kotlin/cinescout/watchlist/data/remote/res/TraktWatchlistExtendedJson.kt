package cinescout.watchlist.data.remote.res

import cinescout.screenplay.domain.sample.ScreenplaySample
import screenplay.data.remote.trakt.model.TraktMovieExtendedBody
import screenplay.data.remote.trakt.model.TraktScreenplay
import screenplay.data.remote.trakt.model.TraktScreenplayIds
import screenplay.data.remote.trakt.model.TraktScreenplayType
import screenplay.data.remote.trakt.model.TraktTvShowExtendedBody

object TraktWatchlistExtendedJson {

    val OneMovie = """
        [
            {
                "${TraktScreenplayType.Movie}": {
                    "${TraktScreenplay.Ids}": {
                        "${TraktScreenplayIds.Tmdb}": ${ScreenplaySample.Inception.tmdbId.value},
                        "${TraktScreenplayIds.Trakt}": "${ScreenplaySample.Inception.traktId.value}"
                    },
                    "${TraktScreenplay.Overview}": "${ScreenplaySample.Inception.overview}",
                    "${TraktMovieExtendedBody.Released}": "${ScreenplaySample.Inception.releaseDate.orNull()}",
                    "${TraktScreenplay.Title}": "${ScreenplaySample.Inception.title}",
                    "${TraktScreenplay.Votes}": ${ScreenplaySample.Inception.rating.voteCount},
                    "${TraktScreenplay.Rating}": ${ScreenplaySample.Inception.rating.average.value}
                }
            }
        ]
    """.trimIndent()

    val OneMovieAndOneTvShow = """
        [
            {
                "${TraktScreenplayType.Movie}": {
                    "${TraktScreenplay.Ids}": {
                        "${TraktScreenplayIds.Tmdb}": ${ScreenplaySample.Inception.tmdbId.value},
                        "${TraktScreenplayIds.Trakt}": "${ScreenplaySample.Inception.traktId.value}"
                    },
                    "${TraktScreenplay.Overview}": "${ScreenplaySample.Inception.overview}",
                    "${TraktMovieExtendedBody.Released}": "${ScreenplaySample.Inception.releaseDate.orNull()}",
                    "${TraktScreenplay.Title}": "${ScreenplaySample.Inception.title}",
                    "${TraktScreenplay.Rating}": ${ScreenplaySample.Inception.rating.average.value},
                    "${TraktScreenplay.Votes}": ${ScreenplaySample.Inception.rating.voteCount}
                }
            },
            {
                "${TraktScreenplayType.TvShow}": {
                    "${TraktTvShowExtendedBody.FirstAired}": "${ScreenplaySample.BreakingBad.firstAirDate}",
                    "${TraktScreenplay.Ids}": {
                        "${TraktScreenplayIds.Tmdb}": ${ScreenplaySample.BreakingBad.tmdbId.value},
                        "${TraktScreenplayIds.Trakt}": "${ScreenplaySample.BreakingBad.traktId.value}"
                    },
                    "${TraktScreenplay.Overview}": "${ScreenplaySample.BreakingBad.overview}",
                    "${TraktScreenplay.Title}": "${ScreenplaySample.BreakingBad.title}",
                    "${TraktScreenplay.Rating}": ${ScreenplaySample.BreakingBad.rating.average.value},
                    "${TraktScreenplay.Votes}": ${ScreenplaySample.BreakingBad.rating.voteCount}
                }
            }
        ]
    """.trimIndent()

    val OneTvShow = """
        [
            {
                "${TraktScreenplayType.TvShow}": {
                    "${TraktTvShowExtendedBody.FirstAired}": "${ScreenplaySample.BreakingBad.firstAirDate}",
                    "${TraktScreenplay.Ids}": {
                        "${TraktScreenplayIds.Tmdb}": ${ScreenplaySample.BreakingBad.tmdbId.value},
                        "${TraktScreenplayIds.Trakt}": "${ScreenplaySample.BreakingBad.traktId.value}"
                    },
                    "${TraktScreenplay.Overview}": "${ScreenplaySample.BreakingBad.overview}",
                    "${TraktScreenplay.Title}": "${ScreenplaySample.BreakingBad.title}",
                    "${TraktScreenplay.Rating}": ${ScreenplaySample.BreakingBad.rating.average.value},
                    "${TraktScreenplay.Votes}": ${ScreenplaySample.BreakingBad.rating.voteCount}
                }
            }
        ]
    """.trimIndent()
}
