package cinescout.rating.data.datasource

import arrow.core.Either
import cinescout.model.NetworkOperation
import cinescout.rating.domain.model.ScreenplayIdWithPersonalRating
import cinescout.rating.domain.model.ScreenplayWithPersonalRating
import cinescout.screenplay.domain.model.Rating
import cinescout.screenplay.domain.model.ScreenplayType
import cinescout.screenplay.domain.model.TmdbScreenplayId

interface RemotePersonalRatingDataSource {

    suspend fun getAllRatingIds(
        type: ScreenplayType
    ): Either<NetworkOperation, List<ScreenplayIdWithPersonalRating>>

    suspend fun getRatings(
        type: ScreenplayType,
        page: Int
    ): Either<NetworkOperation, List<ScreenplayWithPersonalRating>>

    suspend fun postRating(screenplayId: TmdbScreenplayId, rating: Rating): Either<NetworkOperation, Unit>
    
    suspend fun deleteRating(screenplayId: TmdbScreenplayId): Either<NetworkOperation, Unit>
}
