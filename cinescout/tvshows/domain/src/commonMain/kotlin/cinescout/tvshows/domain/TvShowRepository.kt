package cinescout.tvshows.domain

import arrow.core.Either
import cinescout.error.DataError
import cinescout.screenplay.domain.model.Rating
import cinescout.store5.StoreFlow
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
import store.PagedStore
import store.Paging
import store.Refresh
import store.Store

interface TvShowRepository {

    suspend fun addToDisliked(tvShowId: TmdbTvShowId)

    suspend fun addToLiked(tvShowId: TmdbTvShowId)

    suspend fun addToWatchlist(tvShowId: TmdbTvShowId): Either<DataError.Remote, Unit>

    fun getAllDislikedTvShows(): Flow<List<TvShow>>

    fun getAllLikedTvShows(): Flow<List<TvShow>>

    fun getAllRatedTvShows(refresh: Boolean): StoreFlow<List<TvShowWithPersonalRating>>

    fun getAllRatedTvShowIds(refresh: Boolean): StoreFlow<List<TvShowIdWithPersonalRating>>

    fun getAllWatchlistTvShows(refresh: Boolean): StoreFlow<List<TvShow>>

    fun getAllWatchlistTvShowIds(refresh: Boolean): StoreFlow<List<TmdbTvShowId>>

    fun getRecommendationsFor(tvShowId: TmdbTvShowId, refresh: Refresh): PagedStore<TvShow, Paging>

    fun getTvShowCredits(tvShowId: TmdbTvShowId, refresh: Refresh): Store<TvShowCredits>

    fun getTvShowDetails(tvShowId: TmdbTvShowId, refresh: Boolean): StoreFlow<TvShowWithDetails>

    fun getTvShowImages(tvShowId: TmdbTvShowId, refresh: Refresh): Store<TvShowImages>

    fun getTvShowKeywords(tvShowId: TmdbTvShowId, refresh: Refresh): Store<TvShowKeywords>

    fun getTvShowVideos(tvShowId: TmdbTvShowId, refresh: Refresh): Store<TvShowVideos>

    suspend fun rate(tvShowId: TmdbTvShowId, rating: Rating): Either<DataError.Remote, Unit>

    suspend fun removeFromWatchlist(tvShowId: TmdbTvShowId): Either<DataError.Remote, Unit>

    fun searchTvShows(query: String): PagedStore<TvShow, Paging>

}
