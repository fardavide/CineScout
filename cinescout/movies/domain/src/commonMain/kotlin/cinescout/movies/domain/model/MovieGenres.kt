package cinescout.movies.domain.model

import arrow.core.NonEmptyList

data class MovieGenres(
    val movieId: TmdbMovieId,
    val genres: NonEmptyList<Genre>
)
