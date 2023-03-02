package cinescout.movies.domain.sample

import cinescout.movies.domain.model.MovieIdWithPersonalRating
import cinescout.screenplay.domain.model.Rating
import cinescout.screenplay.domain.model.getOrThrow

object MovieIdWithPersonalRatingSample {

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
