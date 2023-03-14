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

data class SuggestedMovieId(
    override val affinity: Affinity,
    override val screenplayId: TmdbScreenplayId.Movie,
    override val source: SuggestionSource
) : SuggestedScreenplayId

fun SuggestedMovieId(movieId: TmdbScreenplayId.Movie, source: SuggestionSource) = SuggestedMovieId(
    affinity = Affinity.from(source),
    screenplayId = movieId,
    source = source
)

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
