package cinescout.sync.data.repository

import arrow.core.Either
import cinescout.model.NetworkOperation
import cinescout.screenplay.domain.model.id.ScreenplayIds
import cinescout.sync.data.datasource.LocalSyncDataSource
import cinescout.sync.data.datasource.RemoteSyncDataSource
import cinescout.sync.domain.model.LastActivities
import cinescout.sync.domain.repository.SyncRepository
import org.koin.core.annotation.Factory

@Factory
internal class RealSyncRepository(
    private val localDataSource: LocalSyncDataSource,
    private val remoteDataSource: RemoteSyncDataSource
) : SyncRepository {

    override suspend fun getAllNotFetchedScreenplayIds(): List<ScreenplayIds> =
        localDataSource.findAllNotFetchedScreenplayIds()

    override suspend fun getLastActivities(): Either<NetworkOperation, LastActivities> =
        remoteDataSource.getLastActivities()
}
