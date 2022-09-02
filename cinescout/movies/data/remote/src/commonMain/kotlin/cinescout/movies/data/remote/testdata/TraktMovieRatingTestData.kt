package cinescout.movies.data.remote.testdata

import cinescout.movies.data.remote.model.TraktPersonalMovieRating
import cinescout.movies.domain.testdata.MovieWithPersonalRatingTestData

object TraktMovieRatingTestData {

    val Inception = TraktPersonalMovieRating(
        tmdbId = MovieWithPersonalRatingTestData.Inception.movie.tmdbId,
        rating = MovieWithPersonalRatingTestData.Inception.personalRating
    )

    val TheWolfOfWallStreet = TraktPersonalMovieRating(
        tmdbId = MovieWithPersonalRatingTestData.TheWolfOfWallStreet.movie.tmdbId,
        rating = MovieWithPersonalRatingTestData.TheWolfOfWallStreet.personalRating
    )

    val War = TraktPersonalMovieRating(
        tmdbId = MovieWithPersonalRatingTestData.War.movie.tmdbId,
        rating = MovieWithPersonalRatingTestData.War.personalRating
    )
}
