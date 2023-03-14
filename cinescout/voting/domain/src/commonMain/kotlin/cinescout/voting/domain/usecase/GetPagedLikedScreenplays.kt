package cinescout.voting.domain.usecase

import app.cash.paging.PagingData
import cinescout.screenplay.domain.model.Screenplay
import cinescout.screenplay.domain.model.ScreenplayType
import kotlinx.coroutines.flow.Flow

interface GetPagedLikedScreenplays {

    operator fun invoke(type: ScreenplayType): Flow<PagingData<Screenplay>>
}
