package cinescout.tvshows.domain.model

import cinescout.screenplay.domain.model.Rating

data class TvShowWithPersonalRating(
    val tvShow: TvShow,
    val personalRating: Rating
)
