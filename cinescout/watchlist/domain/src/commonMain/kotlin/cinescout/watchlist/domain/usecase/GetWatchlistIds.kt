package cinescout.watchlist.domain.usecase

import cinescout.CineScoutTestApi
import cinescout.notImplementedFake
import cinescout.screenplay.domain.model.ScreenplayTypeFilter
import cinescout.screenplay.domain.model.id.ScreenplayIds
import cinescout.store5.StoreFlow
import cinescout.watchlist.domain.model.WatchlistStoreKey
import cinescout.watchlist.domain.store.WatchlistIdsStore
import org.koin.core.annotation.Factory
import org.mobilenativefoundation.store.store5.StoreReadRequest

interface GetWatchlistIds {

    operator fun invoke(type: ScreenplayTypeFilter, refresh: Boolean): StoreFlow<List<ScreenplayIds>>
}

@Factory
internal class RealGetWatchlistIds(
    private val watchlistIdsStore: WatchlistIdsStore
) : GetWatchlistIds {

    override fun invoke(type: ScreenplayTypeFilter, refresh: Boolean): StoreFlow<List<ScreenplayIds>> =
        watchlistIdsStore.stream(StoreReadRequest.cached(WatchlistStoreKey.Read(type), refresh))
}

@CineScoutTestApi
class FakeGetWatchlistIds : GetWatchlistIds {

    override fun invoke(type: ScreenplayTypeFilter, refresh: Boolean): StoreFlow<List<ScreenplayIds>> =
        notImplementedFake()
}
