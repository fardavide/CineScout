package cinescout.sync.domain.usecase

import cinescout.CineScoutTestApi
import cinescout.fetchdata.domain.repository.FetchDataRepository
import cinescout.sync.domain.model.NoSyncExpiration
import cinescout.sync.domain.model.RequiredSync
import cinescout.sync.domain.model.SyncStatus
import cinescout.sync.domain.model.SyncWatchlistKey
import cinescout.sync.domain.model.get
import cinescout.sync.domain.repository.SyncRepository
import cinescout.sync.domain.util.with
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.koin.core.annotation.Factory

interface GetWatchlistSyncStatus {

    suspend operator fun invoke(key: SyncWatchlistKey): SyncStatus
}

@Factory
internal class RealGetWatchlistSyncStatus(
    private val fetchDataRepository: FetchDataRepository,
    private val syncRepository: SyncRepository
) : GetWatchlistSyncStatus {

    override suspend fun invoke(key: SyncWatchlistKey): SyncStatus = coroutineScope {
        val fetchDataDeferred = async { fetchDataRepository.get(key, NoSyncExpiration) }
        val lastActivitiesDeferred = async { syncRepository.getLastActivities() }

        lastActivitiesDeferred.await().fold(
            ifLeft = { RequiredSync.Initial },
            ifRight = { lastActivities ->
                val fetchData = fetchDataDeferred.await()
                fetchData with lastActivities.watchlist[key.type]
            }
        )
    }
}

@CineScoutTestApi
class FakeGetWatchlistSyncStatus(private val syncStatus: SyncStatus) : GetWatchlistSyncStatus {

    override suspend fun invoke(key: SyncWatchlistKey) = syncStatus
}
