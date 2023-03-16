package cinescout.watchlist.data.pager

import app.cash.paging.Pager
import app.cash.paging.PagingConfig
import cinescout.screenplay.domain.model.Screenplay
import cinescout.screenplay.domain.model.ScreenplayType
import cinescout.watchlist.data.datasource.LocalWatchlistDataSource
import cinescout.watchlist.data.mediator.WatchlistRemoteMediatorFactory
import cinescout.watchlist.domain.pager.WatchlistPager
import org.koin.core.annotation.Factory

@Factory
internal class RealWatchlistPager(
    private val localDataSource: LocalWatchlistDataSource,
    private val remoteMediatorFactory: WatchlistRemoteMediatorFactory
) : WatchlistPager {

    override fun create(listType: ScreenplayType): Pager<Int, Screenplay> = Pager(
        config = PagingConfig(pageSize = 50),
        remoteMediator = remoteMediatorFactory.create(listType),
        pagingSourceFactory = { localDataSource.findPagedWatchlist(listType) }
    )
}