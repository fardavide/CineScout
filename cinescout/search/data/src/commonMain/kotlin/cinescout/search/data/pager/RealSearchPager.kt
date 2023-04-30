package cinescout.search.data.pager

import app.cash.paging.Pager
import app.cash.paging.PagingConfig
import cinescout.lists.domain.PagingDefaults
import cinescout.screenplay.domain.model.Screenplay
import cinescout.screenplay.domain.model.ScreenplayTypeFilter
import cinescout.search.data.datasource.LocalSearchDataSource
import cinescout.search.data.mediator.SearchRemoteMediatorFactory
import cinescout.search.domain.pager.SearchPager
import org.koin.core.annotation.Factory

@Factory
internal class RealSearchPager(
    private val localSearchDataSource: LocalSearchDataSource,
    private val remoteMediatorFactory: SearchRemoteMediatorFactory
) : SearchPager {

    override fun create(type: ScreenplayTypeFilter, query: String): Pager<Int, Screenplay> = Pager(
        config = PagingConfig(pageSize = PagingDefaults.PageSize),
        remoteMediator = remoteMediatorFactory.create(type, query),
        pagingSourceFactory = { localSearchDataSource.searchPaged(type, query) }
    )
}
