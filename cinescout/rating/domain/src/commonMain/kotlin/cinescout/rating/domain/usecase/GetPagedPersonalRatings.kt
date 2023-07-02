package cinescout.rating.domain.usecase

import app.cash.paging.PagingData
import cinescout.lists.domain.ListParams
import cinescout.rating.domain.model.ScreenplayWithPersonalRating
import cinescout.rating.domain.pager.RatingsPager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.koin.core.annotation.Factory

interface GetPagedPersonalRatings {

    operator fun invoke(params: ListParams): Flow<PagingData<ScreenplayWithPersonalRating>>
}

@Factory
internal class RealGetPagedPersonalRatings(
    private val ratingsPager: RatingsPager
) : GetPagedPersonalRatings {

    override operator fun invoke(params: ListParams): Flow<PagingData<ScreenplayWithPersonalRating>> =
        ratingsPager.create(params).flow
}

class FakeGetPagedPersonalRatings : GetPagedPersonalRatings {

    override fun invoke(params: ListParams): Flow<PagingData<ScreenplayWithPersonalRating>> =
        flowOf(PagingData.empty())
}
