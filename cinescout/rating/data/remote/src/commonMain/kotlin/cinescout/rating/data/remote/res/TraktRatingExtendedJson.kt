package cinescout.rating.data.remote.res

import cinescout.rating.data.remote.model.Rating
import cinescout.rating.data.remote.sample.TraktRatingMetadataBodySample
import cinescout.screenplay.domain.sample.ScreenplaySample
import screenplay.data.remote.trakt.model.TraktContentIds
import screenplay.data.remote.trakt.model.TraktContentType
import screenplay.data.remote.trakt.model.TraktMovieExtendedBody
import screenplay.data.remote.trakt.model.TraktScreenplay
import screenplay.data.remote.trakt.model.TraktTvShowExtendedBody

object TraktRatingExtendedJson {

    val OneMovie = """
        [
            {
                "$Rating": ${TraktRatingMetadataBodySample.Inception.rating},
                "${TraktContentType.Movie}": {
                    "${TraktScreenplay.Ids}": {
                        "${TraktContentIds.Tmdb}": ${ScreenplaySample.Inception.tmdbId.value},
                        "${TraktContentIds.Trakt}": "${ScreenplaySample.Inception.traktId.value}"
                    },
                    "${TraktScreenplay.Overview}": "${ScreenplaySample.Inception.overview}",
                    "${TraktScreenplay.Rating}": ${ScreenplaySample.Inception.rating.average.value},
                    "${TraktMovieExtendedBody.Released}": "${ScreenplaySample.Inception.releaseDate.getOrNull()}",
                    "${TraktScreenplay.Runtime}": "${ScreenplaySample.Inception.runtime.getOrNull()?.inWholeMinutes}",
                    "${TraktMovieExtendedBody.Tagline}": "${ScreenplaySample.Inception.tagline.getOrNull().orEmpty()}",
                    "${TraktScreenplay.Title}": "${ScreenplaySample.Inception.title}",
                    "${TraktScreenplay.Votes}": ${ScreenplaySample.Inception.rating.voteCount}
                }
            }
        ]
    """.trimIndent()

    val OneMovieAndOneTvShow = """
        [
            {
                "$Rating": ${TraktRatingMetadataBodySample.Inception.rating},
                "${TraktContentType.Movie}": {
                    "${TraktScreenplay.Ids}": {
                        "${TraktContentIds.Tmdb}": ${ScreenplaySample.Inception.tmdbId.value},
                        "${TraktContentIds.Trakt}": "${ScreenplaySample.Inception.traktId.value}"
                    },
                    "${TraktScreenplay.Overview}": "${ScreenplaySample.Inception.overview}",
                    "${TraktMovieExtendedBody.Released}": "${ScreenplaySample.Inception.releaseDate.getOrNull()}",
                    "${TraktScreenplay.Rating}": ${ScreenplaySample.Inception.rating.average.value},
                    "${TraktScreenplay.Runtime}": ${ScreenplaySample.Inception.runtime.getOrNull()?.inWholeMinutes},
                    "${TraktMovieExtendedBody.Tagline}": "${ScreenplaySample.Inception.tagline.getOrNull().orEmpty()}",
                    "${TraktScreenplay.Title}": "${ScreenplaySample.Inception.title}",
                    "${TraktScreenplay.Votes}": ${ScreenplaySample.Inception.rating.voteCount}
                }
            },
            {
                "$Rating": ${TraktRatingMetadataBodySample.BreakingBad.rating},
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
                    "${TraktScreenplay.Runtime}": ${ScreenplaySample.BreakingBad.runtime.getOrNull()?.inWholeMinutes},
                    "${TraktScreenplay.Votes}": ${ScreenplaySample.BreakingBad.rating.voteCount}
                }
            }
        ]
    """.trimIndent()

    val OneTvShow = """
        [
            {
                "$Rating": ${TraktRatingMetadataBodySample.BreakingBad.rating},
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
                    "${TraktScreenplay.Runtime}": ${ScreenplaySample.BreakingBad.runtime.getOrNull()?.inWholeMinutes},
                    "${TraktScreenplay.Votes}": ${ScreenplaySample.BreakingBad.rating.voteCount}
                }
            }
        ]
    """.trimIndent()
}
