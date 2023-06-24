package cinescout.rating.data.mediator

import arrow.core.Either
import cinescout.error.NetworkError
import cinescout.model.handleSkippedAsRight
import cinescout.rating.data.datasource.LocalPersonalRatingDataSource
import cinescout.rating.data.datasource.RemotePersonalRatingDataSource
import cinescout.screenplay.domain.model.ScreenplayTypeFilter
import cinescout.sync.domain.model.RequiredSync
import org.koin.core.annotation.Factory

@Factory
internal class SyncRatings(
    private val localDataSource: LocalPersonalRatingDataSource,
    private val remoteDataSource: RemotePersonalRatingDataSource
) {

    suspend operator fun invoke(
        type: ScreenplayTypeFilter,
        requiredSync: RequiredSync
    ): Either<NetworkError, Unit> {
        val remoteData = when (requiredSync) {
            RequiredSync.Initial -> remoteDataSource.getRatings(type, 1)
            RequiredSync.Complete -> remoteDataSource.getAllRatings(type)
        }
        return remoteData
            .map { localDataSource.insertRatings(it) }
            .handleSkippedAsRight()
    }
}
