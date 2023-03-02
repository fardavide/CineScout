package cinescout.movies.domain.model

import cinescout.screenplay.domain.model.Rating

data class MovieIdWithPersonalRating(
    val movieId: TmdbMovieId,
    val personalRating: Rating
)
