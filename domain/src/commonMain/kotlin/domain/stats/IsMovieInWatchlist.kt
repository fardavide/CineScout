package domain.stats

import entities.movies.Movie
import entities.stats.StatRepository
import kotlinx.coroutines.flow.Flow

class IsMovieInWatchlist(
    private val stats: StatRepository
) {

    operator fun invoke(movie: Movie): Flow<Boolean> =
        stats.isInWatchlist(movie)
}
