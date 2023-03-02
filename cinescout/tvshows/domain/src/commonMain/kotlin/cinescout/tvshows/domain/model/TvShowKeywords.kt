package cinescout.tvshows.domain.model

import cinescout.screenplay.domain.model.Keyword

data class TvShowKeywords(
    val tvShowId: TmdbTvShowId,
    val keywords: List<Keyword>
)
