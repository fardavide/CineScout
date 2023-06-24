package cinescout.watchlist.data.mediator

import androidx.paging.LoadType
import androidx.paging.PagingState
import app.cash.paging.RemoteMediator
import cinescout.fetchdata.domain.repository.FetchDataRepository
import cinescout.lists.domain.ListSorting
import cinescout.screenplay.domain.model.Screenplay
import cinescout.screenplay.domain.model.ScreenplayTypeFilter
import cinescout.store5.FetchException
import cinescout.sync.domain.model.RequiredSync
import cinescout.sync.domain.model.SyncNotRequired
import cinescout.sync.domain.model.SyncWatchlistKey
import cinescout.sync.domain.usecase.GetWatchlistSyncStatus
import cinescout.sync.domain.util.toBookmark
import org.koin.core.annotation.Factory
import org.koin.core.annotation.InjectedParam
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.parameter.parametersOf

@Factory
internal class WatchlistRemoteMediator(
    private val fetchDataRepository: FetchDataRepository,
    private val getWatchlistSyncStatus: GetWatchlistSyncStatus,
    private val syncWatchlist: SyncWatchlist,
    @InjectedParam private val type: ScreenplayTypeFilter
) : RemoteMediator<Int, Screenplay>() {

    private val key = SyncWatchlistKey(type)

    override suspend fun initialize(): InitializeAction = when (getWatchlistSyncStatus(key)) {
        RequiredSync.Complete, SyncNotRequired -> InitializeAction.SKIP_INITIAL_REFRESH
        RequiredSync.Initial -> InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(loadType: LoadType, state: PagingState<Int, Screenplay>): MediatorResult {
        val syncStatus = when (loadType) {
            LoadType.REFRESH -> RequiredSync.Initial
            // Prepend is not supported
            LoadType.PREPEND -> SyncNotRequired
            LoadType.APPEND -> getWatchlistSyncStatus(key)
        }

        return when (syncStatus) {
            is RequiredSync -> syncWatchlist(type, syncStatus).fold(
                ifLeft = { networkError -> MediatorResult.Error(FetchException(networkError)) },
                ifRight = {
                    fetchDataRepository.set(key, syncStatus.toBookmark())
                    MediatorResult.Success(endOfPaginationReached = syncStatus is RequiredSync.Complete)
                }
            )
            SyncNotRequired -> MediatorResult.Success(endOfPaginationReached = true)
        }
    }
}

@Factory
internal class WatchlistRemoteMediatorFactory : KoinComponent {

    fun create(sorting: ListSorting, type: ScreenplayTypeFilter): WatchlistRemoteMediator =
        get { parametersOf(sorting, type) }
}
