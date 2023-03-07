package cinescout.tvshows.domain.sample

import cinescout.screenplay.domain.model.Rating
import cinescout.screenplay.domain.model.getOrThrow
import cinescout.tvshows.domain.model.TvShowIdWithPersonalRating

object TvShowIdWithPersonalRatingSample {

    val BreakingBad = TvShowIdWithPersonalRating(
        tvShowId = TmdbTvShowIdSample.BreakingBad,
        personalRating = Rating.of(7).getOrThrow()
    )

    val Dexter = TvShowIdWithPersonalRating(
        tvShowId = TmdbTvShowIdSample.Dexter,
        personalRating = Rating.of(8).getOrThrow()
    )

    val Grimm = TvShowIdWithPersonalRating(
        tvShowId = TmdbTvShowIdSample.Grimm,
        personalRating = Rating.of(9).getOrThrow()
    )
}
