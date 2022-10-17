package cinescout.tvshows.data

import arrow.core.Either
import arrow.core.continuations.either
import cinescout.common.model.Rating
import cinescout.error.DataError
import cinescout.model.NetworkOperation
import cinescout.tvshows.domain.TvShowRepository
import cinescout.tvshows.domain.model.*
import store.*
import store.ext.requireFirst

class RealTvShowRepository(
    val localTvShowDataSource: LocalTvShowDataSource,
    val remoteTvShowDataSource: RemoteTvShowDataSource,
    storeOwner: StoreOwner
) : TvShowRepository, StoreOwner by storeOwner {

    override fun getTvShowDetails(tvShowId: TmdbTvShowId, refresh: Refresh): Store<TvShowWithDetails> =
        Store(
            key = StoreKey("tvShow_details", tvShowId),
            refresh = refresh,
            fetch = Fetcher.forError { remoteTvShowDataSource.getTvShowDetails(tvShowId) },
            read = { localTvShowDataSource.findTvShowWithDetails(tvShowId) },
            write = { localTvShowDataSource.insert(it) }
        )

    override fun getTvShowImages(tvShowId: TmdbTvShowId, refresh: Refresh): Store<TvShowImages> {
        TODO("Not yet implemented")
    }

    override fun getTvShowKeywords(tvShowId: TmdbTvShowId, refresh: Refresh): Store<TvShowKeywords> {
        TODO("Not yet implemented")
    }

    override fun getTvShowVideos(tvShowId: TmdbTvShowId, refresh: Refresh): Store<TvShowVideos> {
        TODO("Not yet implemented")
    }

    override suspend fun rate(tvShowId: TmdbTvShowId, rating: Rating): Either<DataError.Remote, Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun removeFromWatchlist(tvShowId: TmdbTvShowId): Either<DataError.Remote, Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun addToWatchlist(tvShowId: TmdbTvShowId): Either<DataError.Remote, Unit> {
        TODO("Not yet implemented")
    }

    override fun getAllRatedTvShows(refresh: Refresh): PagedStore<TvShowWithPersonalRating, Paging> {
        TODO("Not yet implemented")
    }

    override fun getAllWatchlistTvShows(refresh: Refresh): PagedStore<TvShow, Paging> =
        PagedStore(
            key = StoreKey<TvShow>("watchlist"),
            refresh = refresh,
            initialPage = Paging.Page.DualSources.Initial,
            fetch = { page ->
                either {
                    val watchlistIds = remoteTvShowDataSource.getWatchlistTvShows(page).bind()
                    val watchlistWithDetails = watchlistIds.map { id ->
                        getTvShowDetails(id, refresh).requireFirst()
                            .mapLeft { NetworkOperation.Error(it) }
                            .bind()
                    }
                    watchlistWithDetails.map { it.tvShow }
                }
            },
            read = { localTvShowDataSource.findAllWatchlistTvShows() },
            write = { localTvShowDataSource.insertWatchlist(it) },
            delete = { localTvShowDataSource.deleteWatchlist(it) }
        )

    override fun getTvShowCredits(tvShowId: TmdbTvShowId, refresh: Refresh): Store<TvShowCredits> {
        TODO("Not yet implemented")
    }
}
