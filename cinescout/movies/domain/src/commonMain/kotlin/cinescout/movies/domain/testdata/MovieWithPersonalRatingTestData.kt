package cinescout.movies.domain.testdata

import cinescout.movies.domain.model.MovieWithPersonalRating
import cinescout.movies.domain.model.Rating
import cinescout.movies.domain.model.getOrThrow

object MovieWithPersonalRatingTestData {

    val Inception = MovieWithPersonalRating(
        movie = MovieTestData.Inception,
        personalRating = Rating.of(9).getOrThrow()
    )

    val TheWolfOfWallStreet = MovieWithPersonalRating(
        movie = MovieTestData.TheWolfOfWallStreet,
        personalRating = Rating.of(8).getOrThrow()
    )

    val War = MovieWithPersonalRating(
        movie = MovieTestData.War,
        personalRating = Rating.of(6).getOrThrow()
    )
}
