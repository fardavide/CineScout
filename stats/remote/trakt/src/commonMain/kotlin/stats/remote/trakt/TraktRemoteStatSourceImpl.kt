package stats.remote.trakt

import domain.FindMovie
import entities.Either
import entities.NetworkError
import entities.TmdbId
import entities.movies.Movie
import kotlinx.coroutines.flow.Flow
import stats.remote.TraktRemoteStatSource
import util.PagedList

internal class TraktRemoteStatSourceImpl(
    private val syncService: SyncService,
    private val findMovie: FindMovie
) : TraktRemoteStatSource {

    override fun watchlist(): Flow<Either<NetworkError, PagedList<Movie>>> = Either.fixFlow {
        val (traktWatchlist) = syncService.getMoviesWatchlist()
        val watchlist = traktWatchlist.mapNotNull { findMovie(TmdbId(it.movie.ids.tmdb)) }
        emit(PagedList(watchlist, 1, 1))
    }

    override suspend fun addToWatchlist(movie: Movie): Either<NetworkError, Unit> =
        syncService.addToWatchList(movie)

    override suspend fun removeFromWatchlist(movie: Movie): Either<NetworkError, Unit> =
        syncService.removeFromWatchlist(movie)
}
