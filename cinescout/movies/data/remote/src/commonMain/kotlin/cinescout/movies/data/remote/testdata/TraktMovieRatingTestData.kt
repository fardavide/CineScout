package cinescout.movies.data.remote.testdata

import cinescout.movies.data.remote.model.TraktPersonalMovieRating
import cinescout.movies.domain.testdata.MovieWithRatingTestData

object TraktMovieRatingTestData {

    val Inception = TraktPersonalMovieRating(
        tmdbId = MovieWithRatingTestData.Inception.movie.tmdbId,
        rating = MovieWithRatingTestData.Inception.rating
    )

    val TheWolfOfWallStreet = TraktPersonalMovieRating(
        tmdbId = MovieWithRatingTestData.TheWolfOfWallStreet.movie.tmdbId,
        rating = MovieWithRatingTestData.TheWolfOfWallStreet.rating
    )

    val War = TraktPersonalMovieRating(
        tmdbId = MovieWithRatingTestData.War.movie.tmdbId,
        rating = MovieWithRatingTestData.War.rating
    )
}
