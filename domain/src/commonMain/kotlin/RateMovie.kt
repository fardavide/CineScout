import movies.Movie
import stats.StatRepository

class RateMovie(
    private val stats: StatRepository
) {

    suspend operator fun invoke(movie: Movie, rating: Rating): Unit =
        stats.rate(movie, rating)

}
