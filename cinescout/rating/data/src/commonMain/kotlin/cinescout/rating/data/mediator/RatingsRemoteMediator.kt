package cinescout.rating.data.mediator

import androidx.paging.LoadType
import androidx.paging.PagingState
import app.cash.paging.RemoteMediator
import cinescout.rating.domain.model.ScreenplayWithPersonalRating
import cinescout.rating.domain.usecase.SyncRatings
import cinescout.screenplay.domain.model.ScreenplayTypeFilter
import cinescout.store5.FetchException
import cinescout.sync.domain.model.RequiredSync
import cinescout.sync.domain.model.SyncNotRequired
import cinescout.sync.domain.model.SyncRatingsKey
import cinescout.sync.domain.usecase.GetRatingsSyncStatus
import org.koin.core.annotation.Factory
import org.koin.core.annotation.InjectedParam
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.parameter.parametersOf

@Factory
internal class RatingsRemoteMediator(
    private val getRatingsSyncStatus: GetRatingsSyncStatus,
    @InjectedParam private val type: ScreenplayTypeFilter,
    private val syncRatings: SyncRatings
) : RemoteMediator<Int, ScreenplayWithPersonalRating>() {

    private val key = SyncRatingsKey(type)

    override suspend fun initialize(): InitializeAction = when (getRatingsSyncStatus(key)) {
        RequiredSync.Complete, SyncNotRequired -> InitializeAction.SKIP_INITIAL_REFRESH
        RequiredSync.Initial -> InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ScreenplayWithPersonalRating>
    ): MediatorResult {
        val syncStatus = when (loadType) {
            LoadType.REFRESH -> RequiredSync.Initial
            // Prepend is not supported
            LoadType.PREPEND -> SyncNotRequired
            LoadType.APPEND -> getRatingsSyncStatus(key)
        }

        return when (syncStatus) {
            is RequiredSync -> syncRatings(type, syncStatus).fold(
                ifLeft = { networkError -> MediatorResult.Error(FetchException(networkError)) },
                ifRight = { MediatorResult.Success(endOfPaginationReached = syncStatus is RequiredSync.Complete) }
            )
            SyncNotRequired -> MediatorResult.Success(endOfPaginationReached = true)
        }
    }
}

@Factory
internal class RatingsRemoteMediatorFactory : KoinComponent {

    fun create(type: ScreenplayTypeFilter): RatingsRemoteMediator = get { parametersOf(type) }
}
