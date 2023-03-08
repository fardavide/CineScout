package screenplay.data.remote.trakt.sample

import screenplay.data.remote.trakt.model.TraktMoviesResponse

object TraktMoviesResponseSample {

    val OneMovie: TraktMoviesResponse = listOf(
        TraktMovieBodySample.Inception
    )

    val TwoMovies: TraktMoviesResponse = listOf(
        TraktMovieBodySample.Inception,
        TraktMovieBodySample.TheWolfOfWallStreet
    )

    val ThreeMovies: TraktMoviesResponse = listOf(
        TraktMovieBodySample.Inception,
        TraktMovieBodySample.TheWolfOfWallStreet,
        TraktMovieBodySample.War
    )
}
