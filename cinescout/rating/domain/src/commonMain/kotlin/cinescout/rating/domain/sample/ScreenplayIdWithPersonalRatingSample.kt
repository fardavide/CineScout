package cinescout.rating.domain.sample

import cinescout.rating.domain.model.MovieIdWithPersonalRating
import cinescout.rating.domain.model.TvShowIdWithPersonalRating
import cinescout.screenplay.domain.model.Rating
import cinescout.screenplay.domain.model.getOrThrow
import cinescout.screenplay.domain.sample.ScreenplayIdsSample

object ScreenplayIdWithPersonalRatingSample {

    val BreakingBad = TvShowIdWithPersonalRating(
        personalRating = Rating.of(7).getOrThrow(),
        screenplayIds = ScreenplayIdsSample.BreakingBad
    )

    val Dexter = TvShowIdWithPersonalRating(
        personalRating = Rating.of(8).getOrThrow(),
        screenplayIds = ScreenplayIdsSample.Dexter
    )

    val Grimm = TvShowIdWithPersonalRating(
        personalRating = Rating.of(9).getOrThrow(),
        screenplayIds = ScreenplayIdsSample.Grimm
    )

    val Inception = MovieIdWithPersonalRating(
        personalRating = Rating.of(9).getOrThrow(),
        screenplayIds = ScreenplayIdsSample.Inception
    )

    val TheWolfOfWallStreet = MovieIdWithPersonalRating(
        personalRating = Rating.of(8).getOrThrow(),
        screenplayIds = ScreenplayIdsSample.TheWolfOfWallStreet
    )

    val War = MovieIdWithPersonalRating(
        personalRating = Rating.of(6).getOrThrow(),
        screenplayIds = ScreenplayIdsSample.War
    )
}
