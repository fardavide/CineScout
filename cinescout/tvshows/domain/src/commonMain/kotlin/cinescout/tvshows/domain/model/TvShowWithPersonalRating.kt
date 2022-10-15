package cinescout.tvshows.domain.model

import cinescout.common.model.Rating

data class TvShowWithPersonalRating(
    val tvShow: TvShow,
    val personalRating: Rating
)
