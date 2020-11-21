package stats.remote

import entities.Either
import entities.NetworkError
import entities.TmdbId
import entities.invoke
import entities.mapRight
import entities.movies.Movie
import kotlinx.coroutines.flow.Flow
import movies.remote.tmdb.mapper.MovieResultMapper
import stats.RemoteStatSource
import util.PagedList
import util.getOrPutIfNotNull
import util.toPagedList

internal class RemoteStatsSourceImpl(
    private val accountService: AccountService,
    private val movieResultMapper: MovieResultMapper
) : RemoteStatSource {

    override fun watchlist(): Flow<Either<NetworkError, PagedList<Movie>>> {
        val cache = mutableMapOf<TmdbId, Movie>()
        return accountService.getPagedMoviesWatchlist().mapRight { pages ->

            val lastPage = requireNotNull(pages.maxByOrNull { it.page }) { "There is no page in watchlist" }
            val (currentPage, totalPages) = lastPage.page to lastPage.totalPages

            val allResults = pages.flatMap { page -> page.results }
            allResults.mapNotNull { result ->
                cache.getOrPutIfNotNull(TmdbId(result.id)) {
                    movieResultMapper { result.toBusinessModel().rightOrNull() }
                }
            }.toPagedList(currentPage, totalPages)
        }
    }

    override suspend fun addToWatchlist(movie: Movie): Either<NetworkError, Unit> =
        accountService.addToWatchList(movie)

    override suspend fun removeFromWatchlist(movie: Movie): Either<NetworkError, Unit> =
        accountService.removeFromWatchlist(movie)
}
