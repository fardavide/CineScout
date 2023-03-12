package cinescout.movies.domain.model

import cinescout.screenplay.domain.model.Genre
import cinescout.screenplay.domain.model.Movie

data class MovieWithDetails(
    val movie: Movie,
    val genres: List<Genre>
)
