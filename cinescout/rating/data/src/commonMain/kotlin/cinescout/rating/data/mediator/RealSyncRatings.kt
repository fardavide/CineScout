package cinescout.rating.data.mediator

import arrow.core.Either
import cinescout.error.NetworkError
import cinescout.fetchdata.domain.repository.FetchDataRepository
import cinescout.model.handleSkippedAsRight
import cinescout.perfomance.SyncTracerFactory
import cinescout.rating.data.datasource.LocalPersonalRatingDataSource
import cinescout.rating.data.datasource.RemotePersonalRatingDataSource
import cinescout.rating.domain.usecase.SyncRatings
import cinescout.screenplay.domain.model.ScreenplayTypeFilter
import cinescout.sync.domain.model.RequiredSync
import cinescout.sync.domain.model.SyncRatingsKey
import cinescout.sync.domain.util.toBookmark
import org.koin.core.annotation.Factory

@Factory
internal class RealSyncRatings(
    private val fetchDataRepository: FetchDataRepository,
    private val localDataSource: LocalPersonalRatingDataSource,
    private val remoteDataSource: RemotePersonalRatingDataSource,
    syncTracerFactory: SyncTracerFactory
) : SyncRatings {

    private val syncTracer = syncTracerFactory.create("Ratings")

    override suspend operator fun invoke(
        type: ScreenplayTypeFilter,
        requiredSync: RequiredSync
    ): Either<NetworkError, Unit> {
        val remoteData = syncTracer.network {
            when (requiredSync) {
                RequiredSync.Initial -> remoteDataSource.getRatings(type, 1)
                RequiredSync.Complete -> remoteDataSource.getAllRatings(type)
            }
        }
        return remoteData
            .map { list ->
                syncTracer.disk {
                    when (requiredSync) {
                        RequiredSync.Initial -> localDataSource.insertAllRatings(list)
                        RequiredSync.Complete -> localDataSource.updateAllRatings(list, type)
                    }
                }
            }
            .handleSkippedAsRight()
            .also { fetchDataRepository.set(SyncRatingsKey(type), requiredSync.toBookmark()) }
    }
}
