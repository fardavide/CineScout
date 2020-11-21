package stats

import entities.Either
import entities.NetworkError
import entities.movies.Movie

interface RemoteStatSource {

    // Get
    suspend fun watchlist(): Either<NetworkError, Collection<Movie>>

    // Insert
    suspend fun addToWatchlist(movie: Movie): Either<NetworkError, Unit>
    suspend fun removeFromWatchlist(movie: Movie): Either<NetworkError, Unit>
}
