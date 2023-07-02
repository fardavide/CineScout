package cinescout.progress.data.pager

import app.cash.paging.Pager
import app.cash.paging.PagingConfig
import cinescout.lists.domain.ListParams
import cinescout.lists.domain.PagingDefaults
import cinescout.progress.data.datasource.LocalInProgressDataSource
import cinescout.progress.data.mediator.InProgressRemoteMediatorFactory
import cinescout.progress.domain.pager.InProgressPager
import cinescout.screenplay.domain.model.Screenplay
import org.koin.core.annotation.Factory

@Factory
internal class RealInProgressPager(
    private val localDataSource: LocalInProgressDataSource,
    private val remoteMediatorFactory: InProgressRemoteMediatorFactory
) : InProgressPager {

    override fun create(params: ListParams): Pager<Int, Screenplay> = Pager(
        config = PagingConfig(pageSize = PagingDefaults.PageSize),
        remoteMediator = remoteMediatorFactory.create(params.type),
        pagingSourceFactory = { localDataSource.findPagedInProgress(params) }
    )
}
