package cinescout.history.data.sync

import arrow.core.Either
import cinescout.error.NetworkError
import cinescout.fetchdata.domain.repository.FetchDataRepository
import cinescout.history.data.datasource.LocalHistoryDataSource
import cinescout.history.data.datasource.RemoteHistoryDataSource
import cinescout.history.domain.usecase.SyncHistory
import cinescout.model.handleSkippedAsRight
import cinescout.perfomance.SyncTracerFactory
import cinescout.sync.domain.model.RequiredSync
import cinescout.sync.domain.model.SyncHistoryKey
import cinescout.sync.domain.util.toBookmark
import org.koin.core.annotation.Factory

@Factory
internal class RealSyncHistory(
    private val fetchDataRepository: FetchDataRepository,
    private val localDataSource: LocalHistoryDataSource,
    private val remoteDataSource: RemoteHistoryDataSource,
    syncTracerFactory: SyncTracerFactory
) : SyncHistory {

    private val syncTracer = syncTracerFactory.create("History")

    override suspend fun invoke(key: SyncHistoryKey, requiredSync: RequiredSync): Either<NetworkError, Unit> {
        val remoteData = syncTracer.network { remoteDataSource.getAllHistories(key.type) }
        return remoteData
            .map { list -> syncTracer.disk { localDataSource.updateAll(list, key.type) } }
            .handleSkippedAsRight()
            .also { fetchDataRepository.set(key, requiredSync.toBookmark()) }
    }
}
