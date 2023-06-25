package cinescout.watchlist.data.pager

import app.cash.paging.Pager
import app.cash.paging.PagingConfig
import arrow.core.Option
import cinescout.lists.domain.ListSorting
import cinescout.lists.domain.PagingDefaults
import cinescout.screenplay.domain.model.Screenplay
import cinescout.screenplay.domain.model.ScreenplayTypeFilter
import cinescout.screenplay.domain.model.TmdbGenreId
import cinescout.watchlist.data.datasource.LocalWatchlistDataSource
import cinescout.watchlist.data.mediator.WatchlistRemoteMediatorFactory
import cinescout.watchlist.domain.pager.WatchlistPager
import org.koin.core.annotation.Factory

@Factory
internal class RealWatchlistPager(
    private val localDataSource: LocalWatchlistDataSource,
    private val remoteMediatorFactory: WatchlistRemoteMediatorFactory
) : WatchlistPager {

    override fun create(
        genreFilter: Option<TmdbGenreId>,
        sorting: ListSorting,
        type: ScreenplayTypeFilter
    ): Pager<Int, Screenplay> = Pager(
        config = PagingConfig(pageSize = PagingDefaults.PageSize),
        remoteMediator = remoteMediatorFactory.create(type),
        pagingSourceFactory = { localDataSource.findPagedWatchlist(genreFilter, sorting, type) }
    )
}
