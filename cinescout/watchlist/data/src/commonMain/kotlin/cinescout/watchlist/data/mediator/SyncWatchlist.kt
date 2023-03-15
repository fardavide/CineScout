package cinescout.watchlist.data.mediator

import arrow.core.Either
import cinescout.error.NetworkError
import cinescout.model.handleSkippedAsRight
import cinescout.screenplay.domain.model.ScreenplayType
import cinescout.watchlist.data.datasource.LocalWatchlistDataSource
import cinescout.watchlist.data.datasource.RemoteWatchlistDataSource
import org.koin.core.annotation.Factory

@Factory
internal class SyncWatchlist(
    private val localDataSource: LocalWatchlistDataSource,
    private val remoteDataSource: RemoteWatchlistDataSource
) {

    suspend operator fun invoke(listType: ScreenplayType, page: Int): Either<NetworkError, Unit> {
        return remoteDataSource.getWatchlist(listType, page)
            .map { localDataSource.insertAllWatchlist(it) }
            .handleSkippedAsRight()
    }
}
