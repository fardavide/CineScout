package cinescout.search.domain.usecase

import app.cash.paging.PagingData
import cinescout.screenplay.domain.model.Screenplay
import cinescout.screenplay.domain.model.ScreenplayTypeFilter
import cinescout.search.domain.pager.SearchPager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.koin.core.annotation.Factory

interface SearchPagedScreenplays {

    operator fun invoke(type: ScreenplayTypeFilter, query: String): Flow<PagingData<Screenplay>>
}

@Factory
internal class RealSearchPagedScreenplays(
    private val searchPager: SearchPager
) : SearchPagedScreenplays {

    override fun invoke(type: ScreenplayTypeFilter, query: String): Flow<PagingData<Screenplay>> =
        searchPager.create(type, query).flow
}

class FakeSearchPagedScreenplays : SearchPagedScreenplays {

    override fun invoke(type: ScreenplayTypeFilter, query: String): Flow<PagingData<Screenplay>> =
        flowOf(PagingData.empty())
}
