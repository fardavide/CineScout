package cinescout.search.data.mediator

import androidx.paging.LoadType
import androidx.paging.PagingState
import app.cash.paging.RemoteMediator
import cinescout.error.NetworkError
import cinescout.fetchdata.domain.repository.FetchDataRepository
import cinescout.screenplay.domain.model.Screenplay
import cinescout.screenplay.domain.model.ScreenplayTypeFilter
import cinescout.store5.FetchException
import org.koin.core.annotation.Factory
import org.koin.core.annotation.InjectedParam
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.parameter.parametersOf
import kotlin.time.Duration.Companion.days

@Factory
internal class SearchRemoteMediator(
    private val fetchDataRepository: FetchDataRepository,
    @InjectedParam private val query: String,
    private val syncSearch: SyncSearch,
    @InjectedParam private val type: ScreenplayTypeFilter
) : RemoteMediator<Int, Screenplay>() {

    private val key = Key(type, query)
    private val expiration = 3.days

    override suspend fun initialize(): InitializeAction =
        when (fetchDataRepository.getPage(key, expiration = expiration)) {
            null -> InitializeAction.LAUNCH_INITIAL_REFRESH
            else -> InitializeAction.SKIP_INITIAL_REFRESH
        }

    override suspend fun load(loadType: LoadType, state: PagingState<Int, Screenplay>): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> 1
            // Prepend is not supported
            LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
            LoadType.APPEND -> fetchDataRepository.getPage(key, expiration = expiration)?.plus(1) ?: 1
        }

        return syncSearch(type, query, page).fold(
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

    data class Key(val type: ScreenplayTypeFilter, val query: String)
}

@Factory
internal class SearchRemoteMediatorFactory : KoinComponent {

    fun create(type: ScreenplayTypeFilter, query: String): SearchRemoteMediator = get {
        parametersOf(query, type)
    }
}
