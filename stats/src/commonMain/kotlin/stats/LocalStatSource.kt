package stats

import entities.movies.Movie
import entities.stats.StatRepository

interface LocalStatSource : StatRepository {

    suspend fun addToWatchlist(movies: Collection<Movie>)
}
