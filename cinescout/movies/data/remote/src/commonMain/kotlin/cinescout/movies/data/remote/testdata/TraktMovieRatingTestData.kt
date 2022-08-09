package cinescout.movies.data.remote.testdata

import cinescout.movies.data.remote.model.TraktPersonalMovieRating
import cinescout.movies.domain.testdata.MovieWithPersonalRatingTestData

object TraktMovieRatingTestData {

    val Inception = TraktPersonalMovieRating(
        tmdbId = MovieWithPersonalRatingTestData.Inception.movie.tmdbId,
        rating = MovieWithPersonalRatingTestData.Inception.rating
    )

    val TheWolfOfWallStreet = TraktPersonalMovieRating(
        tmdbId = MovieWithPersonalRatingTestData.TheWolfOfWallStreet.movie.tmdbId,
        rating = MovieWithPersonalRatingTestData.TheWolfOfWallStreet.rating
    )

    val War = TraktPersonalMovieRating(
        tmdbId = MovieWithPersonalRatingTestData.War.movie.tmdbId,
        rating = MovieWithPersonalRatingTestData.War.rating
    )
}
