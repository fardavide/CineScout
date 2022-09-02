package cinescout.movies.domain.testdata

import arrow.core.nonEmptyListOf
import arrow.core.some
import cinescout.movies.domain.model.MovieExtras

object MovieExtrasTestData {

    val Inception = MovieExtras(
        credits = MovieCreditsTestData.Inception,
        genres = nonEmptyListOf(GenreTestData.Action, GenreTestData.Adventure, GenreTestData.ScienceFiction),
        personalRating = MovieWithPersonalRatingTestData.Inception.personalRating.some()
    )

    val TheWolfOfWallStreet = MovieExtras(
        credits = MovieCreditsTestData.TheWolfOfWallStreet,
        genres = nonEmptyListOf(GenreTestData.Action, GenreTestData.Crime, GenreTestData.Drama),
        personalRating = MovieWithPersonalRatingTestData.TheWolfOfWallStreet.personalRating.some()
    )

    val War = MovieExtras(
        credits = MovieCreditsTestData.War,
        genres = nonEmptyListOf(GenreTestData.Action, GenreTestData.Adventure, GenreTestData.ScienceFiction),
        personalRating = MovieWithPersonalRatingTestData.War.personalRating.some()
    )
}
