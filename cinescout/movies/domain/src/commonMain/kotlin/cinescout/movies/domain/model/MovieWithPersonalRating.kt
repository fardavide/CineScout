package cinescout.movies.domain.model

import cinescout.screenplay.domain.model.Rating
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

data class MovieWithPersonalRating(
    val movie: Movie,
    val personalRating: Rating
)

fun List<MovieWithPersonalRating>.ids(): List<MovieIdWithPersonalRating> =
    map { MovieIdWithPersonalRating(it.movie.tmdbId, it.personalRating) }

fun Flow<List<MovieWithPersonalRating>>.ids(): Flow<List<MovieIdWithPersonalRating>> =
    map { movies -> movies.ids() }
