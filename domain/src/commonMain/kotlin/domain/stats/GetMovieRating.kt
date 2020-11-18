package domain.stats

import entities.model.UserRating
import entities.movies.Movie
import entities.stats.StatRepository
import kotlinx.coroutines.flow.Flow

class GetMovieRating(
    private val stats: StatRepository
) {

    operator fun invoke(movie: Movie): Flow<UserRating> =
        stats.rating(movie)
}
