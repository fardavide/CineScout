package cinescout.watchlist.data.mediator

import arrow.core.Either
import arrow.core.left
import cinescout.error.NetworkError
import cinescout.error.handleNotFoundAsEmptyList
import cinescout.model.NetworkOperation
import cinescout.model.handleSkippedAsEmptyList
import cinescout.model.handleSkippedAsRight
import cinescout.screenplay.domain.model.Screenplay
import cinescout.screenplay.domain.model.ScreenplayType
import cinescout.utils.kotlin.plus
import cinescout.watchlist.data.datasource.LocalWatchlistDataSource
import cinescout.watchlist.data.datasource.RemoteWatchlistDataSource
import org.koin.core.annotation.Factory

@Factory
internal class SyncWatchlist(
    private val localDataSource: LocalWatchlistDataSource,
    private val remoteDataSource: RemoteWatchlistDataSource
) {

    suspend operator fun invoke(listType: ScreenplayType, page: Int): Either<NetworkError, Unit> {
        return when (listType) {
            ScreenplayType.All -> {
                val (movies, isMoviesNotFound) = remoteDataSource.getWatchlistMovies(page).handleNotFound()
                val (tvShows, isTvShowsNotFound) = remoteDataSource.getWatchlistTvShows(page).handleNotFound()
                if (isMoviesNotFound && isTvShowsNotFound) return NetworkError.NotFound.left()

                (movies + tvShows).map { localDataSource.insertAllWatchlist(it) }
            }

            ScreenplayType.Movies -> remoteDataSource.getWatchlistMovies(page)
                .map { localDataSource.insertAllWatchlist(it) }
                .handleSkippedAsRight()

            ScreenplayType.TvShows -> remoteDataSource.getWatchlistTvShows(page)
                .map { localDataSource.insertAllWatchlist(it) }
                .handleSkippedAsRight()
        }
    }

    private fun Either<NetworkOperation, List<Screenplay>>.handleNotFound() =
        with(handleSkippedAsEmptyList()) {
            handleNotFoundAsEmptyList() to (this is Either.Left<NetworkError> && this.value is NetworkError.NotFound)
        }
}
