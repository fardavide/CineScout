package cinescout.tvshows.domain.model

import cinescout.screenplay.domain.model.Genre

data class TvShowWithDetails(
    val tvShow: TvShow,
    val genres: List<Genre>
)
