package cinescout.rating.data.mediator

import androidx.paging.LoadType
import androidx.paging.PagingState
import app.cash.paging.RemoteMediator
import cinescout.fetchdata.domain.repository.FetchDataRepository
import cinescout.rating.domain.model.ScreenplayWithPersonalRating
import cinescout.screenplay.domain.model.ScreenplayTypeFilter
import cinescout.store5.FetchException
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
        val syncType = when (loadType) {
            LoadType.REFRESH -> SyncRatings.Type.Initial
            // Prepend is not supported
            LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
            LoadType.APPEND -> when (fetchDataRepository.getPage(key, expiration = expiration)) {
                null -> SyncRatings.Type.Initial
                1 -> SyncRatings.Type.Complete
                else -> return MediatorResult.Success(endOfPaginationReached = true)
            }
        }

        return syncRatings(type, syncType).fold(
            ifLeft = { networkError -> MediatorResult.Error(FetchException(networkError)) },
            ifRight = {
                val page = when (syncType) {
                    SyncRatings.Type.Initial -> 1
                    SyncRatings.Type.Complete -> 2
                }
                fetchDataRepository.set(key, page)
                MediatorResult.Success(endOfPaginationReached = syncType == SyncRatings.Type.Complete)
            }
        )
    }

    data class Key(val type: ScreenplayTypeFilter)
}

@Factory
internal class RatingsRemoteMediatorFactory : KoinComponent {

    fun create(type: ScreenplayTypeFilter): RatingsRemoteMediator = get { parametersOf(type) }
}
