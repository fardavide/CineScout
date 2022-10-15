package cinescout.tvshows.domain.testdata

import cinescout.common.model.Rating
import cinescout.common.model.getOrThrow
import cinescout.tvshows.domain.model.TvShowWithPersonalRating

object TvShowWithPersonalRatingTestData {

    val Grimm = TvShowWithPersonalRating(
        tvShow = TvShowTestData.Grimm,
        personalRating = Rating.of(9).getOrThrow()
    )
}
