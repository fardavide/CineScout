package cinescout.suggestions.domain.model

import cinescout.screenplay.domain.model.TmdbScreenplayId

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
