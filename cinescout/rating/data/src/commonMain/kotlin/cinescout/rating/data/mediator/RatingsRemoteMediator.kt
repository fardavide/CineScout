package cinescout.rating.data.mediator

import androidx.paging.LoadType
import androidx.paging.PagingState
import app.cash.paging.RemoteMediator
import cinescout.error.NetworkError
import cinescout.rating.domain.model.ScreenplayWithPersonalRating
import cinescout.screenplay.domain.model.ScreenplayType
import cinescout.store5.FetchException
import org.koin.core.annotation.Factory
import org.koin.core.annotation.InjectedParam
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.parameter.parametersOf

@Factory
internal class RatingsRemoteMediator(
    @InjectedParam private val listType: ScreenplayType,
    private val syncRatings: SyncRatings
) : RemoteMediator<Int, ScreenplayWithPersonalRating>() {

    private var lastPage = 0

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ScreenplayWithPersonalRating>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> 1
            // Prepend is not supported
            LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
            LoadType.APPEND -> {
                // TODO: Get last page + 1
                ++lastPage
            }
        }

        return syncRatings(listType, page).fold(
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
internal class RatingsRemoteMediatorFactory : KoinComponent {

    fun create(listType: ScreenplayType): RatingsRemoteMediator = get { parametersOf(listType) }
}
