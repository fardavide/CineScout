package screenplay.data.remote.trakt.res

import cinescout.screenplay.domain.sample.TmdbScreenplayIdSample
import cinescout.screenplay.domain.sample.TraktScreenplayIdSample
import screenplay.data.remote.trakt.model.TraktMovieIds
import screenplay.data.remote.trakt.model.TraktScreenplay
import screenplay.data.remote.trakt.model.TraktTvShowIds

object TraktRecommendationsJson {

    val ThreeMovies = """
        [
            {
                "${TraktScreenplay.Ids}": {
                    "${TraktMovieIds.Tmdb}": ${TmdbScreenplayIdSample.Inception.value},
                    "${TraktMovieIds.Trakt}": ${TraktScreenplayIdSample.Inception.value}
                }
            },
            {
                "${TraktScreenplay.Ids}": {
                    "${TraktMovieIds.Tmdb}": ${TmdbScreenplayIdSample.TheWolfOfWallStreet.value},
                    "${TraktMovieIds.Trakt}": ${TraktScreenplayIdSample.TheWolfOfWallStreet.value}
                }
            },
            {
                "${TraktScreenplay.Ids}": {
                    "${TraktMovieIds.Tmdb}": ${TmdbScreenplayIdSample.War.value},
                    "${TraktMovieIds.Trakt}": ${TraktScreenplayIdSample.War.value}
                }
            }
        ]
    """.trimIndent()

    val ThreeMoviesAndThreeTvShows = """
        [
            {
                "${TraktScreenplay.Ids}": {
                    "${TraktMovieIds.Tmdb}": ${TmdbScreenplayIdSample.Inception.value},
                    "${TraktMovieIds.Trakt}": ${TraktScreenplayIdSample.Inception.value}
                }
            },
            {
                "${TraktScreenplay.Ids}": {
                    "${TraktMovieIds.Tmdb}": ${TmdbScreenplayIdSample.TheWolfOfWallStreet.value},
                    "${TraktMovieIds.Trakt}": ${TraktScreenplayIdSample.TheWolfOfWallStreet.value}
                }
            },
            {
                "${TraktScreenplay.Ids}": {
                    "${TraktMovieIds.Tmdb}": ${TmdbScreenplayIdSample.War.value},
                    "${TraktMovieIds.Trakt}": ${TraktScreenplayIdSample.War.value}
                }
            },
            {
                "${TraktScreenplay.Ids}": {
                    "${TraktTvShowIds.Tmdb}": ${TmdbScreenplayIdSample.BreakingBad.value},
                    "${TraktTvShowIds.Trakt}": ${TraktScreenplayIdSample.BreakingBad.value}
                }
            },
            {
                "${TraktScreenplay.Ids}": {
                    "${TraktTvShowIds.Tmdb}": ${TmdbScreenplayIdSample.Dexter.value},
                    "${TraktTvShowIds.Trakt}": ${TraktScreenplayIdSample.Dexter.value}
                }
            },
            {
                "${TraktScreenplay.Ids}": {
                    "${TraktTvShowIds.Tmdb}": ${TmdbScreenplayIdSample.Grimm.value},
                    "${TraktTvShowIds.Trakt}": ${TraktScreenplayIdSample.Grimm.value}
                }
            }
        ]
    """.trimIndent()

    val ThreeTvShows = """
        [
            {
                "${TraktScreenplay.Ids}": {
                    "${TraktTvShowIds.Tmdb}": ${TmdbScreenplayIdSample.BreakingBad.value},
                    "${TraktTvShowIds.Trakt}": ${TraktScreenplayIdSample.BreakingBad.value}
                }
            },
            {
                "${TraktScreenplay.Ids}": {
                    "${TraktTvShowIds.Tmdb}": ${TmdbScreenplayIdSample.Dexter.value},
                    "${TraktTvShowIds.Trakt}": ${TraktScreenplayIdSample.Dexter.value}
                }
            },
            {
                "${TraktScreenplay.Ids}": {
                    "${TraktTvShowIds.Tmdb}": ${TmdbScreenplayIdSample.Grimm.value},
                    "${TraktTvShowIds.Trakt}": ${TraktScreenplayIdSample.Grimm.value}
                }
            }
        ]
    """.trimIndent()
}
