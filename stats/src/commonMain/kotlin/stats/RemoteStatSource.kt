package stats

import entities.Either
import entities.NetworkError
import entities.movies.Movie
import kotlinx.coroutines.flow.Flow
import util.PagedList

interface RemoteStatSource {

    // Get
    suspend fun watchlist(): Flow<Either<NetworkError, PagedList<Movie>>>

    // Insert
    suspend fun addToWatchlist(movie: Movie): Either<NetworkError, Unit>
    suspend fun removeFromWatchlist(movie: Movie): Either<NetworkError, Unit>
}
