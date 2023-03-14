package cinescout.search.domain.usecase

import app.cash.paging.PagingData
import cinescout.screenplay.domain.model.Screenplay
import cinescout.screenplay.domain.model.ScreenplayType
import kotlinx.coroutines.flow.Flow

interface SearchPagedScreenplays {

    operator fun invoke(type: ScreenplayType, query: String): Flow<PagingData<Screenplay>>
}
