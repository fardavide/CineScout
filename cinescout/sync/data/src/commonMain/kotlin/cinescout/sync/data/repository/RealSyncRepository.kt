package cinescout.sync.data.repository

import arrow.core.Either
import cinescout.model.NetworkOperation
import cinescout.sync.data.datasource.RemoteSyncDataSource
import cinescout.sync.domain.model.LastActivities
import cinescout.sync.domain.repository.SyncRepository
import org.koin.core.annotation.Factory

@Factory
internal class RealSyncRepository(
    private val dataSource: RemoteSyncDataSource
) : SyncRepository {

    override suspend fun getLastActivities(): Either<NetworkOperation, LastActivities> =
        dataSource.getLastActivities()
}
