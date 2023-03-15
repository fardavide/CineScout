package cinescout.watchlist.domain.usecase

import cinescout.screenplay.domain.model.ScreenplayType
import cinescout.screenplay.domain.model.TmdbScreenplayId
import cinescout.store5.StoreFlow
import cinescout.watchlist.domain.model.WatchlistStoreKey
import cinescout.watchlist.domain.store.WatchlistIdsStore
import org.koin.core.annotation.Factory
import org.mobilenativefoundation.store.store5.StoreReadRequest

interface GetWatchlistIds {

    operator fun invoke(type: ScreenplayType, refresh: Boolean): StoreFlow<List<TmdbScreenplayId>>
}

@Factory
internal class RealGetWatchlistIds(
    private val watchlistIdsStore: WatchlistIdsStore
) : GetWatchlistIds {

    override fun invoke(type: ScreenplayType, refresh: Boolean): StoreFlow<List<TmdbScreenplayId>> =
        watchlistIdsStore.stream(StoreReadRequest.cached(WatchlistStoreKey.Read(type), refresh))
}

class FakeGetWatchlistIds : GetWatchlistIds {

    override fun invoke(type: ScreenplayType, refresh: Boolean): StoreFlow<List<TmdbScreenplayId>> = TODO()
}
