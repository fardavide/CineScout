package cinescout.rating.data.datasource

import arrow.core.Either
import cinescout.error.NetworkError
import cinescout.rating.domain.model.ScreenplayIdWithPersonalRating

interface RemotePersonalRatingDataSource {

    suspend fun getRatingIds(): Either<NetworkError, List<ScreenplayIdWithPersonalRating>>

    suspend fun postRatings(ratings: List<ScreenplayIdWithPersonalRating>): Either<NetworkError, Unit>
}
