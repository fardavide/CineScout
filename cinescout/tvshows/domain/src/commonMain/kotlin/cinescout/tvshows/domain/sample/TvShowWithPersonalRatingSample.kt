package cinescout.tvshows.domain.sample

import cinescout.screenplay.domain.model.Rating
import cinescout.screenplay.domain.model.getOrThrow
import cinescout.screenplay.domain.sample.ScreenplaySample
import cinescout.tvshows.domain.model.TvShowWithPersonalRating

object TvShowWithPersonalRatingSample {

    val BreakingBad = TvShowWithPersonalRating(
        tvShow = ScreenplaySample.BreakingBad,
        personalRating = Rating.of(7).getOrThrow()
    )

    val Dexter = TvShowWithPersonalRating(
        tvShow = ScreenplaySample.Dexter,
        personalRating = Rating.of(8).getOrThrow()
    )

    val Grimm = TvShowWithPersonalRating(
        tvShow = ScreenplaySample.Grimm,
        personalRating = Rating.of(9).getOrThrow()
    )
}
