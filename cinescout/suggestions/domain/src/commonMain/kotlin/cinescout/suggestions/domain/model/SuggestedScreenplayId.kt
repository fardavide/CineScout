package cinescout.suggestions.domain.model

import cinescout.screenplay.domain.model.ids.MovieIds
import cinescout.screenplay.domain.model.ids.ScreenplayIds
import cinescout.screenplay.domain.model.ids.TvShowIds

sealed interface SuggestedScreenplayId {

    val affinity: Affinity
    val screenplayIds: ScreenplayIds
    val source: SuggestionSource
}

fun SuggestedScreenplayId(screenplayIds: ScreenplayIds, source: SuggestionSource) = when (screenplayIds) {
    is MovieIds -> SuggestedMovieId(screenplayIds, source)
    is TvShowIds -> SuggestedTvShowId(screenplayIds, source)
}

data class SuggestedMovieId(
    override val affinity: Affinity,
    override val screenplayIds: MovieIds,
    override val source: SuggestionSource
) : SuggestedScreenplayId

fun SuggestedMovieId(movieIds: MovieIds, source: SuggestionSource) = SuggestedMovieId(
    affinity = Affinity.from(source),
    screenplayIds = movieIds,
    source = source
)

data class SuggestedTvShowId(
    override val affinity: Affinity,
    override val screenplayIds: TvShowIds,
    override val source: SuggestionSource
) : SuggestedScreenplayId

fun SuggestedTvShowId(tvShowIds: TvShowIds, source: SuggestionSource) = SuggestedTvShowId(
    affinity = Affinity.from(source),
    screenplayIds = tvShowIds,
    source = source
)

fun List<SuggestedScreenplayId>.filterTypes(types: List<SuggestionSourceType>): List<SuggestedScreenplayId> =
    filter { it.source.type() in types }
