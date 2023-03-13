package screenplay.data.remote.trakt.sample

import screenplay.data.remote.trakt.model.TraktMoviesMetadataResponse

object TraktMoviesResponseSample {

    val OneMovie: TraktMoviesMetadataResponse = listOf(
        TraktMovieBodySample.Inception
    )

    val TwoMovies: TraktMoviesMetadataResponse = listOf(
        TraktMovieBodySample.Inception,
        TraktMovieBodySample.TheWolfOfWallStreet
    )

    val ThreeMovies: TraktMoviesMetadataResponse = listOf(
        TraktMovieBodySample.Inception,
        TraktMovieBodySample.TheWolfOfWallStreet,
        TraktMovieBodySample.War
    )
}
