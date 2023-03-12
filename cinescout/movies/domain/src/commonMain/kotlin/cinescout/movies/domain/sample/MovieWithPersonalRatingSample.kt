package cinescout.movies.domain.sample

import cinescout.movies.domain.model.MovieWithPersonalRating
import cinescout.screenplay.domain.model.Rating
import cinescout.screenplay.domain.model.getOrThrow
import cinescout.screenplay.domain.sample.ScreenplaySample

object MovieWithPersonalRatingSample {

    val Inception = MovieWithPersonalRating(
        movie = ScreenplaySample.Inception,
        personalRating = Rating.of(9).getOrThrow()
    )

    val TheWolfOfWallStreet = MovieWithPersonalRating(
        movie = ScreenplaySample.TheWolfOfWallStreet,
        personalRating = Rating.of(8).getOrThrow()
    )

    val War = MovieWithPersonalRating(
        movie = ScreenplaySample.War,
        personalRating = Rating.of(6).getOrThrow()
    )
}
