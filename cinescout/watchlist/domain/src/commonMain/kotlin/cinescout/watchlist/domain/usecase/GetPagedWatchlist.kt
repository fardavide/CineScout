package cinescout.watchlist.domain.usecase

import app.cash.paging.PagingData
import arrow.core.Option
import cinescout.lists.domain.ListSorting
import cinescout.screenplay.domain.model.Screenplay
import cinescout.screenplay.domain.model.ScreenplayTypeFilter
import cinescout.screenplay.domain.model.TmdbGenreId
import cinescout.watchlist.domain.pager.WatchlistPager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.koin.core.annotation.Factory

interface GetPagedWatchlist {

    operator fun invoke(
        genreFilter: Option<TmdbGenreId>,
        sorting: ListSorting,
        type: ScreenplayTypeFilter
    ): Flow<PagingData<Screenplay>>
}

@Factory
internal class RealGetPagedWatchlist(
    private val watchlistPager: WatchlistPager
) : GetPagedWatchlist {
    
    override operator fun invoke(
        genreFilter: Option<TmdbGenreId>,
        sorting: ListSorting,
        type: ScreenplayTypeFilter
    ): Flow<PagingData<Screenplay>> = watchlistPager.create(genreFilter, sorting, type).flow
}

class FakeGetPagedWatchlist : GetPagedWatchlist {

    override fun invoke(
        genreFilter: Option<TmdbGenreId>,
        sorting: ListSorting,
        type: ScreenplayTypeFilter
    ): Flow<PagingData<Screenplay>> = flowOf(PagingData.empty())
}
