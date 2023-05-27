package cinescout.rating.domain.sample

import cinescout.rating.domain.model.MovieIdWithPersonalRating
import cinescout.rating.domain.model.TvShowIdWithPersonalRating
import cinescout.screenplay.domain.model.getOrThrow
import cinescout.screenplay.domain.sample.ScreenplayIdsSample

object ScreenplayIdWithPersonalRatingSample {

    val BreakingBad = TvShowIdWithPersonalRating(
        personalRating = ScreenplayPersonalRatingSample.BreakingBad.getOrThrow(),
        screenplayIds = ScreenplayIdsSample.BreakingBad
    )

    val Dexter = TvShowIdWithPersonalRating(
        personalRating = ScreenplayPersonalRatingSample.Dexter.getOrThrow(),
        screenplayIds = ScreenplayIdsSample.Dexter
    )

    val Grimm = TvShowIdWithPersonalRating(
        personalRating = ScreenplayPersonalRatingSample.Grimm.getOrThrow(),
        screenplayIds = ScreenplayIdsSample.Grimm
    )

    val Inception = MovieIdWithPersonalRating(
        personalRating = ScreenplayPersonalRatingSample.Inception.getOrThrow(),
        screenplayIds = ScreenplayIdsSample.Inception
    )

    val TheWolfOfWallStreet = MovieIdWithPersonalRating(
        personalRating = ScreenplayPersonalRatingSample.TheWolfOfWallStreet.getOrThrow(),
        screenplayIds = ScreenplayIdsSample.TheWolfOfWallStreet
    )

    val War = MovieIdWithPersonalRating(
        personalRating = ScreenplayPersonalRatingSample.War.getOrThrow(),
        screenplayIds = ScreenplayIdsSample.War
    )
}
