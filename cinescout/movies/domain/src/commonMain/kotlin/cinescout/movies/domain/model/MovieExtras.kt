package cinescout.movies.domain.model

import arrow.core.NonEmptyList
import arrow.core.Option
import cinescout.screenplay.domain.model.Genre
import cinescout.screenplay.domain.model.Rating

data class MovieExtras(
    val credits: MovieCredits,
    val genres: NonEmptyList<Genre>,
    val personalRating: Option<Rating>
)
