package domain.stats

import entities.field.UserRating
import entities.movies.Movie
import entities.stats.StatRepository

class RateMovie(
    private val stats: StatRepository
) {

    suspend operator fun invoke(movie: Movie, rating: UserRating): Unit =
        stats.rate(movie, rating)

}
