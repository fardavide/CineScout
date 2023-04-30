package cinescout.watchlist.domain.usecase

import app.cash.paging.PagingData
import cinescout.lists.domain.ListSorting
import cinescout.screenplay.domain.model.Screenplay
import cinescout.screenplay.domain.model.ScreenplayTypeFilter
import cinescout.watchlist.domain.pager.WatchlistPager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.koin.core.annotation.Factory

interface GetPagedWatchlist {

    operator fun invoke(sorting: ListSorting, type: ScreenplayTypeFilter): Flow<PagingData<Screenplay>>
}

@Factory
internal class RealGetPagedWatchlist(
    private val watchlistPager: WatchlistPager
) : GetPagedWatchlist {
    
    override operator fun invoke(
        sorting: ListSorting,
        type: ScreenplayTypeFilter
    ): Flow<PagingData<Screenplay>> = watchlistPager.create(sorting, type).flow
}

class FakeGetPagedWatchlist : GetPagedWatchlist {

    override fun invoke(sorting: ListSorting, type: ScreenplayTypeFilter): Flow<PagingData<Screenplay>> =
        flowOf(PagingData.empty())
}
