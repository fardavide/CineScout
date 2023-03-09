package cinescout.suggestions.domain.model

import cinescout.screenplay.domain.model.TmdbScreenplayId

data class SuggestedTvShowId(
    override val affinity: Affinity,
    override val screenplayId: TmdbScreenplayId.TvShow,
    override val source: SuggestionSource
) : SuggestedScreenplayId

fun SuggestedTvShowId(tvShowId: TmdbScreenplayId.TvShow, source: SuggestionSource) = SuggestedTvShowId(
    affinity = Affinity.from(source),
    screenplayId = tvShowId,
    source = source
)
