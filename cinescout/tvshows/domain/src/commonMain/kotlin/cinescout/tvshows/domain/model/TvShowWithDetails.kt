package cinescout.tvshows.domain.model

import cinescout.screenplay.domain.model.Genre
import cinescout.screenplay.domain.model.TvShow

data class TvShowWithDetails(
    val tvShow: TvShow,
    val genres: List<Genre>
)
