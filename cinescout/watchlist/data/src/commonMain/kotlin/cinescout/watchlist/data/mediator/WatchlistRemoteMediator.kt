package cinescout.watchlist.data.mediator

import androidx.paging.LoadType
import androidx.paging.PagingState
import app.cash.paging.RemoteMediator
import cinescout.fetchdata.domain.repository.FetchDataRepository
import cinescout.lists.domain.ListSorting
import cinescout.screenplay.domain.model.Screenplay
import cinescout.screenplay.domain.model.ScreenplayTypeFilter
import cinescout.store5.FetchException
import org.koin.core.annotation.Factory
import org.koin.core.annotation.InjectedParam
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.parameter.parametersOf
import kotlin.time.Duration.Companion.minutes

@Factory
internal class WatchlistRemoteMediator(
    private val fetchDataRepository: FetchDataRepository,
    @InjectedParam private val sorting: ListSorting,
    private val syncWatchlist: SyncWatchlist,
    @InjectedParam private val type: ScreenplayTypeFilter
) : RemoteMediator<Int, Screenplay>() {

    private val key = Key(type)
    private val expiration = 5.minutes

    override suspend fun initialize(): InitializeAction =
        when (fetchDataRepository.getPage(key, expiration = expiration)) {
            null -> InitializeAction.LAUNCH_INITIAL_REFRESH
            else -> InitializeAction.SKIP_INITIAL_REFRESH
        }

    // TODO: check if should fetch: No, Initial, Complete
    override suspend fun load(loadType: LoadType, state: PagingState<Int, Screenplay>): MediatorResult {
        val syncType = when (loadType) {
            LoadType.REFRESH -> SyncWatchlist.Type.Initial
            // Prepend is not supported
            LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
            LoadType.APPEND -> when (fetchDataRepository.getPage(key, expiration = expiration)) {
                null -> SyncWatchlist.Type.Initial
                1 -> SyncWatchlist.Type.Complete
                else -> return MediatorResult.Success(endOfPaginationReached = true)
            }
        }

        return syncWatchlist(type, syncType).fold(
            ifLeft = { networkError -> MediatorResult.Error(FetchException(networkError)) },
            ifRight = {
                val page = when (syncType) {
                    SyncWatchlist.Type.Initial -> 1
                    SyncWatchlist.Type.Complete -> 2
                }
                fetchDataRepository.set(key, page)
                MediatorResult.Success(endOfPaginationReached = syncType == SyncWatchlist.Type.Complete)
            }
        )
    }

    @JvmInline value class Key(val type: ScreenplayTypeFilter)
}

@Factory
internal class WatchlistRemoteMediatorFactory : KoinComponent {

    fun create(sorting: ListSorting, type: ScreenplayTypeFilter): WatchlistRemoteMediator =
        get { parametersOf(sorting, type) }
}
