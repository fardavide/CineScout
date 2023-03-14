package cinescout.rating.data.datasource

import cinescout.rating.domain.model.ScreenplayIdWithPersonalRating
import kotlinx.coroutines.flow.Flow

interface LocalPersonalRatingDataSource {

    fun findRatingIds(): Flow<List<ScreenplayIdWithPersonalRating>>

    suspend fun insertRatings(ratings: List<ScreenplayIdWithPersonalRating>)
}
