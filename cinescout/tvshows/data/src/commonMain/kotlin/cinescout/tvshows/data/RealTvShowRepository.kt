package cinescout.tvshows.data

import arrow.core.Either
import cinescout.error.DataError
import cinescout.screenplay.domain.model.Rating
import cinescout.store5.StoreFlow
import cinescout.store5.cached
import cinescout.tvshows.data.store.RatedTvShowIdsStore
import cinescout.tvshows.data.store.RatedTvShowsStore
import cinescout.tvshows.data.store.TvShowDetailsKey
import cinescout.tvshows.data.store.TvShowDetailsStore
import cinescout.tvshows.data.store.WatchlistTvShowIdsStore
import cinescout.tvshows.data.store.WatchlistTvShowsStore
import cinescout.tvshows.domain.TvShowRepository
import cinescout.tvshows.domain.model.TmdbTvShowId
import cinescout.tvshows.domain.model.TvShow
import cinescout.tvshows.domain.model.TvShowCredits
import cinescout.tvshows.domain.model.TvShowIdWithPersonalRating
import cinescout.tvshows.domain.model.TvShowImages
import cinescout.tvshows.domain.model.TvShowKeywords
import cinescout.tvshows.domain.model.TvShowVideos
import cinescout.tvshows.domain.model.TvShowWithDetails
import cinescout.tvshows.domain.model.TvShowWithPersonalRating
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory
import org.mobilenativefoundation.store.store5.StoreReadRequest
import store.Fetcher
import store.PagedFetcher
import store.PagedReader
import store.PagedStore
import store.Paging
import store.Reader
import store.Refresh
import store.Store
import store.StoreKey
import store.StoreOwner

@Factory(binds = [TvShowRepository::class])
class RealTvShowRepository(
    val localTvShowDataSource: LocalTvShowDataSource,
    private val ratedTvShowIdsStore: RatedTvShowIdsStore,
    private val ratedTvShowsStore: RatedTvShowsStore,
    val remoteTvShowDataSource: RemoteTvShowDataSource,
    storeOwner: StoreOwner,
    private val tvShowDetailsStore: TvShowDetailsStore,
    private val watchlistTvShowIdsStore: WatchlistTvShowIdsStore,
    private val watchlistTvShowsStore: WatchlistTvShowsStore
) : TvShowRepository, StoreOwner by storeOwner {

    override suspend fun addToDisliked(tvShowId: TmdbTvShowId) {
        localTvShowDataSource.insertDisliked(tvShowId)
    }

    override suspend fun addToLiked(tvShowId: TmdbTvShowId) {
        localTvShowDataSource.insertLiked(tvShowId)
    }

    override suspend fun addToWatchlist(tvShowId: TmdbTvShowId): Either<DataError.Remote, Unit> {
        localTvShowDataSource.insertWatchlist(tvShowId)
        return remoteTvShowDataSource.postAddToWatchlist(tvShowId).mapLeft { error ->
            DataError.Remote(error)
        }
    }

    override fun getAllDislikedTvShows(): Flow<List<TvShow>> = localTvShowDataSource.findAllDislikedTvShows()

    override fun getAllLikedTvShows(): Flow<List<TvShow>> = localTvShowDataSource.findAllLikedTvShows()

    override fun getAllRatedTvShows(refresh: Boolean): StoreFlow<List<TvShowWithPersonalRating>> =
        ratedTvShowsStore.stream(StoreReadRequest.cached(refresh))

    override fun getAllRatedTvShowIds(refresh: Boolean): StoreFlow<List<TvShowIdWithPersonalRating>> =
        ratedTvShowIdsStore.stream(StoreReadRequest.cached(refresh))

    override fun getAllWatchlistTvShows(refresh: Boolean): StoreFlow<List<TvShow>> =
        watchlistTvShowsStore.stream(StoreReadRequest.cached(refresh))

    override fun getAllWatchlistTvShowIds(refresh: Boolean): StoreFlow<List<TmdbTvShowId>> =
        watchlistTvShowIdsStore.stream(StoreReadRequest.cached(refresh))

    override fun getRecommendationsFor(tvShowId: TmdbTvShowId, refresh: Refresh): PagedStore<TvShow, Paging> =
        PagedStore(
            key = StoreKey("recommendations", tvShowId),
            refresh = refresh,
            initialPage = Paging.Page.Initial,
            fetcher = PagedFetcher.forError { page -> remoteTvShowDataSource.getRecommendationsFor(tvShowId, page) },
            reader = PagedReader.fromSource(localTvShowDataSource.findRecommendationsFor(tvShowId)),
            write = { recommendedTvShows ->
                localTvShowDataSource.insertRecommendations(tvShowId = tvShowId, recommendations = recommendedTvShows)
            }
        )

    override fun getTvShowCredits(tvShowId: TmdbTvShowId, refresh: Refresh): Store<TvShowCredits> = Store(
        key = StoreKey("credits", tvShowId),
        refresh = refresh,
        fetcher = Fetcher.forError { remoteTvShowDataSource.getTvShowCredits(tvShowId) },
        reader = Reader.fromSource { localTvShowDataSource.findTvShowCredits(tvShowId) },
        write = { localTvShowDataSource.insertCredits(it) }
    )

    override fun getTvShowDetails(tvShowId: TmdbTvShowId, refresh: Boolean): StoreFlow<TvShowWithDetails> =
        tvShowDetailsStore.stream(StoreReadRequest.cached(TvShowDetailsKey(tvShowId), refresh))

    override fun getTvShowImages(tvShowId: TmdbTvShowId, refresh: Refresh): Store<TvShowImages> = Store(
        key = StoreKey("images", tvShowId),
        refresh = refresh,
        fetcher = Fetcher.forError { remoteTvShowDataSource.getTvShowImages(tvShowId) },
        reader = Reader.fromSource { localTvShowDataSource.findTvShowImages(tvShowId) },
        write = { localTvShowDataSource.insertImages(it) }
    )

    override fun getTvShowKeywords(tvShowId: TmdbTvShowId, refresh: Refresh): Store<TvShowKeywords> = Store(
        key = StoreKey("keywords", tvShowId),
        refresh = refresh,
        fetcher = Fetcher.forError { remoteTvShowDataSource.getTvShowKeywords(tvShowId) },
        reader = Reader.fromSource { localTvShowDataSource.findTvShowKeywords(tvShowId) },
        write = { localTvShowDataSource.insertKeywords(it) }
    )

    override fun getTvShowVideos(tvShowId: TmdbTvShowId, refresh: Refresh): Store<TvShowVideos> = Store(
        key = StoreKey("videos", tvShowId),
        refresh = refresh,
        fetcher = Fetcher.forError { remoteTvShowDataSource.getTvShowVideos(tvShowId) },
        reader = Reader.fromSource { localTvShowDataSource.findTvShowVideos(tvShowId) },
        write = { localTvShowDataSource.insertVideos(it) }
    )

    override suspend fun rate(tvShowId: TmdbTvShowId, rating: Rating): Either<DataError.Remote, Unit> {
        localTvShowDataSource.insertRating(tvShowId, rating)
        return remoteTvShowDataSource.postRating(tvShowId, rating).mapLeft { networkError ->
            DataError.Remote(networkError)
        }
    }

    override suspend fun removeFromWatchlist(tvShowId: TmdbTvShowId): Either<DataError.Remote, Unit> {
        localTvShowDataSource.deleteWatchlist(tvShowId)
        return remoteTvShowDataSource.postRemoveFromWatchlist(tvShowId).mapLeft { error ->
            DataError.Remote(error)
        }
    }

    override fun searchTvShows(query: String): PagedStore<TvShow, Paging> = PagedStore(
        key = StoreKey("search_tv_show", query),
        initialPage = Paging.Page.Initial,
        fetcher = PagedFetcher.forError { page -> remoteTvShowDataSource.searchTvShow(query, page) },
        reader = PagedReader.fromSource(localTvShowDataSource.findTvShowsByQuery(query)),
        write = { tvShows -> localTvShowDataSource.insert(tvShows) }
    )
}
