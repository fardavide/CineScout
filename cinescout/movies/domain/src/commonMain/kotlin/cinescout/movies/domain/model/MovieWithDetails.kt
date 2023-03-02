package cinescout.movies.domain.model

import cinescout.screenplay.domain.model.Genre

data class MovieWithDetails(
    val movie: Movie,
    val genres: List<Genre>
)
