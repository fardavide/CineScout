package cinescout.movies.domain.testdata

import arrow.core.some
import cinescout.movies.domain.model.MovieWithExtras

object MovieWithExtrasTestData {

    val Inception = MovieWithExtras(
        movieWithDetails = MovieWithDetailsTestData.Inception,
        credits = MovieCreditsTestData.Inception,
        personalRating = MovieWithPersonalRatingTestData.Inception.rating.some()
    )

    val TheWolfOfWallStreet = MovieWithExtras(
        movieWithDetails = MovieWithDetailsTestData.TheWolfOfWallStreet,
        credits = MovieCreditsTestData.TheWolfOfWallStreet,
        personalRating = MovieWithPersonalRatingTestData.TheWolfOfWallStreet.rating.some()
    )

    val War = MovieWithExtras(
        movieWithDetails = MovieWithDetailsTestData.War,
        credits = MovieCreditsTestData.War,
        personalRating = MovieWithPersonalRatingTestData.War.rating.some()
    )
}
