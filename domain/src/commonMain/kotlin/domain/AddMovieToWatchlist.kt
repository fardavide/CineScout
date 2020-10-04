package domain

import entities.UserRating
import entities.movies.Movie
import entities.stats.StatRepository

/**
 * Add a [Movie] to watchlist and also rate it positively
 */
class AddMovieToWatchlist(
    private val stats: StatRepository
) {

    suspend operator fun invoke(movie: Movie) {
        stats.addToWatchlist(movie)
        stats.rate(movie, UserRating.Positive)
    }

}
