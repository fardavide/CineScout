package screenplay.data.remote.trakt.test

import cinescout.screenplay.domain.sample.ScreenplaySample
import cinescout.screenplay.domain.sample.TmdbScreenplayIdSample
import screenplay.data.remote.trakt.model.TraktMovieBody

object TraktRecommendedMoviesJson {

    val OneMovie = """
        [
            {
                "${TraktMovieBody.Ids}": {
                    "${TraktMovieBody.Ids.Tmdb}": ${TmdbScreenplayIdSample.Inception.value}
                },
                "${TraktMovieBody.Title}": "${ScreenplaySample.Inception.title}"
            }
        ]
    """.trimIndent()

    val TwoMovies = """
        [
            {
                "${TraktMovieBody.Ids}": {
                    "${TraktMovieBody.Ids.Tmdb}": ${TmdbScreenplayIdSample.Inception.value}
                },
                "${TraktMovieBody.Title}": "${ScreenplaySample.Inception.title}"
            },
            {
                "${TraktMovieBody.Ids}": {
                    "${TraktMovieBody.Ids.Tmdb}": ${TmdbScreenplayIdSample.TheWolfOfWallStreet.value}
                },
                "${TraktMovieBody.Title}": "${ScreenplaySample.TheWolfOfWallStreet.title}"
            }
        ]
    """.trimIndent()

    val ThreeMovies = """
        [
            {
                "${TraktMovieBody.Ids}": {
                    "${TraktMovieBody.Ids.Tmdb}": ${TmdbScreenplayIdSample.Inception.value}
                },
                "${TraktMovieBody.Title}": "${ScreenplaySample.Inception.title}"
            },
            {
                "${TraktMovieBody.Ids}": {
                    "${TraktMovieBody.Ids.Tmdb}": ${TmdbScreenplayIdSample.TheWolfOfWallStreet.value}
                },
                "${TraktMovieBody.Title}": "${ScreenplaySample.TheWolfOfWallStreet.title}"
            },
            {
                "${TraktMovieBody.Ids}": {
                    "${TraktMovieBody.Ids.Tmdb}": ${TmdbScreenplayIdSample.War.value}
                },
                "${TraktMovieBody.Title}": "${ScreenplaySample.War.title}"
            }
        ]
    """.trimIndent()
}
