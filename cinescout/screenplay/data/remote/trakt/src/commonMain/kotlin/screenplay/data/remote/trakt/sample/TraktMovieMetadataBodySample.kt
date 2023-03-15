package screenplay.data.remote.trakt.sample

import cinescout.screenplay.domain.sample.TmdbScreenplayIdSample
import screenplay.data.remote.trakt.model.TraktMovieIds
import screenplay.data.remote.trakt.model.TraktMovieMetadataBody

object TraktMovieMetadataBodySample {

    val Inception = TraktMovieMetadataBody(
        ids = TraktMovieIds(
            tmdb = TmdbScreenplayIdSample.Inception
        )
    )
    val TheWolfOfWallStreet = TraktMovieMetadataBody(
        ids = TraktMovieIds(
            tmdb = TmdbScreenplayIdSample.TheWolfOfWallStreet
        )
    )
    val War = TraktMovieMetadataBody(
        ids = TraktMovieIds(
            tmdb = TmdbScreenplayIdSample.War
        )
    )
}
