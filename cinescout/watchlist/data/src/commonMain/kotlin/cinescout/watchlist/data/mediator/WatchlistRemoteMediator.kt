package cinescout.watchlist.data.mediator

import androidx.paging.LoadType
import androidx.paging.PagingState
import app.cash.paging.RemoteMediator
import cinescout.error.NetworkError
import cinescout.screenplay.domain.model.Screenplay
import cinescout.screenplay.domain.model.ScreenplayType
import cinescout.store5.FetchException
import org.koin.core.annotation.Factory

internal class WatchlistRemoteMediator(
    private val listType: ScreenplayType,
    private val syncWatchlist: SyncWatchlist
) : RemoteMediator<Int, Screenplay>() {

    private var lastPage = 0

    override suspend fun load(loadType: LoadType, state: PagingState<Int, Screenplay>): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> 1
            // Prepend is not supported
            LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
            LoadType.APPEND -> {
                // TODO: Get last page + 1
                ++lastPage
            }
        }

        return syncWatchlist(listType, page).fold(
            ifLeft = { networkError ->
                when (networkError) {
                    is NetworkError.NotFound -> MediatorResult.Success(endOfPaginationReached = true)
                    else -> MediatorResult.Error(FetchException(networkError))
                }
            },
            ifRight = {
                // TODO: Store current page page
                lastPage = page
                MediatorResult.Success(endOfPaginationReached = false)
            }
        )
    }
}

@Factory
internal class WatchlistRemoteMediatorFactory(private val syncWatchlist: SyncWatchlist) {

    fun create(listType: ScreenplayType): WatchlistRemoteMediator =
        WatchlistRemoteMediator(listType, syncWatchlist)
}
