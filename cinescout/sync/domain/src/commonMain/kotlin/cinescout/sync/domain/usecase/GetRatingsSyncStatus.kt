package cinescout.sync.domain.usecase

import cinescout.CineScoutTestApi
import cinescout.fetchdata.domain.repository.FetchDataRepository
import cinescout.sync.domain.model.SyncExpiration
import cinescout.sync.domain.model.SyncRatingsKey
import cinescout.sync.domain.model.SyncStatus
import cinescout.sync.domain.util.toSyncStatus
import org.koin.core.annotation.Factory

interface GetRatingsSyncStatus {

    suspend operator fun invoke(key: SyncRatingsKey): SyncStatus
}

// TODO: use with last activities
@Factory
internal class RealGetRatingsSyncStatus(
    private val fetchDataRepository: FetchDataRepository
) : GetRatingsSyncStatus {

    override suspend fun invoke(key: SyncRatingsKey): SyncStatus =
        fetchDataRepository.get(key, SyncExpiration.Ratings).toSyncStatus()
}

@CineScoutTestApi
class FakeGetRatingsSyncStatus(private val syncStatus: SyncStatus) : GetRatingsSyncStatus {

    override suspend fun invoke(key: SyncRatingsKey) = syncStatus
}
