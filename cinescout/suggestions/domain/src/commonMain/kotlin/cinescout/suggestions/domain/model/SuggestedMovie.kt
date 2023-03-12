package cinescout.suggestions.domain.model

import cinescout.screenplay.domain.model.Movie

data class SuggestedMovie(
    val affinity: Affinity,
    val movie: Movie,
    val source: SuggestionSource
)

fun SuggestedMovie(movie: Movie, source: SuggestionSource) = SuggestedMovie(
    affinity = Affinity.from(source),
    movie = movie,
    source = source
)
