package cinescout.rating.data.mediator

import androidx.paging.LoadType
import androidx.paging.PagingState
import app.cash.paging.RemoteMediator
import cinescout.fetchdata.domain.repository.FetchDataRepository
import cinescout.rating.domain.model.ScreenplayWithPersonalRating
import cinescout.screenplay.domain.model.ScreenplayTypeFilter
import cinescout.store5.FetchException
import cinescout.sync.domain.model.RequiredSync
import cinescout.sync.domain.model.SyncNotRequired
import cinescout.sync.domain.util.toBookmark
import cinescout.sync.domain.util.toSyncStatus
import org.koin.core.annotation.Factory
import org.koin.core.annotation.InjectedParam
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.parameter.parametersOf
import kotlin.time.Duration.Companion.minutes

@Factory
internal class RatingsRemoteMediator(
    private val fetchDataRepository: FetchDataRepository,
    @InjectedParam private val type: ScreenplayTypeFilter,
    private val syncRatings: SyncRatings
) : RemoteMediator<Int, ScreenplayWithPersonalRating>() {

    private val key = Key(type)
    private val expiration = 5.minutes

    // TODO: Always refresh, since we're going to check if needed
    override suspend fun initialize(): InitializeAction =
        when (fetchDataRepository.getPage(key, expiration = expiration)) {
            null -> InitializeAction.LAUNCH_INITIAL_REFRESH
            else -> InitializeAction.SKIP_INITIAL_REFRESH
        }

    // TODO: check if should fetch: No, Initial, Complete
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ScreenplayWithPersonalRating>
    ): MediatorResult {
        val syncStatus = when (loadType) {
            LoadType.REFRESH -> RequiredSync.Initial
            // Prepend is not supported
            LoadType.PREPEND -> SyncNotRequired
            LoadType.APPEND -> fetchDataRepository.get(key, expiration = expiration).toSyncStatus()
        }

        return when (syncStatus) {
            is RequiredSync -> syncRatings(type, syncStatus).fold(
                ifLeft = { networkError -> MediatorResult.Error(FetchException(networkError)) },
                ifRight = {
                    fetchDataRepository.set(key, syncStatus.toBookmark())
                    MediatorResult.Success(endOfPaginationReached = syncStatus is RequiredSync.Complete)
                }
            )
            SyncNotRequired -> MediatorResult.Success(endOfPaginationReached = true)
        }
    }

    data class Key(val type: ScreenplayTypeFilter)
}

@Factory
internal class RatingsRemoteMediatorFactory : KoinComponent {

    fun create(type: ScreenplayTypeFilter): RatingsRemoteMediator = get { parametersOf(type) }
}
