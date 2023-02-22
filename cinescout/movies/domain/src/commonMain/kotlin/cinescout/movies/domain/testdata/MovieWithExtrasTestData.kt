package cinescout.movies.domain.testdata

import arrow.core.some
import cinescout.movies.domain.model.MovieWithExtras
import cinescout.movies.domain.sample.MovieWithDetailsSample
import cinescout.movies.domain.sample.MovieWithPersonalRatingSample

object MovieWithExtrasTestData {

    val Inception = MovieWithExtras(
        movieWithDetails = MovieWithDetailsSample.Inception,
        isInWatchlist = true,
        credits = MovieCreditsTestData.Inception,
        keywords = MovieKeywordsTestData.Inception,
        personalRating = MovieWithPersonalRatingSample.Inception.personalRating.some()
    )

    val TheWolfOfWallStreet = MovieWithExtras(
        movieWithDetails = MovieWithDetailsSample.TheWolfOfWallStreet,
        isInWatchlist = false,
        credits = MovieCreditsTestData.TheWolfOfWallStreet,
        keywords = MovieKeywordsTestData.TheWolfOfWallStreet,
        personalRating = MovieWithPersonalRatingSample.TheWolfOfWallStreet.personalRating.some()
    )

    val War = MovieWithExtras(
        movieWithDetails = MovieWithDetailsSample.War,
        isInWatchlist = false,
        credits = MovieCreditsTestData.War,
        keywords = MovieKeywordsTestData.War,
        personalRating = MovieWithPersonalRatingSample.War.personalRating.some()
    )
}
