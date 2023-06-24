package cinescout.rating.data.mediator

import arrow.core.Either
import cinescout.error.NetworkError
import cinescout.model.handleSkippedAsRight
import cinescout.rating.data.datasource.LocalPersonalRatingDataSource
import cinescout.rating.data.datasource.RemotePersonalRatingDataSource
import cinescout.screenplay.domain.model.ScreenplayTypeFilter
import org.koin.core.annotation.Factory

@Factory
internal class SyncRatings(
    private val localDataSource: LocalPersonalRatingDataSource,
    private val remoteDataSource: RemotePersonalRatingDataSource
) {

    suspend operator fun invoke(type: ScreenplayTypeFilter, syncType: Type): Either<NetworkError, Unit> {
        val remoteData = when (syncType) {
            Type.Initial -> remoteDataSource.getRatings(type, 1)
            Type.Complete -> remoteDataSource.getAllRatings(type)
        }
        return remoteData
            .map { localDataSource.insertRatings(it) }
            .handleSkippedAsRight()
    }

    enum class Type {
        Initial,
        Complete
    }
}
