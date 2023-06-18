package cinescout.watchlist.data.remote.res

import cinescout.screenplay.domain.sample.ScreenplaySample
import screenplay.data.remote.trakt.model.TraktContent
import screenplay.data.remote.trakt.model.TraktContentIds
import screenplay.data.remote.trakt.model.TraktContentType
import screenplay.data.remote.trakt.model.TraktMovieExtendedBody
import screenplay.data.remote.trakt.model.TraktTvShowExtendedBody

object TraktWatchlistExtendedJson {

    val OneMovie = """
        [
            {
                "${TraktContentType.Movie}": {
                    "${TraktContent.Ids}": {
                        "${TraktContentIds.Tmdb}": ${ScreenplaySample.Inception.tmdbId.value},
                        "${TraktContentIds.Trakt}": "${ScreenplaySample.Inception.traktId.value}"
                    },
                    "${TraktContent.Overview}": "${ScreenplaySample.Inception.overview}",
                    "${TraktMovieExtendedBody.Released}": "${ScreenplaySample.Inception.releaseDate.getOrNull()}",
                    "${TraktContent.Rating}": ${ScreenplaySample.Inception.rating.average.value},
                    "${TraktContent.Runtime}": "${ScreenplaySample.Inception.runtime.getOrNull()?.inWholeMinutes}",
                    "${TraktMovieExtendedBody.Tagline}": "${ScreenplaySample.Inception.tagline.getOrNull().orEmpty()}",
                    "${TraktContent.Title}": "${ScreenplaySample.Inception.title}",
                    "${TraktContent.Votes}": ${ScreenplaySample.Inception.rating.voteCount}
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
                        "${TraktContentIds.Trakt}": "${ScreenplaySample.Inception.traktId.value}"
                    },
                    "${TraktContent.Overview}": "${ScreenplaySample.Inception.overview}",
                    "${TraktMovieExtendedBody.Released}": "${ScreenplaySample.Inception.releaseDate.getOrNull()}",
                    "${TraktContent.Rating}": ${ScreenplaySample.Inception.rating.average.value},
                    "${TraktContent.Runtime}": "${ScreenplaySample.Inception.runtime.getOrNull()?.inWholeMinutes}",
                    "${TraktMovieExtendedBody.Tagline}": "${ScreenplaySample.Inception.tagline.getOrNull().orEmpty()}",
                    "${TraktContent.Title}": "${ScreenplaySample.Inception.title}",
                    "${TraktContent.Votes}": ${ScreenplaySample.Inception.rating.voteCount}
                }
            },
            {
                "${TraktContentType.TvShow}": {
                    "${TraktTvShowExtendedBody.AiredEpisodes}": "${ScreenplaySample.BreakingBad.airedEpisodes}",
                    "${TraktTvShowExtendedBody.FirstAired}": "${ScreenplaySample.BreakingBad.firstAirDate}",
                    "${TraktContent.Ids}": {
                        "${TraktContentIds.Tmdb}": ${ScreenplaySample.BreakingBad.tmdbId.value},
                        "${TraktContentIds.Trakt}": "${ScreenplaySample.BreakingBad.traktId.value}"
                    },
                    "${TraktContent.Overview}": "${ScreenplaySample.BreakingBad.overview}",
                    "${TraktContent.Rating}": ${ScreenplaySample.BreakingBad.rating.average.value},
                    "${TraktContent.Runtime}": ${ScreenplaySample.BreakingBad.runtime.getOrNull()?.inWholeMinutes}
                    "${TraktTvShowExtendedBody.Status}": "ended",
                    "${TraktContent.Title}": "${ScreenplaySample.BreakingBad.title}",
                    "${TraktContent.Votes}": ${ScreenplaySample.BreakingBad.rating.voteCount}
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
                    "${TraktContent.Ids}": {
                        "${TraktContentIds.Tmdb}": ${ScreenplaySample.BreakingBad.tmdbId.value},
                        "${TraktContentIds.Trakt}": "${ScreenplaySample.BreakingBad.traktId.value}"
                    },
                    "${TraktContent.Overview}": "${ScreenplaySample.BreakingBad.overview}",
                    "${TraktContent.Rating}": ${ScreenplaySample.BreakingBad.rating.average.value},
                    "${TraktContent.Runtime}": ${ScreenplaySample.BreakingBad.runtime.getOrNull()?.inWholeMinutes}
                    "${TraktTvShowExtendedBody.Status}": "ended",
                    "${TraktContent.Title}": "${ScreenplaySample.BreakingBad.title}",
                    "${TraktContent.Votes}": ${ScreenplaySample.BreakingBad.rating.voteCount}
                }
            }
        ]
    """.trimIndent()
}
