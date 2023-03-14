package cinescout.search.domain.usecase

import app.cash.paging.PagingData
import cinescout.screenplay.domain.model.Screenplay
import cinescout.screenplay.domain.model.ScreenplayType
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory

interface SearchPagedScreenplays {

    operator fun invoke(type: ScreenplayType, query: String): Flow<PagingData<Screenplay>>
}

@Factory
internal class RealSearchPagedScreenplays : SearchPagedScreenplays {

    override fun invoke(type: ScreenplayType, query: String): Flow<PagingData<Screenplay>> {
        TODO("Not yet implemented")
    }
}
