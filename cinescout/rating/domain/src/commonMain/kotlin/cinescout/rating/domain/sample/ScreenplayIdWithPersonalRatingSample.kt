package cinescout.rating.domain.sample

import cinescout.rating.domain.model.MovieIdWithPersonalRating
import cinescout.rating.domain.model.TvShowIdWithPersonalRating
import cinescout.screenplay.domain.model.Rating
import cinescout.screenplay.domain.model.getOrThrow
import cinescout.screenplay.domain.sample.TmdbScreenplayIdSample

object ScreenplayIdWithPersonalRatingSample {

    val BreakingBad = TvShowIdWithPersonalRating(
        personalRating = Rating.of(7).getOrThrow(),
        screenplayId = TmdbScreenplayIdSample.BreakingBad
    )

    val Dexter = TvShowIdWithPersonalRating(
        personalRating = Rating.of(8).getOrThrow(),
        screenplayId = TmdbScreenplayIdSample.Dexter
    )

    val Grimm = TvShowIdWithPersonalRating(
        personalRating = Rating.of(9).getOrThrow(),
        screenplayId = TmdbScreenplayIdSample.Grimm
    )

    val Inception = MovieIdWithPersonalRating(
        personalRating = Rating.of(9).getOrThrow(),
        screenplayId = TmdbScreenplayIdSample.Inception
    )

    val TheWolfOfWallStreet = MovieIdWithPersonalRating(
        personalRating = Rating.of(8).getOrThrow(),
        screenplayId = TmdbScreenplayIdSample.TheWolfOfWallStreet
    )

    val War = MovieIdWithPersonalRating(
        personalRating = Rating.of(6).getOrThrow(),
        screenplayId = TmdbScreenplayIdSample.War
    )
}
