package cinescout.watchlist.data.mediator

import arrow.core.Either
import cinescout.error.NetworkError
import cinescout.fetchdata.domain.repository.FetchDataRepository
import cinescout.model.handleSkippedAsRight
import cinescout.screenplay.domain.model.ScreenplayTypeFilter
import cinescout.sync.domain.model.RequiredSync
import cinescout.sync.domain.model.SyncWatchlistKey
import cinescout.sync.domain.util.toBookmark
import cinescout.watchlist.data.datasource.LocalWatchlistDataSource
import cinescout.watchlist.data.datasource.RemoteWatchlistDataSource
import cinescout.watchlist.domain.usecase.SyncWatchlist
import org.koin.core.annotation.Factory

@Factory
internal class RealSyncWatchlist(
    private val fetchDataRepository: FetchDataRepository,
    private val localDataSource: LocalWatchlistDataSource,
    private val remoteDataSource: RemoteWatchlistDataSource
) : SyncWatchlist {

    override suspend operator fun invoke(
        type: ScreenplayTypeFilter,
        requiredSync: RequiredSync
    ): Either<NetworkError, Unit> {
        val remoteData = when (requiredSync) {
            RequiredSync.Initial -> remoteDataSource.getWatchlist(type, 1)
            RequiredSync.Complete -> remoteDataSource.getAllWatchlist(type)
        }
        return remoteData
            .map { list ->
                when (requiredSync) {
                    RequiredSync.Initial -> localDataSource.insertAllWatchlist(list)
                    RequiredSync.Complete -> localDataSource.updateAllWatchlist(list)
                }
            }
            .handleSkippedAsRight()
            .also { fetchDataRepository.set(SyncWatchlistKey(type), requiredSync.toBookmark()) }
    }
}
