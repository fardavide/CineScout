package cinescout.watchlist.data

import androidx.paging.LoadType
import androidx.paging.PagingState
import app.cash.paging.RemoteMediator
import cinescout.lists.domain.ListType
import cinescout.screenplay.domain.model.Screenplay
import cinescout.store5.FetchException
import cinescout.watchlist.domain.WatchlistPagerKey
import org.koin.core.annotation.Factory

internal class WatchlistRemoteMediator(
    private val listType: ListType,
    private val syncWatchlist: SyncWatchlist
) : RemoteMediator<WatchlistPagerKey, Screenplay>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<WatchlistPagerKey, Screenplay>
    ): MediatorResult {
        val nextPage = when (loadType) {
            LoadType.REFRESH -> 0
            LoadType.PREPEND -> state.pages.last().prevKey?.page
                ?: return MediatorResult.Success(endOfPaginationReached = false)
            LoadType.APPEND -> state.pages.last().nextKey?.page
                ?: return MediatorResult.Success(endOfPaginationReached = true)
        }

        return syncWatchlist(listType, nextPage).fold(
            ifLeft = { networkError -> MediatorResult.Error(FetchException(networkError)) },
            ifRight = { MediatorResult.Success(endOfPaginationReached = false) }
        )
    }
}

@Factory
internal class WatchlistRemoteMediatorFactory(private val syncWatchlist: SyncWatchlist) {

    fun create(listType: ListType): WatchlistRemoteMediator = WatchlistRemoteMediator(listType, syncWatchlist)
}
