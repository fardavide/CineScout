package cinescout.suggestions.domain.model

import cinescout.tvshows.domain.model.TvShow

data class SuggestedTvShow(
    val affinity: Affinity,
    val tvShow: TvShow,
    val source: SuggestionSource
)

fun SuggestedTvShow(tvShow: TvShow, source: SuggestionSource) = SuggestedTvShow(
    affinity = Affinity.from(source),
    tvShow = tvShow,
    source = source
)
