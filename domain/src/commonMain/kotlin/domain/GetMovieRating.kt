package domain

import entities.Rating
import entities.movies.Movie
import entities.stats.StatRepository
import kotlinx.coroutines.flow.Flow

class GetMovieRating(
    private val stats: StatRepository
) {

    operator fun invoke(movie: Movie): Flow<Rating> =
        stats.rating(movie)
}
