package cinescout.watchlist.data.pager

import app.cash.paging.Pager
import app.cash.paging.PagingConfig
import cinescout.lists.domain.ListParams
import cinescout.lists.domain.PagingDefaults
import cinescout.screenplay.domain.model.Screenplay
import cinescout.watchlist.data.datasource.LocalWatchlistDataSource
import cinescout.watchlist.data.mediator.WatchlistRemoteMediatorFactory
import cinescout.watchlist.domain.pager.WatchlistPager
import org.koin.core.annotation.Factory

@Factory
internal class RealWatchlistPager(
    private val localDataSource: LocalWatchlistDataSource,
    private val remoteMediatorFactory: WatchlistRemoteMediatorFactory
) : WatchlistPager {

    override fun create(params: ListParams): Pager<Int, Screenplay> = Pager(
        config = PagingConfig(pageSize = PagingDefaults.PageSize),
        remoteMediator = remoteMediatorFactory.create(params.type),
        pagingSourceFactory = { localDataSource.findPagedWatchlist(params) }
    )
}
