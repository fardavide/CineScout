package domain.stats

import entities.movies.Movie
import entities.stats.StatRepository

class RemoveMovieFromWatchlist (
    private val stats: StatRepository
) {

    suspend operator fun invoke(movie: Movie) {
        stats.removeFromWatchlist(movie)
    }
}
