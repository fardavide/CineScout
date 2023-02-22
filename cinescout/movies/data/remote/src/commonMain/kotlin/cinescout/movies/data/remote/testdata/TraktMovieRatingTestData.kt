package cinescout.movies.data.remote.testdata

import cinescout.movies.data.remote.model.TraktPersonalMovieRating
import cinescout.movies.domain.sample.MovieWithPersonalRatingSample

object TraktMovieRatingTestData {

    val Inception = TraktPersonalMovieRating(
        tmdbId = MovieWithPersonalRatingSample.Inception.movie.tmdbId,
        rating = MovieWithPersonalRatingSample.Inception.personalRating
    )

    val TheWolfOfWallStreet = TraktPersonalMovieRating(
        tmdbId = MovieWithPersonalRatingSample.TheWolfOfWallStreet.movie.tmdbId,
        rating = MovieWithPersonalRatingSample.TheWolfOfWallStreet.personalRating
    )

    val War = TraktPersonalMovieRating(
        tmdbId = MovieWithPersonalRatingSample.War.movie.tmdbId,
        rating = MovieWithPersonalRatingSample.War.personalRating
    )
}
