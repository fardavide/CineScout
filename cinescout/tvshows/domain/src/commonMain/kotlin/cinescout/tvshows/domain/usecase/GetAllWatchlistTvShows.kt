package cinescout.tvshows.domain.usecase

import cinescout.error.NetworkError
import cinescout.store5.StoreFlow
import cinescout.store5.test.storeFlowOf
import cinescout.tvshows.domain.model.TvShow
import cinescout.tvshows.domain.store.WatchlistTvShowsStore
import org.koin.core.annotation.Factory
import org.mobilenativefoundation.store.store5.StoreReadRequest

interface GetAllWatchlistTvShows {

    operator fun invoke(refresh: Boolean = true): StoreFlow<List<TvShow>>
}

@Factory
class RealGetAllWatchlistTvShows(
    private val watchlistTvShowsStore: WatchlistTvShowsStore
) : GetAllWatchlistTvShows {

    override operator fun invoke(refresh: Boolean): StoreFlow<List<TvShow>> =
        watchlistTvShowsStore.stream(StoreReadRequest.cached(Unit, refresh = refresh))
}

class FakeGetAllWatchlistTvShows(
    private val watchlist: List<TvShow>? = null
) : GetAllWatchlistTvShows {

    override operator fun invoke(refresh: Boolean): StoreFlow<List<TvShow>> =
        watchlist?.let(::storeFlowOf) ?: storeFlowOf(NetworkError.NotFound)
}
