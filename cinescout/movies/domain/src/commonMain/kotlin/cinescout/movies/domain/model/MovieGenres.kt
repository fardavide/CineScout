package cinescout.movies.domain.model

import arrow.core.NonEmptyList
import cinescout.screenplay.domain.model.Genre

data class MovieGenres(
    val movieId: TmdbMovieId,
    val genres: NonEmptyList<Genre>
)
