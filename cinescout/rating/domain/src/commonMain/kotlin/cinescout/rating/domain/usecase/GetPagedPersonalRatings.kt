package cinescout.rating.domain.usecase

import app.cash.paging.PagingData
import cinescout.rating.domain.model.ScreenplayWithPersonalRating
import cinescout.screenplay.domain.model.ScreenplayType
import kotlinx.coroutines.flow.Flow

interface GetPagedPersonalRatings {

    operator fun invoke(type: ScreenplayType): Flow<PagingData<ScreenplayWithPersonalRating>>
}
