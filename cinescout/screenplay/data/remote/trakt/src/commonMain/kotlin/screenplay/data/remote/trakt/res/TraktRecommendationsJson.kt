package screenplay.data.remote.trakt.res

import cinescout.screenplay.domain.sample.TmdbScreenplayIdSample
import screenplay.data.remote.trakt.model.TraktMovieIds
import screenplay.data.remote.trakt.model.TraktScreenplay
import screenplay.data.remote.trakt.model.TraktTvShowIds

object TraktRecommendationsJson {

    val ThreeMovies = """
        [
            {
                "${TraktScreenplay.Ids}": {
                    "${TraktMovieIds.Tmdb}": ${TmdbScreenplayIdSample.Inception.value}
                }
            },
            {
                "${TraktScreenplay.Ids}": {
                    "${TraktMovieIds.Tmdb}": ${TmdbScreenplayIdSample.TheWolfOfWallStreet.value}
                }
            },
            {
                "${TraktScreenplay.Ids}": {
                    "${TraktMovieIds.Tmdb}": ${TmdbScreenplayIdSample.War.value}
                }
            }
        ]
    """.trimIndent()

    val ThreeMoviesAndThreeTvShows = """
        [
            {
                "${TraktScreenplay.Ids}": {
                    "${TraktMovieIds.Tmdb}": ${TmdbScreenplayIdSample.Inception.value}
                }
            },
            {
                "${TraktScreenplay.Ids}": {
                    "${TraktMovieIds.Tmdb}": ${TmdbScreenplayIdSample.TheWolfOfWallStreet.value}
                }
            },
            {
                "${TraktScreenplay.Ids}": {
                    "${TraktMovieIds.Tmdb}": ${TmdbScreenplayIdSample.War.value}
                }
            },
            {
                "${TraktScreenplay.Ids}": {
                    "${TraktTvShowIds.Tmdb}": ${TmdbScreenplayIdSample.BreakingBad.value}
                }
            },
            {
                "${TraktScreenplay.Ids}": {
                    "${TraktTvShowIds.Tmdb}": ${TmdbScreenplayIdSample.Dexter.value}
                }
            },
            {
                "${TraktScreenplay.Ids}": {
                    "${TraktTvShowIds.Tmdb}": ${TmdbScreenplayIdSample.Grimm.value}
                }
            }
        ]
    """.trimIndent()

    val ThreeTvShows = """
        [
            {
                "${TraktScreenplay.Ids}": {
                    "${TraktTvShowIds.Tmdb}": ${TmdbScreenplayIdSample.BreakingBad.value}
                }
            },
            {
                "${TraktScreenplay.Ids}": {
                    "${TraktTvShowIds.Tmdb}": ${TmdbScreenplayIdSample.Dexter.value}
                }
            },
            {
                "${TraktScreenplay.Ids}": {
                    "${TraktTvShowIds.Tmdb}": ${TmdbScreenplayIdSample.Grimm.value}
                }
            }
        ]
    """.trimIndent()
}
