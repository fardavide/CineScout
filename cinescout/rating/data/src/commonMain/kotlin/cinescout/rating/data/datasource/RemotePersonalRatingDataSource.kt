package cinescout.rating.data.datasource

import arrow.core.Either
import cinescout.model.NetworkOperation
import cinescout.rating.domain.model.ScreenplayIdWithPersonalRating
import cinescout.screenplay.domain.model.Rating
import cinescout.screenplay.domain.model.ScreenplayType
import cinescout.screenplay.domain.model.TmdbScreenplayId

interface RemotePersonalRatingDataSource {

    suspend fun getRatingIds(
        type: ScreenplayType
    ): Either<NetworkOperation, List<ScreenplayIdWithPersonalRating>>
    
    suspend fun postRating(screenplayId: TmdbScreenplayId, rating: Rating): Either<NetworkOperation, Unit>
    
    suspend fun deleteRating(screenplayId: TmdbScreenplayId): Either<NetworkOperation, Unit>
}
