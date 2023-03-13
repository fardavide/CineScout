package cinescout.watchlist.domain.usecase

import cinescout.lists.domain.ListType
import cinescout.screenplay.domain.model.TmdbScreenplayId
import cinescout.store5.StoreFlow
import cinescout.watchlist.domain.WatchlistIdsStore
import cinescout.watchlist.domain.WatchlistIdsStoreKey
import org.koin.core.annotation.Factory
import org.mobilenativefoundation.store.store5.StoreReadRequest

interface GetWatchlistIds {

    operator fun invoke(listType: ListType, refresh: Boolean): StoreFlow<List<TmdbScreenplayId>>
}

@Factory
internal class RealGetWatchlistIds(
    private val watchlistIdsStore: WatchlistIdsStore
) : GetWatchlistIds {

    override fun invoke(listType: ListType, refresh: Boolean): StoreFlow<List<TmdbScreenplayId>> =
        watchlistIdsStore.stream(StoreReadRequest.cached(WatchlistIdsStoreKey(listType), refresh))
}
