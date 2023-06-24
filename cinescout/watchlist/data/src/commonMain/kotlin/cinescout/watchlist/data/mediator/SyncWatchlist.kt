package cinescout.watchlist.data.mediator

import arrow.core.Either
import cinescout.error.NetworkError
import cinescout.model.handleSkippedAsRight
import cinescout.screenplay.domain.model.ScreenplayTypeFilter
import cinescout.watchlist.data.datasource.LocalWatchlistDataSource
import cinescout.watchlist.data.datasource.RemoteWatchlistDataSource
import org.koin.core.annotation.Factory

@Factory
internal class SyncWatchlist(
    private val localDataSource: LocalWatchlistDataSource,
    private val remoteDataSource: RemoteWatchlistDataSource
) {

    suspend operator fun invoke(type: ScreenplayTypeFilter, syncType: Type): Either<NetworkError, Unit> {
        val remoteData = when (syncType) {
            Type.Initial -> remoteDataSource.getWatchlist(type, 1)
            Type.Complete -> remoteDataSource.getAllWatchlist(type)
        }
        return remoteData
            .map { localDataSource.insertAllWatchlist(it) }
            .handleSkippedAsRight()
    }

    enum class Type {
        Initial,
        Complete
    }
}
