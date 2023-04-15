package cinescout.watchlist.data.remote.res

import cinescout.screenplay.domain.sample.ScreenplaySample
import screenplay.data.remote.trakt.model.TraktMovieExtendedBody
import screenplay.data.remote.trakt.model.TraktMovieIds
import screenplay.data.remote.trakt.model.TraktScreenplay
import screenplay.data.remote.trakt.model.TraktScreenplayType
import screenplay.data.remote.trakt.model.TraktTvShowExtendedBody

object TraktWatchlistExtendedJson {

    val OneMovie = """
        [
            {
                "${TraktScreenplayType.Movie}": {
                    "${TraktScreenplay.Ids}": {
                        "${TraktMovieIds.Tmdb}": ${ScreenplaySample.Inception.tmdbId.value},
                        "${TraktMovieIds.Trakt}": "${ScreenplaySample.Inception.traktId.value}"
                    },
                    "${TraktMovieExtendedBody.Overview}": "${ScreenplaySample.Inception.overview}",
                    "${TraktMovieExtendedBody.Released}": "${ScreenplaySample.Inception.releaseDate.orNull()}",
                    "${TraktMovieExtendedBody.Title}": "${ScreenplaySample.Inception.title}",
                    "${TraktMovieExtendedBody.Votes}": ${ScreenplaySample.Inception.rating.voteCount},
                    "${TraktMovieExtendedBody.Rating}": ${ScreenplaySample.Inception.rating.average.value}
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
                    },
                    "${TraktMovieExtendedBody.Overview}": "${ScreenplaySample.Inception.overview}",
                    "${TraktMovieExtendedBody.Released}": "${ScreenplaySample.Inception.releaseDate.orNull()}",
                    "${TraktMovieExtendedBody.Title}": "${ScreenplaySample.Inception.title}",
                    "${TraktMovieExtendedBody.Rating}": ${ScreenplaySample.Inception.rating.average.value},
                    "${TraktMovieExtendedBody.Votes}": ${ScreenplaySample.Inception.rating.voteCount}
                }
            },
            {
                "${TraktScreenplayType.TvShow}": {
                    "${TraktTvShowExtendedBody.FirstAired}": "${ScreenplaySample.BreakingBad.firstAirDate}",
                    "${TraktScreenplay.Ids}": {
                        "${TraktMovieIds.Tmdb}": ${ScreenplaySample.BreakingBad.tmdbId.value},
                        "${TraktMovieIds.Trakt}": "${ScreenplaySample.BreakingBad.traktId.value}"
                    },
                    "${TraktTvShowExtendedBody.Overview}": "${ScreenplaySample.BreakingBad.overview}",
                    "${TraktTvShowExtendedBody.Title}": "${ScreenplaySample.BreakingBad.title}",
                    "${TraktTvShowExtendedBody.Rating}": ${ScreenplaySample.BreakingBad.rating.average.value},
                    "${TraktTvShowExtendedBody.Votes}": ${ScreenplaySample.BreakingBad.rating.voteCount}
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
                        "${TraktMovieIds.Tmdb}": ${ScreenplaySample.BreakingBad.tmdbId.value},
                        "${TraktMovieIds.Trakt}": "${ScreenplaySample.BreakingBad.traktId.value}"
                    },
                    "${TraktTvShowExtendedBody.Overview}": "${ScreenplaySample.BreakingBad.overview}",
                    "${TraktTvShowExtendedBody.Title}": "${ScreenplaySample.BreakingBad.title}",
                    "${TraktTvShowExtendedBody.Rating}": ${ScreenplaySample.BreakingBad.rating.average.value},
                    "${TraktTvShowExtendedBody.Votes}": ${ScreenplaySample.BreakingBad.rating.voteCount}
                }
            }
        ]
    """.trimIndent()
}
