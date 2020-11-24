package stats.remote

import entities.Either
import entities.NetworkError
import entities.TmdbId
import entities.flatMap
import entities.flatMapLeft
import entities.invoke
import entities.mapRight
import entities.movies.Movie
import entities.then
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import movies.remote.tmdb.mapper.MovieResultMapper
import stats.RemoteStatSource
import util.PagedList
import util.getOrPutIfNotNull
import util.toPagedList

internal class RemoteStatsSourceImpl(
    private val tmdbSource: TmdbRemoteStatSource,
    private val traktSource: TraktRemoteStatSource
) : RemoteStatSource {

    override fun watchlist(): Flow<Either<NetworkError, PagedList<Movie>>> =
        combine(traktSource.watchlist(), tmdbSource.watchlist()) { traktWatchlistEither, tmdbWatchlistEither ->
            traktWatchlistEither.flatMap { trakt ->
                tmdbWatchlistEither.map { tmdb ->
                    PagedList(
                        (trakt + tmdb).distinct(),
                        trakt.currentPage + tmdb.currentPage,
                        trakt.totalPages + tmdb.totalPages
                    )
                }
            }
        }

    override suspend fun addToWatchlist(movie: Movie): Either<NetworkError, Unit> =
        traktSource.addToWatchlist(movie) then tmdbSource.addToWatchlist(movie)

    override suspend fun removeFromWatchlist(movie: Movie): Either<NetworkError, Unit> =
        traktSource.removeFromWatchlist(movie) then tmdbSource.removeFromWatchlist(movie)
}
