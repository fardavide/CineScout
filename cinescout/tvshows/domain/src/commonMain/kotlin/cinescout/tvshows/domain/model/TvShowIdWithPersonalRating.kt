package cinescout.tvshows.domain.model

import cinescout.screenplay.domain.model.Rating

data class TvShowIdWithPersonalRating(
    val tvShowId: TmdbTvShowId,
    val personalRating: Rating
)
