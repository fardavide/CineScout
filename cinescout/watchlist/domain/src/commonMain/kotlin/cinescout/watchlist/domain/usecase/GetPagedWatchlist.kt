package cinescout.watchlist.domain.usecase

import app.cash.paging.PagingData
import cinescout.lists.domain.ListType
import cinescout.screenplay.domain.model.Screenplay
import cinescout.watchlist.domain.WatchlistPager
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory

interface GetPagedWatchlist {

    operator fun invoke(listType: ListType): Flow<PagingData<Screenplay>>
}

@Factory
internal class RealGetPagedWatchlist(
    private val watchlistPager: WatchlistPager
) : GetPagedWatchlist {
    
    override operator fun invoke(listType: ListType): Flow<PagingData<Screenplay>> =
        watchlistPager.create(listType).flow
}
