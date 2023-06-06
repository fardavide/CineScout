package cinescout.watchlist.data.remote.res

import cinescout.screenplay.domain.sample.ScreenplaySample
import screenplay.data.remote.trakt.model.TraktContentIds
import screenplay.data.remote.trakt.model.TraktContentType
import screenplay.data.remote.trakt.model.TraktMovieExtendedBody
import screenplay.data.remote.trakt.model.TraktScreenplay
import screenplay.data.remote.trakt.model.TraktTvShowExtendedBody

object TraktWatchlistExtendedJson {

    val OneMovie = """
        [
            {
                "${TraktContentType.Movie}": {
                    "${TraktScreenplay.Ids}": {
                        "${TraktContentIds.Tmdb}": ${ScreenplaySample.Inception.tmdbId.value},
                        "${TraktContentIds.Trakt}": "${ScreenplaySample.Inception.traktId.value}"
                    },
                    "${TraktScreenplay.Overview}": "${ScreenplaySample.Inception.overview}",
                    "${TraktMovieExtendedBody.Released}": "${ScreenplaySample.Inception.releaseDate.orNull()}",
                    "${TraktScreenplay.Rating}": ${ScreenplaySample.Inception.rating.average.value},
                    "${TraktScreenplay.Runtime}": "${ScreenplaySample.Inception.runtime.orNull()?.inWholeMinutes}",
                    "${TraktMovieExtendedBody.Tagline}": "${ScreenplaySample.Inception.tagline.orNull().orEmpty()}",
                    "${TraktScreenplay.Title}": "${ScreenplaySample.Inception.title}",
                    "${TraktScreenplay.Votes}": ${ScreenplaySample.Inception.rating.voteCount}
                }
            }
        ]
    """.trimIndent()

    val OneMovieAndOneTvShow = """
        [
            {
                "${TraktContentType.Movie}": {
                    "${TraktScreenplay.Ids}": {
                        "${TraktContentIds.Tmdb}": ${ScreenplaySample.Inception.tmdbId.value},
                        "${TraktContentIds.Trakt}": "${ScreenplaySample.Inception.traktId.value}"
                    },
                    "${TraktScreenplay.Overview}": "${ScreenplaySample.Inception.overview}",
                    "${TraktMovieExtendedBody.Released}": "${ScreenplaySample.Inception.releaseDate.orNull()}",
                    "${TraktScreenplay.Rating}": ${ScreenplaySample.Inception.rating.average.value},
                    "${TraktScreenplay.Runtime}": "${ScreenplaySample.Inception.runtime.orNull()?.inWholeMinutes}",
                    "${TraktMovieExtendedBody.Tagline}": "${ScreenplaySample.Inception.tagline.orNull().orEmpty()}",
                    "${TraktScreenplay.Title}": "${ScreenplaySample.Inception.title}",
                    "${TraktScreenplay.Votes}": ${ScreenplaySample.Inception.rating.voteCount}
                }
            },
            {
                "${TraktContentType.TvShow}": {
                    "${TraktTvShowExtendedBody.AiredEpisodes}": "${ScreenplaySample.BreakingBad.airedEpisodes}",
                    "${TraktTvShowExtendedBody.FirstAired}": "${ScreenplaySample.BreakingBad.firstAirDate}",
                    "${TraktScreenplay.Ids}": {
                        "${TraktContentIds.Tmdb}": ${ScreenplaySample.BreakingBad.tmdbId.value},
                        "${TraktContentIds.Trakt}": "${ScreenplaySample.BreakingBad.traktId.value}"
                    },
                    "${TraktScreenplay.Overview}": "${ScreenplaySample.BreakingBad.overview}",
                    "${TraktScreenplay.Title}": "${ScreenplaySample.BreakingBad.title}",
                    "${TraktScreenplay.Rating}": ${ScreenplaySample.BreakingBad.rating.average.value},
                    "${TraktScreenplay.Runtime}": ${ScreenplaySample.BreakingBad.runtime.orNull()?.inWholeMinutes}
                    "${TraktScreenplay.Votes}": ${ScreenplaySample.BreakingBad.rating.voteCount}
                }
            }
        ]
    """.trimIndent()

    val OneTvShow = """
        [
            {
                "${TraktContentType.TvShow}": {
                    "${TraktTvShowExtendedBody.AiredEpisodes}": "${ScreenplaySample.BreakingBad.airedEpisodes}",
                    "${TraktTvShowExtendedBody.FirstAired}": "${ScreenplaySample.BreakingBad.firstAirDate}",
                    "${TraktScreenplay.Ids}": {
                        "${TraktContentIds.Tmdb}": ${ScreenplaySample.BreakingBad.tmdbId.value},
                        "${TraktContentIds.Trakt}": "${ScreenplaySample.BreakingBad.traktId.value}"
                    },
                    "${TraktScreenplay.Overview}": "${ScreenplaySample.BreakingBad.overview}",
                    "${TraktScreenplay.Title}": "${ScreenplaySample.BreakingBad.title}",
                    "${TraktScreenplay.Rating}": ${ScreenplaySample.BreakingBad.rating.average.value},
                    "${TraktScreenplay.Runtime}": ${ScreenplaySample.BreakingBad.runtime.orNull()?.inWholeMinutes}
                    "${TraktScreenplay.Votes}": ${ScreenplaySample.BreakingBad.rating.voteCount}
                }
            }
        ]
    """.trimIndent()
}
