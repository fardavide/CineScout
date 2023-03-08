package screenplay.data.remote.trakt.sample

import cinescout.screenplay.domain.sample.ScreenplaySample
import cinescout.screenplay.domain.sample.TmdbScreenplayIdSample
import screenplay.data.remote.trakt.model.TraktMovieBody

object TraktMovieBodySample {

    val Inception = TraktMovieBody(
        ids = TraktMovieBody.Ids(
            tmdb = TmdbScreenplayIdSample.Inception
        ),
        title = ScreenplaySample.Inception.title
    )
    val TheWolfOfWallStreet = TraktMovieBody(
        ids = TraktMovieBody.Ids(
            tmdb = TmdbScreenplayIdSample.TheWolfOfWallStreet
        ),
        title = ScreenplaySample.TheWolfOfWallStreet.title
    )
    val War = TraktMovieBody(
        ids = TraktMovieBody.Ids(
            tmdb = TmdbScreenplayIdSample.War
        ),
        title = ScreenplaySample.War.title
    )
}
