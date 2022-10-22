package cinescout.tvshows.domain.testdata

import cinescout.common.model.Rating
import cinescout.common.model.getOrThrow
import cinescout.tvshows.domain.model.TvShowWithPersonalRating

object TvShowWithPersonalRatingTestData {

    val BreakingBad = TvShowWithPersonalRating(
        tvShow = TvShowTestData.BreakingBad,
        personalRating = Rating.of(7).getOrThrow()
    )

    val Dexter = TvShowWithPersonalRating(
        tvShow = TvShowTestData.Dexter,
        personalRating = Rating.of(8).getOrThrow()
    )

    val Grimm = TvShowWithPersonalRating(
        tvShow = TvShowTestData.Grimm,
        personalRating = Rating.of(9).getOrThrow()
    )
}
