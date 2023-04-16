package cinescout.suggestions.domain.model

import cinescout.screenplay.domain.model.ScreenplayIds

sealed interface SuggestedScreenplayId {

    val affinity: Affinity
    val screenplayIds: ScreenplayIds
    val source: SuggestionSource
}

fun SuggestedScreenplayId(screenplayIds: ScreenplayIds, source: SuggestionSource) = when (screenplayIds) {
    is ScreenplayIds.Movie -> SuggestedMovieId(screenplayIds, source)
    is ScreenplayIds.TvShow -> SuggestedTvShowId(screenplayIds, source)
}

data class SuggestedMovieId(
    override val affinity: Affinity,
    override val screenplayIds: ScreenplayIds.Movie,
    override val source: SuggestionSource
) : SuggestedScreenplayId

fun SuggestedMovieId(movieIds: ScreenplayIds.Movie, source: SuggestionSource) = SuggestedMovieId(
    affinity = Affinity.from(source),
    screenplayIds = movieIds,
    source = source
)

data class SuggestedTvShowId(
    override val affinity: Affinity,
    override val screenplayIds: ScreenplayIds.TvShow,
    override val source: SuggestionSource
) : SuggestedScreenplayId

fun SuggestedTvShowId(tvShowIds: ScreenplayIds.TvShow, source: SuggestionSource) = SuggestedTvShowId(
    affinity = Affinity.from(source),
    screenplayIds = tvShowIds,
    source = source
)
