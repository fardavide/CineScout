package cinescout.movies.domain.sample

import arrow.core.some
import cinescout.movies.domain.model.MovieWithExtras

object MovieWithExtrasSample {

    val Inception = MovieWithExtras(
        movieWithDetails = MovieWithDetailsSample.Inception,
        isInWatchlist = true,
        credits = MovieCreditsSample.Inception,
        keywords = MovieKeywordsSample.Inception,
        personalRating = MovieWithPersonalRatingSample.Inception.personalRating.some()
    )

    val TheWolfOfWallStreet = MovieWithExtras(
        movieWithDetails = MovieWithDetailsSample.TheWolfOfWallStreet,
        isInWatchlist = false,
        credits = MovieCreditsSample.TheWolfOfWallStreet,
        keywords = MovieKeywordsSample.TheWolfOfWallStreet,
        personalRating = MovieWithPersonalRatingSample.TheWolfOfWallStreet.personalRating.some()
    )

    val War = MovieWithExtras(
        movieWithDetails = MovieWithDetailsSample.War,
        isInWatchlist = false,
        credits = MovieCreditsSample.War,
        keywords = MovieKeywordsSample.War,
        personalRating = MovieWithPersonalRatingSample.War.personalRating.some()
    )
}
