package cinescout.tvshows.data

import arrow.core.continuations.either
import cinescout.tvshows.domain.TvShowRepository
import cinescout.tvshows.domain.model.TmdbTvShowId
import cinescout.tvshows.domain.model.TvShow
import cinescout.tvshows.domain.model.TvShowWithDetails
import store.PagedStore
import store.Paging
import store.Refresh
import store.Store
import store.StoreKey
import store.StoreOwner
import store.ext.requireFirst

class RealTvShowRepository(
    val localTvShowDataSource: LocalTvShowDataSource,
    val remoteTvShowDataSource: RemoteTvShowDataSource,
    storeOwner: StoreOwner
) : TvShowRepository, StoreOwner by storeOwner {

    override fun getTvShowDetails(id: TmdbTvShowId, refresh: Refresh): Store<TvShowWithDetails> =
        Store(
            key = StoreKey("tvShow_details", id),
            refresh = refresh,
            fetch = { remoteTvShowDataSource.getTvShowDetails(id) },
            read = { localTvShowDataSource.findTvShowWithDetails(id) },
            write = { localTvShowDataSource.insert(it) }
        )

    override fun getAllWatchlistTvShows(refresh: Refresh): PagedStore<TvShow, Paging> =
        PagedStore(
            key = StoreKey<TvShow>("watchlist"),
            refresh = refresh,
            initialPage = Paging.Page.DualSources.Initial,
            fetch = { page ->
                either {
                    val watchlistIds = remoteTvShowDataSource.getWatchlistTvShows(page).bind()
                    val watchlistWithDetails = watchlistIds.map { id ->
                        getTvShowDetails(id, refresh).requireFirst().bind()
                    }
                    watchlistWithDetails.map { it.tvShow }
                }
            },
            read = { localTvShowDataSource.findAllWatchlistTvShows() },
            write = { localTvShowDataSource.insertWatchlist(it) },
            delete = { localTvShowDataSource.deleteWatchlist(it) }
        )
}
