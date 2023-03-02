package cinescout.movies.domain.sample

import cinescout.movies.domain.model.MovieWithPersonalRating
import cinescout.screenplay.domain.model.Rating
import cinescout.screenplay.domain.model.getOrThrow

object MovieWithPersonalRatingSample {

    val Inception = MovieWithPersonalRating(
        movie = MovieSample.Inception,
        personalRating = Rating.of(9).getOrThrow()
    )

    val TheWolfOfWallStreet = MovieWithPersonalRating(
        movie = MovieSample.TheWolfOfWallStreet,
        personalRating = Rating.of(8).getOrThrow()
    )

    val War = MovieWithPersonalRating(
        movie = MovieSample.War,
        personalRating = Rating.of(6).getOrThrow()
    )
}
