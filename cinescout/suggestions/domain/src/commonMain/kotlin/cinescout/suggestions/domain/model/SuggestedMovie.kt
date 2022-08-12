package cinescout.suggestions.domain.model

import arrow.core.Option
import cinescout.movies.domain.model.Movie

data class SuggestedMovie(
    val movie: Movie,
    val affinity: Option<Affinity>
)
