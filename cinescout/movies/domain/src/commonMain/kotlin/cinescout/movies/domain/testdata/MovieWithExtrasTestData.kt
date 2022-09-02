package cinescout.movies.domain.testdata

import arrow.core.some
import cinescout.movies.domain.model.MovieWithExtras

object MovieWithExtrasTestData {

    val Inception = MovieWithExtras(
        movieWithDetails = MovieWithDetailsTestData.Inception,
        credits = MovieCreditsTestData.Inception,
        keywords = MovieKeywordsTestData.Inception,
        personalRating = MovieWithPersonalRatingTestData.Inception.personalRating.some()
    )

    val TheWolfOfWallStreet = MovieWithExtras(
        movieWithDetails = MovieWithDetailsTestData.TheWolfOfWallStreet,
        credits = MovieCreditsTestData.TheWolfOfWallStreet,
        keywords = MovieKeywordsTestData.TheWolfOfWallStreet,
        personalRating = MovieWithPersonalRatingTestData.TheWolfOfWallStreet.personalRating.some()
    )

    val War = MovieWithExtras(
        movieWithDetails = MovieWithDetailsTestData.War,
        credits = MovieCreditsTestData.War,
        keywords = MovieKeywordsTestData.War,
        personalRating = MovieWithPersonalRatingTestData.War.personalRating.some()
    )
}
