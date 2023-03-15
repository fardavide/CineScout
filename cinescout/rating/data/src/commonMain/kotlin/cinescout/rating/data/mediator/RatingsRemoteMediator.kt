package cinescout.rating.data.mediator

import androidx.paging.LoadType
import androidx.paging.PagingState
import app.cash.paging.RemoteMediator
import cinescout.rating.domain.model.ScreenplayWithPersonalRating
import cinescout.screenplay.domain.model.ScreenplayType
import cinescout.store5.FetchException
import org.koin.core.annotation.Factory

internal class RatingsRemoteMediator(
    private val listType: ScreenplayType,
    private val syncRatings: SyncRatings
) : RemoteMediator<Int, ScreenplayWithPersonalRating>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ScreenplayWithPersonalRating>
    ): MediatorResult {
        val nextPage = when (loadType) {
            LoadType.REFRESH -> 0
            LoadType.PREPEND -> state.pages.last().prevKey
                ?: return MediatorResult.Success(endOfPaginationReached = false)
            LoadType.APPEND -> state.pages.last().nextKey
                ?: return MediatorResult.Success(endOfPaginationReached = true)
        }

        return syncRatings(listType, nextPage).fold(
            ifLeft = { networkError -> MediatorResult.Error(FetchException(networkError)) },
            ifRight = { MediatorResult.Success(endOfPaginationReached = false) }
        )
    }
}

@Factory
internal class RatingsRemoteMediatorFactory(private val syncRatings: SyncRatings) {

    fun create(listType: ScreenplayType): RatingsRemoteMediator = RatingsRemoteMediator(listType, syncRatings)
}
