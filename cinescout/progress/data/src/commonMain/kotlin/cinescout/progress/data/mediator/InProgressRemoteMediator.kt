package cinescout.progress.data.mediator

import androidx.paging.RemoteMediator
import app.cash.paging.LoadType
import app.cash.paging.PagingState
import cinescout.history.domain.usecase.SyncHistory
import cinescout.screenplay.domain.model.Screenplay
import cinescout.screenplay.domain.model.ScreenplayTypeFilter
import cinescout.store5.FetchException
import cinescout.sync.domain.model.RequiredSync
import cinescout.sync.domain.model.SyncHistoryKey
import cinescout.sync.domain.model.SyncNotRequired
import cinescout.sync.domain.usecase.GetHistorySyncStatus
import org.koin.core.annotation.Factory
import org.koin.core.annotation.InjectedParam
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.parameter.parametersOf

@Factory
internal class InProgressRemoteMediator(
    private val getHistorySyncStatus: GetHistorySyncStatus,
    private val syncHistory: SyncHistory,
    @InjectedParam type: ScreenplayTypeFilter
) : RemoteMediator<Int, Screenplay>() {

    private val key = SyncHistoryKey(type)

    override suspend fun initialize(): InitializeAction = when (getHistorySyncStatus(key)) {
        RequiredSync.Complete, SyncNotRequired -> InitializeAction.SKIP_INITIAL_REFRESH
        RequiredSync.Initial -> InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(loadType: LoadType, state: PagingState<Int, Screenplay>): MediatorResult {
        val syncStatus = when (loadType) {
            LoadType.REFRESH -> RequiredSync.Initial
            // Prepend is not supported
            LoadType.PREPEND -> SyncNotRequired
            LoadType.APPEND -> getHistorySyncStatus(key)
        }

        return when (syncStatus) {
            is RequiredSync -> syncHistory(key, syncStatus).fold(
                ifLeft = { networkError -> MediatorResult.Error(FetchException(networkError)) },
                ifRight = { MediatorResult.Success(endOfPaginationReached = syncStatus is RequiredSync.Complete) }
            )
            SyncNotRequired -> MediatorResult.Success(endOfPaginationReached = true)
        }
    }
}

@Factory
internal class InProgressRemoteMediatorFactory : KoinComponent {

    fun create(type: ScreenplayTypeFilter): InProgressRemoteMediator = get { parametersOf(type) }
}
