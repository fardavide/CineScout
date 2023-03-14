package screenplay.data.remote.trakt.sample

import cinescout.screenplay.domain.sample.ScreenplaySample
import cinescout.screenplay.domain.sample.TmdbScreenplayIdSample
import screenplay.data.remote.trakt.model.TraktMovieIds
import screenplay.data.remote.trakt.model.TraktMovieMetadataBody

object TraktMovieMetadataBodySample {

    val Inception = TraktMovieMetadataBody(
        ids = TraktMovieIds(
            tmdb = TmdbScreenplayIdSample.Inception
        ),
        title = ScreenplaySample.Inception.title
    )
    val TheWolfOfWallStreet = TraktMovieMetadataBody(
        ids = TraktMovieIds(
            tmdb = TmdbScreenplayIdSample.TheWolfOfWallStreet
        ),
        title = ScreenplaySample.TheWolfOfWallStreet.title
    )
    val War = TraktMovieMetadataBody(
        ids = TraktMovieIds(
            tmdb = TmdbScreenplayIdSample.War
        ),
        title = ScreenplaySample.War.title
    )
}
