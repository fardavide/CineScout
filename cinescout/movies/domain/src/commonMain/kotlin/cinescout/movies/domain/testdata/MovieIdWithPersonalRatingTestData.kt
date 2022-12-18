package cinescout.movies.domain.testdata

import cinescout.common.model.Rating
import cinescout.common.model.getOrThrow
import cinescout.movies.domain.model.MovieIdWithPersonalRating
import cinescout.movies.domain.sample.TmdbMovieIdSample

object MovieIdWithPersonalRatingTestData {

    val Inception = MovieIdWithPersonalRating(
        movieId = TmdbMovieIdSample.Inception,
        personalRating = Rating.of(9).getOrThrow()
    )

    val TheWolfOfWallStreet = MovieIdWithPersonalRating(
        movieId = TmdbMovieIdSample.TheWolfOfWallStreet,
        personalRating = Rating.of(8).getOrThrow()
    )

    val War = MovieIdWithPersonalRating(
        movieId = TmdbMovieIdSample.War,
        personalRating = Rating.of(6).getOrThrow()
    )
}
