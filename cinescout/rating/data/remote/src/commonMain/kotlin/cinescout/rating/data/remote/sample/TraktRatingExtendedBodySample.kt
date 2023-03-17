package cinescout.rating.data.remote.sample

import cinescout.rating.data.remote.model.TraktMovieRatingExtendedBody
import cinescout.rating.data.remote.model.TraktTvShowRatingExtendedBody
import cinescout.rating.domain.sample.ScreenplayIdWithPersonalRatingSample
import screenplay.data.remote.trakt.sample.TraktScreenplayExtendedBodySample

object TraktRatingExtendedBodySample {

    val BreakingBad = TraktTvShowRatingExtendedBody(
        tvShow = TraktScreenplayExtendedBodySample.BreakingBad,
        rating = ScreenplayIdWithPersonalRatingSample.BreakingBad.personalRating.intValue
    )

    val Dexter = TraktTvShowRatingExtendedBody(
        tvShow = TraktScreenplayExtendedBodySample.Dexter,
        rating = ScreenplayIdWithPersonalRatingSample.Dexter.personalRating.intValue
    )

    val Grimm = TraktTvShowRatingExtendedBody(
        tvShow = TraktScreenplayExtendedBodySample.Grimm,
        rating = ScreenplayIdWithPersonalRatingSample.Grimm.personalRating.intValue
    )

    val Inception = TraktMovieRatingExtendedBody(
        movie = TraktScreenplayExtendedBodySample.Inception,
        rating = ScreenplayIdWithPersonalRatingSample.Inception.personalRating.intValue
    )

    val TheWolfOfWallStreet = TraktMovieRatingExtendedBody(
        movie = TraktScreenplayExtendedBodySample.TheWolfOfWallStreet,
        rating = ScreenplayIdWithPersonalRatingSample.TheWolfOfWallStreet.personalRating.intValue
    )

    val War = TraktMovieRatingExtendedBody(
        movie = TraktScreenplayExtendedBodySample.War,
        rating = ScreenplayIdWithPersonalRatingSample.War.personalRating.intValue
    )
}
