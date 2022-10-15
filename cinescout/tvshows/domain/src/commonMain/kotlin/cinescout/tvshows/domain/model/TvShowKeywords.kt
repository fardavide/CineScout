package cinescout.tvshows.domain.model

import cinescout.common.model.Keyword

data class TvShowKeywords(
    val tvShowId: TmdbTvShowId,
    val keywords: List<Keyword>
)
