package cinescout.rating.data.remote.sample

import cinescout.rating.data.remote.model.TraktMovieRatingMetadataBody
import cinescout.rating.domain.sample.ScreenplayIdWithPersonalRatingSample
import screenplay.data.remote.trakt.sample.TraktMovieMetadataBodySample

object TraktMovieRatingMetadataBodySample {

    val Inception = TraktMovieRatingMetadataBody(
        movie = TraktMovieMetadataBodySample.Inception,
        rating = ScreenplayIdWithPersonalRatingSample.Inception.personalRating.intValue
    )

    val TheWolfOfWallStreet = TraktMovieRatingMetadataBody(
        movie = TraktMovieMetadataBodySample.TheWolfOfWallStreet,
        rating = ScreenplayIdWithPersonalRatingSample.TheWolfOfWallStreet.personalRating.intValue
    )

    val War = TraktMovieRatingMetadataBody(
        movie = TraktMovieMetadataBodySample.War,
        rating = ScreenplayIdWithPersonalRatingSample.War.personalRating.intValue
    )
}
