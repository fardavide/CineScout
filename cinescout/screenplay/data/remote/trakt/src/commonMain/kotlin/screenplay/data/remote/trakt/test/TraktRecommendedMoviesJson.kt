package screenplay.data.remote.trakt.test

import cinescout.screenplay.domain.sample.TmdbScreenplayIdSample
import screenplay.data.remote.trakt.model.TraktMovieIds
import screenplay.data.remote.trakt.model.TraktScreenplay

object TraktRecommendedMoviesJson {

    val OneMovie = """
        [
            {
                "${TraktScreenplay.Ids}": {
                    "${TraktMovieIds.Tmdb}": ${TmdbScreenplayIdSample.Inception.value}
                }
            }
        ]
    """.trimIndent()

    val TwoMovies = """
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
            }
        ]
    """.trimIndent()

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
}
