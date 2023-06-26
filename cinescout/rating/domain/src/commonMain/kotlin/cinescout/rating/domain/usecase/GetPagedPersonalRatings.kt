package cinescout.rating.domain.usecase

import app.cash.paging.PagingData
import arrow.core.Option
import cinescout.lists.domain.ListSorting
import cinescout.rating.domain.model.ScreenplayWithPersonalRating
import cinescout.rating.domain.pager.RatingsPager
import cinescout.screenplay.domain.model.ScreenplayTypeFilter
import cinescout.screenplay.domain.model.id.GenreSlug
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.koin.core.annotation.Factory

interface GetPagedPersonalRatings {

    operator fun invoke(
        genreFilter: Option<GenreSlug>,
        sorting: ListSorting,
        type: ScreenplayTypeFilter
    ): Flow<PagingData<ScreenplayWithPersonalRating>>
}

@Factory
internal class RealGetPagedPersonalRatings(
    private val ratingsPager: RatingsPager
) : GetPagedPersonalRatings {

    override operator fun invoke(
        genreFilter: Option<GenreSlug>,
        sorting: ListSorting,
        type: ScreenplayTypeFilter
    ): Flow<PagingData<ScreenplayWithPersonalRating>> = ratingsPager.create(genreFilter, sorting, type).flow
}

class FakeGetPagedPersonalRatings : GetPagedPersonalRatings {

    override fun invoke(
        genreFilter: Option<GenreSlug>,
        sorting: ListSorting,
        type: ScreenplayTypeFilter
    ): Flow<PagingData<ScreenplayWithPersonalRating>> = flowOf(PagingData.empty())
}
