package cinescout.rating.domain.usecase

import app.cash.paging.PagingData
import cinescout.rating.domain.model.ScreenplayWithPersonalRating
import cinescout.rating.domain.pager.RatingsPager
import cinescout.screenplay.domain.model.ScreenplayType
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory

interface GetPagedPersonalRatings {

    operator fun invoke(type: ScreenplayType): Flow<PagingData<ScreenplayWithPersonalRating>>
}

@Factory
internal class RealGetPagedPersonalRatings(
    private val ratingsPager: RatingsPager
) : GetPagedPersonalRatings {

    override operator fun invoke(type: ScreenplayType): Flow<PagingData<ScreenplayWithPersonalRating>> =
        ratingsPager.create(type).flow
}
