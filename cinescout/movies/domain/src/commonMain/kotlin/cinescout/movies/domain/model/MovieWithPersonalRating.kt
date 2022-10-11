package cinescout.movies.domain.model

import cinescout.common.model.Rating

data class MovieWithPersonalRating(
    val movie: Movie,
    val personalRating: Rating
)
