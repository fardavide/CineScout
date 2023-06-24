package cinescout.sync.domain.usecase

import cinescout.CineScoutTestApi
import cinescout.fetchdata.domain.repository.FetchDataRepository
import cinescout.sync.domain.model.NoSyncExpiration
import cinescout.sync.domain.model.RequiredSync
import cinescout.sync.domain.model.SyncRatingsKey
import cinescout.sync.domain.model.SyncStatus
import cinescout.sync.domain.model.get
import cinescout.sync.domain.repository.SyncRepository
import cinescout.sync.domain.util.with
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.koin.core.annotation.Factory

interface GetRatingsSyncStatus {

    suspend operator fun invoke(key: SyncRatingsKey): SyncStatus
}

@Factory
internal class RealGetRatingsSyncStatus(
    private val fetchDataRepository: FetchDataRepository,
    private val syncRepository: SyncRepository
) : GetRatingsSyncStatus {

    override suspend fun invoke(key: SyncRatingsKey): SyncStatus = coroutineScope {
        val fetchDataDeferred = async { fetchDataRepository.get(key, NoSyncExpiration) }
        val lastActivitiesDeferred = async { syncRepository.getLastActivities() }

        lastActivitiesDeferred.await().fold(
            ifLeft = { RequiredSync.Initial },
            ifRight = { lastActivities ->
                val fetchData = fetchDataDeferred.await()
                fetchData with lastActivities.ratings[key.type]
            }
        )
    }
}

@CineScoutTestApi
class FakeGetRatingsSyncStatus(private val syncStatus: SyncStatus) : GetRatingsSyncStatus {

    override suspend fun invoke(key: SyncRatingsKey) = syncStatus
}
