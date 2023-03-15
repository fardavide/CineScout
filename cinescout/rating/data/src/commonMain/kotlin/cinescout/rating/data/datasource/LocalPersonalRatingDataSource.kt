package cinescout.rating.data.datasource

import androidx.paging.PagingSource
import cinescout.rating.domain.model.ScreenplayIdWithPersonalRating
import cinescout.rating.domain.model.ScreenplayWithPersonalRating
import cinescout.screenplay.domain.model.ScreenplayType
import kotlinx.coroutines.flow.Flow

interface LocalPersonalRatingDataSource {

    fun findPagedRatings(type: ScreenplayType): PagingSource<Int, ScreenplayWithPersonalRating>

    fun findRatingIds(type: ScreenplayType): Flow<List<ScreenplayIdWithPersonalRating>>

    suspend fun insertRatingIds(ratings: List<ScreenplayIdWithPersonalRating>)

    suspend fun insertRatings(ratings: List<ScreenplayWithPersonalRating>)
}
