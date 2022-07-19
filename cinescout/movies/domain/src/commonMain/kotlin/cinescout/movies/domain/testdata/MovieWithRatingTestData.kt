package cinescout.movies.domain.testdata

import cinescout.movies.domain.model.MovieWithRating
import cinescout.movies.domain.model.Rating
import cinescout.movies.domain.model.getOrThrow

object MovieWithRatingTestData {

    val Inception = MovieWithRating(
        movie = MovieTestData.Inception,
        rating = Rating.of(9).getOrThrow()
    )

    val TheWolfOfWallStreet = MovieWithRating(
        movie = MovieTestData.TheWolfOfWallStreet,
        rating = Rating.of(8).getOrThrow()
    )

    val War = MovieWithRating(
        movie = MovieTestData.War,
        rating = Rating.of(6).getOrThrow()
    )
}
