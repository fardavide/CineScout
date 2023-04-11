package cinescout.rating.data.mediator

import arrow.core.Either
import cinescout.error.NetworkError
import cinescout.model.handleSkippedAsRight
import cinescout.rating.data.datasource.LocalPersonalRatingDataSource
import cinescout.rating.data.datasource.RemotePersonalRatingDataSource
import cinescout.screenplay.domain.model.ScreenplayType
import org.koin.core.annotation.Factory

@Factory
internal class SyncRatings(
    private val localDataSource: LocalPersonalRatingDataSource,
    private val remoteDataSource: RemotePersonalRatingDataSource
) {

    suspend operator fun invoke(type: ScreenplayType, page: Int): Either<NetworkError, Unit> =
        remoteDataSource.getRatings(type, page)
            .map { localDataSource.insertRatings(it) }
            .handleSkippedAsRight()
}
