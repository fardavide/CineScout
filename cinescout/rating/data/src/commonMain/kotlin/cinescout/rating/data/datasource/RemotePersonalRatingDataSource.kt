package cinescout.rating.data.datasource

import arrow.core.Either
import cinescout.model.NetworkOperation
import cinescout.rating.domain.model.ScreenplayIdWithPersonalRating
import cinescout.rating.domain.model.ScreenplayWithPersonalRating
import cinescout.screenplay.domain.model.Rating
import cinescout.screenplay.domain.model.ScreenplayTypeFilter
import cinescout.screenplay.domain.model.ids.TmdbScreenplayId

interface RemotePersonalRatingDataSource {

    suspend fun getAllRatingIds(
        type: ScreenplayTypeFilter
    ): Either<NetworkOperation, List<ScreenplayIdWithPersonalRating>>

    suspend fun getAllRatings(
        type: ScreenplayTypeFilter
    ): Either<NetworkOperation, List<ScreenplayWithPersonalRating>>

    suspend fun getRatings(
        type: ScreenplayTypeFilter,
        page: Int
    ): Either<NetworkOperation, List<ScreenplayWithPersonalRating>>

    suspend fun postRating(screenplayId: TmdbScreenplayId, rating: Rating): Either<NetworkOperation, Unit>
    
    suspend fun deleteRating(screenplayId: TmdbScreenplayId): Either<NetworkOperation, Unit>
}
