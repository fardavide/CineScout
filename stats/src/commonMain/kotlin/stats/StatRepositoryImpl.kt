package stats

import entities.Either
import entities.ResourceError
import entities.movies.Movie
import entities.plus
import entities.stats.StatRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flowOf
import util.interval
import kotlin.time.seconds

internal class StatRepositoryImpl(
    private val localSource: LocalStatSource,
    private val remoteSource: RemoteStatSource
) : StatRepository by localSource {

    // Get
    override fun watchlist(): Flow<Either<ResourceError, Collection<Movie>>> =
        localSource.watchlist().flatMapMerge { listEither ->
            flowOf(listEither) + interval(RefreshInterval) {
                remoteSource.watchlist().mapLeft(ResourceError::Network).ifRight {
                    val localMovies = listEither.rightOrNull() ?: emptyList()
                    localSource.addToWatchlist(it - localMovies)
                }
            }
        }.distinctUntilChanged()

    override suspend fun addToWatchlist(movie: Movie) {
        localSource.addToWatchlist(movie)
        remoteSource.addToWatchlist(movie)
    }

    override suspend fun removeFromWatchlist(movie: Movie) {
        localSource.removeFromWatchlist(movie)
        remoteSource.removeFromWatchlist(movie)
    }

    private companion object {
        val RefreshInterval = 20.seconds
    }
}
