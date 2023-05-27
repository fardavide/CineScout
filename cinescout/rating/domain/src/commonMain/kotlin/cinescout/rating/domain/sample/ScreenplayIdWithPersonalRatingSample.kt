package cinescout.rating.domain.sample

import cinescout.rating.domain.model.MovieIdWithPersonalRating
import cinescout.rating.domain.model.TvShowIdWithPersonalRating
import cinescout.screenplay.domain.sample.ScreenplayIdsSample

object ScreenplayIdWithPersonalRatingSample {

    val BreakingBad = TvShowIdWithPersonalRating(
        personalRating = ScreenplayPersonalRatingSample.BreakingBad,
        screenplayIds = ScreenplayIdsSample.BreakingBad
    )

    val Dexter = TvShowIdWithPersonalRating(
        personalRating = ScreenplayPersonalRatingSample.Dexter,
        screenplayIds = ScreenplayIdsSample.Dexter
    )

    val Grimm = TvShowIdWithPersonalRating(
        personalRating = ScreenplayPersonalRatingSample.Grimm,
        screenplayIds = ScreenplayIdsSample.Grimm
    )

    val Inception = MovieIdWithPersonalRating(
        personalRating = ScreenplayPersonalRatingSample.Inception,
        screenplayIds = ScreenplayIdsSample.Inception
    )

    val TheWolfOfWallStreet = MovieIdWithPersonalRating(
        personalRating = ScreenplayPersonalRatingSample.TheWolfOfWallStreet,
        screenplayIds = ScreenplayIdsSample.TheWolfOfWallStreet
    )

    val War = MovieIdWithPersonalRating(
        personalRating = ScreenplayPersonalRatingSample.War,
        screenplayIds = ScreenplayIdsSample.War
    )
}
