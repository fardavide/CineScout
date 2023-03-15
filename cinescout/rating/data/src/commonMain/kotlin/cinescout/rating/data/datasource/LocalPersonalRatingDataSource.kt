package cinescout.rating.data.datasource

import cinescout.rating.domain.model.ScreenplayIdWithPersonalRating
import cinescout.screenplay.domain.model.ScreenplayType
import kotlinx.coroutines.flow.Flow

interface LocalPersonalRatingDataSource {

    fun findRatingIds(type: ScreenplayType): Flow<List<ScreenplayIdWithPersonalRating>>

    suspend fun insertRatings(ratings: List<ScreenplayIdWithPersonalRating>)
}
