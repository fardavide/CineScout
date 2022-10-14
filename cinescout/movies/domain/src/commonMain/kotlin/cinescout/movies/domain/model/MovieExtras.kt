package cinescout.movies.domain.model

import arrow.core.NonEmptyList
import arrow.core.Option
import cinescout.common.model.Genre
import cinescout.common.model.Rating

data class MovieExtras(
    val credits: MovieCredits,
    val genres: NonEmptyList<Genre>,
    val personalRating: Option<Rating>
)
