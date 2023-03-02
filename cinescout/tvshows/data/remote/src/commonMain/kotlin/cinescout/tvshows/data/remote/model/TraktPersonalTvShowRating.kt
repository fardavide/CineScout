package cinescout.tvshows.data.remote.model

import cinescout.screenplay.domain.model.Rating
import cinescout.tvshows.domain.model.TmdbTvShowId

data class TraktPersonalTvShowRating(
    val tmdbId: TmdbTvShowId,
    val rating: Rating
)
