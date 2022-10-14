package cinescout.movies.domain.model

import cinescout.common.model.Genre

data class MovieWithDetails(
    val movie: Movie,
    val genres: List<Genre>
)
