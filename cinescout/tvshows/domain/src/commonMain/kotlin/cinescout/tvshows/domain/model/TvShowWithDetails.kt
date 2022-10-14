package cinescout.tvshows.domain.model

import cinescout.common.model.Genre

data class TvShowWithDetails(
    val tvShow: TvShow,
    val genres: List<Genre>
)
