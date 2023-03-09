package cinescout.suggestions.domain.model

import cinescout.screenplay.domain.model.TmdbScreenplayId

data class SuggestedTvShowId(
    val affinity: Affinity,
    val tvShowId: TmdbScreenplayId.TvShow,
    val source: SuggestionSource
)

fun SuggestedTvShowId(tvShowId: TmdbScreenplayId.TvShow, source: SuggestionSource) = SuggestedTvShowId(
    affinity = Affinity.from(source),
    tvShowId = tvShowId,
    source = source
)
