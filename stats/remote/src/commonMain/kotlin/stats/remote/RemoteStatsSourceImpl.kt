package stats.remote

import entities.Either
import entities.NetworkError
import entities.movies.Movie
import movies.remote.tmdb.mapper.MoviePageResultMapper
import movies.remote.tmdb.mapper.map
import stats.RemoteStatSource

internal class RemoteStatsSourceImpl(
    private val accountService: AccountService,
    private val moviePageResultMapper: MoviePageResultMapper
) : RemoteStatSource {

    override suspend fun watchlist(): Either<NetworkError, Collection<Movie>> =
        accountService.getMoviesWatchlist().map(moviePageResultMapper) { it.toBusinessModel() }

    override suspend fun addToWatchlist(movie: Movie): Either<NetworkError, Unit> =
        accountService.addToWatchList(movie)

    override suspend fun removeFromWatchlist(movie: Movie): Either<NetworkError, Unit> =
        accountService.removeFromWatchlist(movie)
}
