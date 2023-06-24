package cinescout.sync.domain.usecase

import cinescout.CineScoutTestApi
import cinescout.fetchdata.domain.repository.FetchDataRepository
import cinescout.sync.domain.model.SyncExpiration
import cinescout.sync.domain.model.SyncStatus
import cinescout.sync.domain.model.SyncWatchlistKey
import cinescout.sync.domain.util.toSyncStatus
import org.koin.core.annotation.Factory

interface GetWatchlistSyncStatus {

    suspend operator fun invoke(key: SyncWatchlistKey): SyncStatus
}

// TODO: use with last activities
@Factory
internal class RealGetWatchlistSyncStatus(
    private val fetchDataRepository: FetchDataRepository
) : GetWatchlistSyncStatus {

    override suspend fun invoke(key: SyncWatchlistKey): SyncStatus =
        fetchDataRepository.get(key, SyncExpiration.Watchlist).toSyncStatus()
}

@CineScoutTestApi
class FakeGetWatchlistSyncStatus(private val syncStatus: SyncStatus) : GetWatchlistSyncStatus {

    override suspend fun invoke(key: SyncWatchlistKey) = syncStatus
}
