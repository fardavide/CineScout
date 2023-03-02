package cinescout.tvshows.domain.sample

import cinescout.screenplay.domain.model.Rating
import cinescout.screenplay.domain.model.getOrThrow
import cinescout.tvshows.domain.model.TvShowWithPersonalRating

object TvShowWithPersonalRatingSample {

    val BreakingBad = TvShowWithPersonalRating(
        tvShow = TvShowSample.BreakingBad,
        personalRating = Rating.of(7).getOrThrow()
    )

    val Dexter = TvShowWithPersonalRating(
        tvShow = TvShowSample.Dexter,
        personalRating = Rating.of(8).getOrThrow()
    )

    val Grimm = TvShowWithPersonalRating(
        tvShow = TvShowSample.Grimm,
        personalRating = Rating.of(9).getOrThrow()
    )
}
