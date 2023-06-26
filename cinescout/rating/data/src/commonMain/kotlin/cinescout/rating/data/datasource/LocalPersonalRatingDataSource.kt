package cinescout.rating.data.datasource

import androidx.paging.PagingSource
import arrow.core.Option
import cinescout.lists.domain.ListSorting
import cinescout.rating.domain.model.ScreenplayIdWithPersonalRating
import cinescout.rating.domain.model.ScreenplayWithGenreSlugsAndPersonalRating
import cinescout.rating.domain.model.ScreenplayWithPersonalRating
import cinescout.screenplay.domain.model.Rating
import cinescout.screenplay.domain.model.ScreenplayTypeFilter
import cinescout.screenplay.domain.model.id.GenreSlug
import cinescout.screenplay.domain.model.id.ScreenplayIds
import cinescout.screenplay.domain.model.id.TmdbScreenplayId
import kotlinx.coroutines.flow.Flow

interface LocalPersonalRatingDataSource {

    suspend fun delete(screenplayId: TmdbScreenplayId)

    fun findPagedRatings(
        genreFilter: Option<GenreSlug>,
        sorting: ListSorting,
        type: ScreenplayTypeFilter
    ): PagingSource<Int, ScreenplayWithPersonalRating>

    fun findRatingIds(type: ScreenplayTypeFilter): Flow<List<ScreenplayIdWithPersonalRating>>

    suspend fun insert(ids: ScreenplayIds, rating: Rating)

    suspend fun insertAllRatings(ratings: List<ScreenplayWithGenreSlugsAndPersonalRating>)

    suspend fun updateAllRatings(ratings: List<ScreenplayWithGenreSlugsAndPersonalRating>)

    suspend fun updateAllRatingIds(ratings: List<ScreenplayIdWithPersonalRating>)
}
