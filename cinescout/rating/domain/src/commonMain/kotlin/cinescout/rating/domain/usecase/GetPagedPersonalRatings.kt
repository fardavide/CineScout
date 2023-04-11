package cinescout.rating.domain.usecase

import app.cash.paging.PagingData
import cinescout.lists.domain.ListSorting
import cinescout.rating.domain.model.ScreenplayWithPersonalRating
import cinescout.rating.domain.pager.RatingsPager
import cinescout.screenplay.domain.model.ScreenplayType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.koin.core.annotation.Factory

interface GetPagedPersonalRatings {

    operator fun invoke(
        sorting: ListSorting,
        type: ScreenplayType
    ): Flow<PagingData<ScreenplayWithPersonalRating>>
}

@Factory
internal class RealGetPagedPersonalRatings(
    private val ratingsPager: RatingsPager
) : GetPagedPersonalRatings {

    override operator fun invoke(
        sorting: ListSorting,
        type: ScreenplayType
    ): Flow<PagingData<ScreenplayWithPersonalRating>> = ratingsPager.create(sorting, type).flow
}

class FakeGetPagedPersonalRatings : GetPagedPersonalRatings {

    override fun invoke(
        sorting: ListSorting,
        type: ScreenplayType
    ): Flow<PagingData<ScreenplayWithPersonalRating>> = flowOf(PagingData.empty())
}
