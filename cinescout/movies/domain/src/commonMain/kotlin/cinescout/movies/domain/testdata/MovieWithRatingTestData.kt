package cinescout.movies.domain.testdata

import arrow.core.orNull
import cinescout.movies.domain.model.MovieWithRating
import cinescout.movies.domain.model.Rating

object MovieWithRatingTestData {

    val Inception = MovieWithRating(
        movie = MovieTestData.Inception,
        rating = Rating.of(9).orNull()!!
    )

    val TheWolfOfWallStreet = MovieWithRating(
        movie = MovieTestData.TheWolfOfWallStreet,
        rating = Rating.of(8).orNull()!!
    )

    val War = MovieWithRating(
        movie = MovieTestData.War,
        rating = Rating.of(6).orNull()!!
    )
}
