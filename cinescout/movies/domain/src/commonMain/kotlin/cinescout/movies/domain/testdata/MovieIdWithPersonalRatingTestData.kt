package cinescout.movies.domain.testdata

import cinescout.common.model.Rating
import cinescout.common.model.getOrThrow
import cinescout.movies.domain.model.MovieIdWithPersonalRating

object MovieIdWithPersonalRatingTestData {

    val Inception = MovieIdWithPersonalRating(
        movieId = TmdbMovieIdTestData.Inception,
        personalRating = Rating.of(9).getOrThrow()
    )

    val TheWolfOfWallStreet = MovieIdWithPersonalRating(
        movieId = TmdbMovieIdTestData.TheWolfOfWallStreet,
        personalRating = Rating.of(8).getOrThrow()
    )

    val War = MovieIdWithPersonalRating(
        movieId = TmdbMovieIdTestData.War,
        personalRating = Rating.of(6).getOrThrow()
    )
}
