package cinescout.watchlist.domain.usecase

import app.cash.paging.PagingData
import cinescout.lists.domain.ListType
import cinescout.screenplay.domain.model.Screenplay
import cinescout.watchlist.domain.WatchlistPager
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory

interface ObservePagedWatchlist {

    operator fun invoke(listType: ListType): Flow<PagingData<Screenplay>>
}

@Factory
internal class RealObservePagedWatchlist(
    private val watchlistPager: WatchlistPager
) : ObservePagedWatchlist {
    
    override operator fun invoke(listType: ListType): Flow<PagingData<Screenplay>> =
        watchlistPager.create(listType).flow
}
