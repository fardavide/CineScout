package cinescout.tvshows.domain.model

import cinescout.common.model.Rating

data class TvShowIdWithPersonalRating(
    val tvShowId: TmdbTvShowId,
    val personalRating: Rating
)
