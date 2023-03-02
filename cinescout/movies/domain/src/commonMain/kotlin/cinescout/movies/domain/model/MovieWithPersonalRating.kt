package cinescout.movies.domain.model

import cinescout.screenplay.domain.model.Rating

data class MovieWithPersonalRating(
    val movie: Movie,
    val personalRating: Rating
)
