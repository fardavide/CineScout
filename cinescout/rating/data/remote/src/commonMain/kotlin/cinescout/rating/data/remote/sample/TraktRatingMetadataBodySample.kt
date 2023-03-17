package cinescout.rating.data.remote.sample

import cinescout.rating.data.remote.model.TraktMovieRatingMetadataBody
import cinescout.rating.data.remote.model.TraktTvShowRatingMetadataBody
import cinescout.rating.domain.sample.ScreenplayIdWithPersonalRatingSample
import screenplay.data.remote.trakt.sample.TraktScreenplayMetadataBodySample

object TraktRatingMetadataBodySample {

    val BreakingBad = TraktTvShowRatingMetadataBody(
        tvShow = TraktScreenplayMetadataBodySample.BreakingBad,
        rating = ScreenplayIdWithPersonalRatingSample.BreakingBad.personalRating.intValue
    )

    val Dexter = TraktTvShowRatingMetadataBody(
        tvShow = TraktScreenplayMetadataBodySample.Dexter,
        rating = ScreenplayIdWithPersonalRatingSample.Dexter.personalRating.intValue
    )

    val Grimm = TraktTvShowRatingMetadataBody(
        tvShow = TraktScreenplayMetadataBodySample.Grimm,
        rating = ScreenplayIdWithPersonalRatingSample.Grimm.personalRating.intValue
    )

    val Inception = TraktMovieRatingMetadataBody(
        movie = TraktScreenplayMetadataBodySample.Inception,
        rating = ScreenplayIdWithPersonalRatingSample.Inception.personalRating.intValue
    )

    val TheWolfOfWallStreet = TraktMovieRatingMetadataBody(
        movie = TraktScreenplayMetadataBodySample.TheWolfOfWallStreet,
        rating = ScreenplayIdWithPersonalRatingSample.TheWolfOfWallStreet.personalRating.intValue
    )

    val War = TraktMovieRatingMetadataBody(
        movie = TraktScreenplayMetadataBodySample.War,
        rating = ScreenplayIdWithPersonalRatingSample.War.personalRating.intValue
    )
}
