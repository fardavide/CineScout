package cinescout.tvshows.data

import arrow.core.Either
import cinescout.error.DataError
import cinescout.screenplay.domain.model.Rating
import cinescout.tvshows.domain.TvShowRepository
import cinescout.tvshows.domain.model.TmdbTvShowId
import cinescout.tvshows.domain.model.TvShow
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory
import store.PagedFetcher
import store.PagedReader
import store.PagedStore
import store.Paging
import store.Refresh
import store.StoreKey
import store.StoreOwner

@Factory(binds = [TvShowRepository::class])
class RealTvShowRepository(
    val localTvShowDataSource: LocalTvShowDataSource,
    val remoteTvShowDataSource: RemoteTvShowDataSource,
    storeOwner: StoreOwner
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
