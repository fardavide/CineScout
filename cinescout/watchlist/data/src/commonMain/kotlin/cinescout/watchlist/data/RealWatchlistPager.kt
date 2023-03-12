package cinescout.watchlist.data

import app.cash.paging.Pager
import app.cash.paging.PagingConfig
import cinescout.lists.domain.ListType
import cinescout.screenplay.domain.model.Screenplay
import cinescout.watchlist.domain.WatchlistPager
import cinescout.watchlist.domain.WatchlistPagerKey
import org.koin.core.annotation.Factory

@Factory
internal class RealWatchlistPager(
    private val localDataSource: LocalWatchlistDataSource,
    private val remoteMediatorFactory: WatchlistRemoteMediatorFactory
) : WatchlistPager {

    override fun create(listType: ListType): Pager<WatchlistPagerKey, Screenplay> = Pager(
        config = PagingConfig(pageSize = 50),
        remoteMediator = remoteMediatorFactory.create(listType),
        pagingSourceFactory = { localDataSource.findPagedWatchlist(listType) }
    )
}
