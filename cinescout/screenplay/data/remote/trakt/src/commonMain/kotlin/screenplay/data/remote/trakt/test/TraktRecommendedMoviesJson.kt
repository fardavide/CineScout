package screenplay.data.remote.trakt.test

import cinescout.screenplay.domain.sample.ScreenplaySample
import cinescout.screenplay.domain.sample.TmdbScreenplayIdSample
import screenplay.data.remote.trakt.model.TraktMovieIds
import screenplay.data.remote.trakt.model.TraktMovieMetadataBody

object TraktRecommendedMoviesJson {

    val OneMovie = """
        [
            {
                "${TraktMovieMetadataBody.Ids}": {
                    "${TraktMovieIds.Tmdb}": ${TmdbScreenplayIdSample.Inception.value}
                },
                "${TraktMovieMetadataBody.Title}": "${ScreenplaySample.Inception.title}"
            }
        ]
    """.trimIndent()

    val TwoMovies = """
        [
            {
                "${TraktMovieMetadataBody.Ids}": {
                    "${TraktMovieIds.Tmdb}": ${TmdbScreenplayIdSample.Inception.value}
                },
                "${TraktMovieMetadataBody.Title}": "${ScreenplaySample.Inception.title}"
            },
            {
                "${TraktMovieMetadataBody.Ids}": {
                    "${TraktMovieIds.Tmdb}": ${TmdbScreenplayIdSample.TheWolfOfWallStreet.value}
                },
                "${TraktMovieMetadataBody.Title}": "${ScreenplaySample.TheWolfOfWallStreet.title}"
            }
        ]
    """.trimIndent()

    val ThreeMovies = """
        [
            {
                "${TraktMovieMetadataBody.Ids}": {
                    "${TraktMovieIds.Tmdb}": ${TmdbScreenplayIdSample.Inception.value}
                },
                "${TraktMovieMetadataBody.Title}": "${ScreenplaySample.Inception.title}"
            },
            {
                "${TraktMovieMetadataBody.Ids}": {
                    "${TraktMovieIds.Tmdb}": ${TmdbScreenplayIdSample.TheWolfOfWallStreet.value}
                },
                "${TraktMovieMetadataBody.Title}": "${ScreenplaySample.TheWolfOfWallStreet.title}"
            },
            {
                "${TraktMovieMetadataBody.Ids}": {
                    "${TraktMovieIds.Tmdb}": ${TmdbScreenplayIdSample.War.value}
                },
                "${TraktMovieMetadataBody.Title}": "${ScreenplaySample.War.title}"
            }
        ]
    """.trimIndent()
}
