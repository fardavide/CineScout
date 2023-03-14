package cinescout.watchlist.data

import androidx.paging.LoadType
import androidx.paging.PagingState
import app.cash.paging.RemoteMediator
import cinescout.screenplay.domain.model.Screenplay
import cinescout.screenplay.domain.model.ScreenplayType
import cinescout.store5.FetchException
import org.koin.core.annotation.Factory

internal class WatchlistRemoteMediator(
    private val listType: ScreenplayType,
    private val syncWatchlist: SyncWatchlist
) : RemoteMediator<Int, Screenplay>() {

    override suspend fun load(loadType: LoadType, state: PagingState<Int, Screenplay>): MediatorResult {
        val nextPage = when (loadType) {
            LoadType.REFRESH -> 0
            LoadType.PREPEND -> state.pages.last().prevKey
                ?: return MediatorResult.Success(endOfPaginationReached = false)
            LoadType.APPEND -> state.pages.last().nextKey
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

    fun create(listType: ScreenplayType): WatchlistRemoteMediator =
        WatchlistRemoteMediator(listType, syncWatchlist)
}
