package cinescout.suggestions.domain.model

import cinescout.screenplay.domain.model.TmdbScreenplayId

sealed interface SuggestedScreenplayId {

    val affinity: Affinity
    val screenplayId: TmdbScreenplayId
    val source: SuggestionSource
}

fun SuggestedScreenplayId(screenplayId: TmdbScreenplayId, source: SuggestionSource) = when (screenplayId) {
    is TmdbScreenplayId.Movie -> SuggestedMovieId(screenplayId, source)
    is TmdbScreenplayId.TvShow -> SuggestedTvShowId(screenplayId, source)
}
