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
import kotlinx.coroutines.flow.map
import util.flatInterval
import kotlin.time.seconds

internal class StatRepositoryImpl(
    private val localSource: LocalStatSource,
    private val remoteSource: RemoteStatSource
) : StatRepository by localSource {

    // Get
    override fun watchlist(): Flow<Either<ResourceError, Collection<Movie>>> =
        // Observe local - EMIT
        localSource.watchlist().flatMapMerge { listEither ->
            // Each local
            val localMovies = listEither.rightOrNull() ?: emptyList()
            // With internal
            flowOf(listEither) + flatInterval(RefreshInterval) {
                // Get paged remote - EMIT
                remoteSource.watchlist().map {
                    // Each new page
                    it.mapLeft(ResourceError::Network).ifRight { remoteMovies ->
                        // Store cache
                        with(localSource) {
                            addToWatchlist(remoteMovies - localMovies)
                            // Remove only when all the pages are loaded
                            if (remoteMovies.hasMorePages().not())
                                removeFromWatchlist(localMovies - remoteMovies)
                        }
                    }
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
