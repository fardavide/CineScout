package entities.stats

import entities.Actor
import entities.FiveYearRange
import entities.Genre
import entities.Rating
import entities.Rating.Negative
import entities.Rating.Positive
import entities.movies.Movie
import kotlinx.coroutines.flow.Flow

interface StatRepository {

    // Get
    suspend fun topActors(limit: UInt): Collection<Actor>
    suspend fun topGenres(limit: UInt): Collection<Genre>
    suspend fun topYears(limit: UInt): Collection<FiveYearRange>
    suspend fun ratedMovies(): Collection<Pair<Movie, Rating>>
    fun rating(movie: Movie): Flow<Rating>
    suspend fun watchlist(): Collection<Movie>
    fun isInWatchlist(movie: Movie): Flow<Boolean>

    // Insert
    suspend fun rate(movie: Movie, rating: Rating)
    suspend fun addToWatchlist(movie: Movie)
    suspend fun removeFromWatchlist(movie: Movie)
}

val Collection<Pair<Movie, Rating>>.movies get() =
    map { it.first }

fun <T> Collection<Pair<T, Rating>>.positives() = filter { it.second == Positive }.map { it.first }
fun <T> Collection<Pair<T, Rating>>.negatives() = filter { it.second == Negative }.map { it.first }
