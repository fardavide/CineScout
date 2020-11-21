package stats.remote

import entities.Either
import entities.NetworkError
import entities.invoke
import entities.mapRight
import entities.movies.Movie
import kotlinx.coroutines.flow.Flow
import movies.remote.tmdb.mapper.MoviePageResultMapper
import stats.RemoteStatSource
import util.PagedList
import util.toPagedList

internal class RemoteStatsSourceImpl(
    private val accountService: AccountService,
    private val moviePageResultMapper: MoviePageResultMapper
) : RemoteStatSource {

    override suspend fun watchlist(): Flow<Either<NetworkError, PagedList<Movie>>> =
        accountService.getPagedMoviesWatchlist().mapRight { pages ->

            val lastPage = requireNotNull(pages.maxByOrNull { it.page }) { "There is no page in watchlist" }
            val (currentPage, totalPages) = lastPage.page to lastPage.totalPages

            pages.flatMap { page -> moviePageResultMapper { page.toBusinessModel() } }
                .toPagedList(currentPage, totalPages)
        }

    override suspend fun addToWatchlist(movie: Movie): Either<NetworkError, Unit> =
        accountService.addToWatchList(movie)

    override suspend fun removeFromWatchlist(movie: Movie): Either<NetworkError, Unit> =
        accountService.removeFromWatchlist(movie)
}
