package cinescout.movies.domain.model

import cinescout.common.model.Rating

data class MovieIdWithPersonalRating(
    val movieId: TmdbMovieId,
    val personalRating: Rating
)
