package cinescout.watchlist.data.mediator

import androidx.paging.LoadType
import androidx.paging.PagingState
import app.cash.paging.RemoteMediator
import cinescout.error.NetworkError
import cinescout.fetchdata.domain.repository.FetchDataRepository
import cinescout.screenplay.domain.model.Screenplay
import cinescout.screenplay.domain.model.ScreenplayType
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
    @InjectedParam private val listType: ScreenplayType,
    private val syncWatchlist: SyncWatchlist
) : RemoteMediator<Int, Screenplay>() {

    private val key = Key(listType)
    private val expiration = 5.minutes

    override suspend fun load(loadType: LoadType, state: PagingState<Int, Screenplay>): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> 1
            // Prepend is not supported
            LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
            LoadType.APPEND -> {
                fetchDataRepository.getPage(key, expiration = expiration)?.plus(1) ?: 1
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
                fetchDataRepository.set(key, page)
                MediatorResult.Success(endOfPaginationReached = false)
            }
        )
    }

    data class Key(val listType: ScreenplayType)
}

@Factory
internal class WatchlistRemoteMediatorFactory : KoinComponent {

    fun create(listType: ScreenplayType): WatchlistRemoteMediator = get { parametersOf(listType) }
}
