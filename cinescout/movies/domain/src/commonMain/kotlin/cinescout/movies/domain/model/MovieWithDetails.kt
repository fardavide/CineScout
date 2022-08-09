package cinescout.movies.domain.model

import arrow.core.NonEmptyList

data class MovieWithDetails(
    val movie: Movie,
    val genres: NonEmptyList<Genre>
)
