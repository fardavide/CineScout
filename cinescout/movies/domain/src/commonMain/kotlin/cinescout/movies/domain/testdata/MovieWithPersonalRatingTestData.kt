package cinescout.movies.domain.testdata

import cinescout.common.model.Rating
import cinescout.common.model.getOrThrow
import cinescout.movies.domain.model.MovieWithPersonalRating
import cinescout.movies.domain.sample.MovieSample

object MovieWithPersonalRatingTestData {

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
