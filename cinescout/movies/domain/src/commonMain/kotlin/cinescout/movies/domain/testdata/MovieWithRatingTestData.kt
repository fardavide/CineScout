package cinescout.movies.domain.testdata

import cinescout.movies.domain.model.MovieWithPersonalRating
import cinescout.movies.domain.model.Rating
import cinescout.movies.domain.model.getOrThrow

object MovieWithRatingTestData {

    val Inception = MovieWithPersonalRating(
        movie = MovieTestData.Inception,
        rating = Rating.of(9).getOrThrow()
    )

    val TheWolfOfWallStreet = MovieWithPersonalRating(
        movie = MovieTestData.TheWolfOfWallStreet,
        rating = Rating.of(8).getOrThrow()
    )

    val War = MovieWithPersonalRating(
        movie = MovieTestData.War,
        rating = Rating.of(6).getOrThrow()
    )
}
