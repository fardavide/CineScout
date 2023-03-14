package screenplay.data.remote.trakt.sample

import screenplay.data.remote.trakt.model.TraktMoviesMetadataResponse

object TraktMoviesMetadataResponseSample {

    val OneMovie: TraktMoviesMetadataResponse = listOf(
        TraktMovieMetadataBodySample.Inception
    )

    val TwoMovies: TraktMoviesMetadataResponse = listOf(
        TraktMovieMetadataBodySample.Inception,
        TraktMovieMetadataBodySample.TheWolfOfWallStreet
    )

    val ThreeMovies: TraktMoviesMetadataResponse = listOf(
        TraktMovieMetadataBodySample.Inception,
        TraktMovieMetadataBodySample.TheWolfOfWallStreet,
        TraktMovieMetadataBodySample.War
    )
}
