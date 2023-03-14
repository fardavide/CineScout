package cinescout.details.domain.sample

import arrow.core.some
import cinescout.details.domain.model.MovieWithExtras
import cinescout.details.domain.model.TvShowWithExtras
import cinescout.people.domain.sample.ScreenplayCreditsSample
import cinescout.rating.domain.sample.ScreenplayIdWithPersonalRatingSample
import cinescout.screenplay.domain.sample.ScreenplayGenresSample
import cinescout.screenplay.domain.sample.ScreenplayKeywordsSample
import cinescout.screenplay.domain.sample.ScreenplaySample

object ScreenplayWithExtrasSample {

    val BreakingBad = TvShowWithExtras(
        credits = ScreenplayCreditsSample.BreakingBad,
        genres = ScreenplayGenresSample.BreakingBad,
        isInWatchlist = false,
        keywords = ScreenplayKeywordsSample.BreakingBad,
        personalRating = ScreenplayIdWithPersonalRatingSample.BreakingBad.personalRating.some(),
        screenplay = ScreenplaySample.BreakingBad
    )

    val Dexter = TvShowWithExtras(
        credits = ScreenplayCreditsSample.Dexter,
        genres = ScreenplayGenresSample.Dexter,
        isInWatchlist = false,
        keywords = ScreenplayKeywordsSample.Dexter,
        personalRating = ScreenplayIdWithPersonalRatingSample.Dexter.personalRating.some(),
        screenplay = ScreenplaySample.Dexter
    )

    val Grimm = TvShowWithExtras(
        credits = ScreenplayCreditsSample.Grimm,
        genres = ScreenplayGenresSample.Grimm,
        isInWatchlist = true,
        keywords = ScreenplayKeywordsSample.Grimm,
        personalRating = ScreenplayIdWithPersonalRatingSample.Grimm.personalRating.some(),
        screenplay = ScreenplaySample.Grimm
    )

    val Inception = MovieWithExtras(
        credits = ScreenplayCreditsSample.Inception,
        genres = ScreenplayGenresSample.Inception,
        isInWatchlist = true,
        keywords = ScreenplayKeywordsSample.Inception,
        personalRating = ScreenplayIdWithPersonalRatingSample.Inception.personalRating.some(),
        screenplay = ScreenplaySample.Inception
    )

    val TheWolfOfWallStreet = MovieWithExtras(
        credits = ScreenplayCreditsSample.TheWolfOfWallStreet,
        genres = ScreenplayGenresSample.TheWolfOfWallStreet,
        isInWatchlist = false,
        keywords = ScreenplayKeywordsSample.TheWolfOfWallStreet,
        personalRating = ScreenplayIdWithPersonalRatingSample.TheWolfOfWallStreet.personalRating.some(),
        screenplay = ScreenplaySample.TheWolfOfWallStreet
    )

    val War = MovieWithExtras(
        credits = ScreenplayCreditsSample.War,
        genres = ScreenplayGenresSample.War,
        isInWatchlist = false,
        keywords = ScreenplayKeywordsSample.War,
        personalRating = ScreenplayIdWithPersonalRatingSample.War.personalRating.some(),
        screenplay = ScreenplaySample.War
    )
}
