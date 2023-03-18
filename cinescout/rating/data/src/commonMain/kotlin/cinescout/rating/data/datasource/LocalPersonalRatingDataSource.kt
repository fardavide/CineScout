package cinescout.rating.data.datasource

import androidx.paging.PagingSource
import cinescout.rating.domain.model.ScreenplayIdWithPersonalRating
import cinescout.rating.domain.model.ScreenplayWithPersonalRating
import cinescout.screenplay.domain.model.Rating
import cinescout.screenplay.domain.model.ScreenplayType
import cinescout.screenplay.domain.model.TmdbScreenplayId
import kotlinx.coroutines.flow.Flow

interface LocalPersonalRatingDataSource {

    suspend fun delete(screenplayId: TmdbScreenplayId)

    fun findPagedRatings(type: ScreenplayType): PagingSource<Int, ScreenplayWithPersonalRating>

    fun findRatingIds(type: ScreenplayType): Flow<List<ScreenplayIdWithPersonalRating>>

    suspend fun insert(screenplayId: TmdbScreenplayId, rating: Rating)

    suspend fun insertRatings(ratings: List<ScreenplayWithPersonalRating>)

    suspend fun updateAllRatings(ratings: List<ScreenplayIdWithPersonalRating>)
}
