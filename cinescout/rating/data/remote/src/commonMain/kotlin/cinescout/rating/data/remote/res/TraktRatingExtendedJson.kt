package cinescout.rating.data.remote.res

import cinescout.rating.data.remote.model.Rating
import cinescout.rating.data.remote.sample.TraktRatingMetadataBodySample
import cinescout.screenplay.domain.sample.ScreenplaySample
import screenplay.data.remote.trakt.model.TraktContentIds
import screenplay.data.remote.trakt.model.TraktMovieExtendedBody
import screenplay.data.remote.trakt.model.TraktScreenplay
import screenplay.data.remote.trakt.model.TraktScreenplayType
import screenplay.data.remote.trakt.model.TraktTvShowExtendedBody

object TraktRatingExtendedJson {

    val OneMovie = """
        [
            {
                "$Rating": ${TraktRatingMetadataBodySample.Inception.rating},
                "${TraktScreenplayType.Movie}": {
                    "${TraktScreenplay.Ids}": {
                        "${TraktContentIds.Tmdb}": ${ScreenplaySample.Inception.tmdbId.value},
                        "${TraktContentIds.Trakt}": "${ScreenplaySample.Inception.traktId.value}"
                    },
                    "${TraktScreenplay.Overview}": "${ScreenplaySample.Inception.overview}",
                    "${TraktMovieExtendedBody.Released}": "${ScreenplaySample.Inception.releaseDate.orNull()}",
                    "${TraktScreenplay.Runtime}": "${ScreenplaySample.Inception.runtime.orNull()?.inWholeMinutes}",
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
                "$Rating": ${TraktRatingMetadataBodySample.Inception.rating},
                "${TraktScreenplayType.Movie}": {
                    "${TraktScreenplay.Ids}": {
                        "${TraktContentIds.Tmdb}": ${ScreenplaySample.Inception.tmdbId.value},
                        "${TraktContentIds.Trakt}": "${ScreenplaySample.Inception.traktId.value}"
                    },
                    "${TraktScreenplay.Overview}": "${ScreenplaySample.Inception.overview}",
                    "${TraktMovieExtendedBody.Released}": "${ScreenplaySample.Inception.releaseDate.orNull()}",
                    "${TraktScreenplay.Title}": "${ScreenplaySample.Inception.title}",
                    "${TraktScreenplay.Rating}": ${ScreenplaySample.Inception.rating.average.value},
                    "${TraktScreenplay.Runtime}": ${ScreenplaySample.Inception.runtime.orNull()?.inWholeMinutes},
                    "${TraktScreenplay.Votes}": ${ScreenplaySample.Inception.rating.voteCount}
                }
            },
            {
                "$Rating": ${TraktRatingMetadataBodySample.BreakingBad.rating},
                "${TraktScreenplayType.TvShow}": {
                    "${TraktTvShowExtendedBody.AiredEpisodes}": "${ScreenplaySample.BreakingBad.airedEpisodes}",
                    "${TraktTvShowExtendedBody.FirstAired}": "${ScreenplaySample.BreakingBad.firstAirDate}",
                    "${TraktScreenplay.Ids}": {
                        "${TraktContentIds.Tmdb}": ${ScreenplaySample.BreakingBad.tmdbId.value},
                        "${TraktContentIds.Trakt}": "${ScreenplaySample.BreakingBad.traktId.value}"
                    },
                    "${TraktScreenplay.Overview}": "${ScreenplaySample.BreakingBad.overview}",
                    "${TraktScreenplay.Title}": "${ScreenplaySample.BreakingBad.title}",
                    "${TraktScreenplay.Rating}": ${ScreenplaySample.BreakingBad.rating.average.value},
                    "${TraktScreenplay.Runtime}": ${ScreenplaySample.BreakingBad.runtime.orNull()?.inWholeMinutes},
                    "${TraktScreenplay.Votes}": ${ScreenplaySample.BreakingBad.rating.voteCount}
                }
            }
        ]
    """.trimIndent()

    val OneTvShow = """
        [
            {
                "$Rating": ${TraktRatingMetadataBodySample.BreakingBad.rating},
                "${TraktScreenplayType.TvShow}": {
                    "${TraktTvShowExtendedBody.AiredEpisodes}": "${ScreenplaySample.BreakingBad.airedEpisodes}",
                    "${TraktTvShowExtendedBody.FirstAired}": "${ScreenplaySample.BreakingBad.firstAirDate}",
                    "${TraktScreenplay.Ids}": {
                        "${TraktContentIds.Tmdb}": ${ScreenplaySample.BreakingBad.tmdbId.value},
                        "${TraktContentIds.Trakt}": "${ScreenplaySample.BreakingBad.traktId.value}"
                    },
                    "${TraktScreenplay.Overview}": "${ScreenplaySample.BreakingBad.overview}",
                    "${TraktScreenplay.Title}": "${ScreenplaySample.BreakingBad.title}",
                    "${TraktScreenplay.Rating}": ${ScreenplaySample.BreakingBad.rating.average.value},
                    "${TraktScreenplay.Runtime}": ${ScreenplaySample.BreakingBad.runtime.orNull()?.inWholeMinutes},
                    "${TraktScreenplay.Votes}": ${ScreenplaySample.BreakingBad.rating.voteCount}
                }
            }
        ]
    """.trimIndent()
}
