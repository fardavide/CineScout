package cinescout.suggestions.domain.model

import cinescout.screenplay.domain.model.TmdbScreenplayId

data class SuggestedMovieId(
    val affinity: Affinity,
    val movieId: TmdbScreenplayId.Movie,
    val source: SuggestionSource
)

fun SuggestedMovieId(movieId: TmdbScreenplayId.Movie, source: SuggestionSource) = SuggestedMovieId(
    affinity = Affinity.from(source),
    movieId = movieId,
    source = source
)
