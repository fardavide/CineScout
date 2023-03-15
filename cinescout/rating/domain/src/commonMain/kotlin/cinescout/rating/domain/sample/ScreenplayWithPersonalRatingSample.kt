package cinescout.rating.domain.sample

import cinescout.rating.domain.model.MovieWithPersonalRating
import cinescout.rating.domain.model.TvShowWithPersonalRating
import cinescout.screenplay.domain.model.Rating
import cinescout.screenplay.domain.model.getOrThrow
import cinescout.screenplay.domain.sample.ScreenplaySample

object ScreenplayWithPersonalRatingSample {

    val BreakingBad = TvShowWithPersonalRating(
        personalRating = Rating.of(7).getOrThrow(),
        screenplay = ScreenplaySample.BreakingBad
    )

    val Dexter = TvShowWithPersonalRating(
        personalRating = Rating.of(8).getOrThrow(),
        screenplay = ScreenplaySample.Dexter
    )

    val Grimm = TvShowWithPersonalRating(
        personalRating = Rating.of(9).getOrThrow(),
        screenplay = ScreenplaySample.Grimm
    )

    val Inception = MovieWithPersonalRating(
        personalRating = Rating.of(9).getOrThrow(),
        screenplay = ScreenplaySample.Inception
    )

    val TheWolfOfWallStreet = MovieWithPersonalRating(
        personalRating = Rating.of(8).getOrThrow(),
        screenplay = ScreenplaySample.TheWolfOfWallStreet
    )

    val War = MovieWithPersonalRating(
        personalRating = Rating.of(6).getOrThrow(),
        screenplay = ScreenplaySample.War
    )
}
