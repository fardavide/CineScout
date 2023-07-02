package cinescout.watchlist.domain.usecase

import app.cash.paging.PagingData
import cinescout.CineScoutTestApi
import cinescout.lists.domain.ListParams
import cinescout.screenplay.domain.model.Screenplay
import cinescout.watchlist.domain.pager.WatchlistPager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.koin.core.annotation.Factory

interface GetPagedWatchlist {

    operator fun invoke(params: ListParams): Flow<PagingData<Screenplay>>
}

@Factory
internal class RealGetPagedWatchlist(
    private val watchlistPager: WatchlistPager
) : GetPagedWatchlist {
    
    override operator fun invoke(params: ListParams): Flow<PagingData<Screenplay>> =
        watchlistPager.create(params).flow
}

@CineScoutTestApi
class FakeGetPagedWatchlist : GetPagedWatchlist {

    override fun invoke(params: ListParams): Flow<PagingData<Screenplay>> = flowOf(PagingData.empty())
}
