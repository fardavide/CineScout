package cinescout.tvshows.domain.model

import arrow.core.NonEmptyList
import cinescout.common.model.Genre

data class TvShowGenres(
    val tvShowId: TmdbTvShowId,
    val genres: NonEmptyList<Genre>
)
