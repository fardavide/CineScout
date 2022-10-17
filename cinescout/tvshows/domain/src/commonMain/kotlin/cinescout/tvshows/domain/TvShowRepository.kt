package cinescout.tvshows.domain

import arrow.core.Either
import cinescout.common.model.Rating
import cinescout.error.DataError
import cinescout.tvshows.domain.model.*
import store.PagedStore
import store.Paging
import store.Refresh
import store.Store

interface TvShowRepository {

    suspend fun addToWatchlist(tvShowId: TmdbTvShowId): Either<DataError.Remote, Unit>

    fun getAllRatedTvShows(refresh: Refresh): PagedStore<TvShowWithPersonalRating, Paging>

    fun getAllWatchlistTvShows(refresh: Refresh): PagedStore<TvShow, Paging>

    fun getTvShowCredits(tvShowId: TmdbTvShowId, refresh: Refresh): Store<TvShowCredits>

    fun getTvShowDetails(tvShowId: TmdbTvShowId, refresh: Refresh): Store<TvShowWithDetails>

    fun getTvShowImages(tvShowId: TmdbTvShowId, refresh: Refresh): Store<TvShowImages>

    fun getTvShowKeywords(tvShowId: TmdbTvShowId, refresh: Refresh): Store<TvShowKeywords>

    fun getTvShowVideos(tvShowId: TmdbTvShowId, refresh: Refresh): Store<TvShowVideos>

    suspend fun rate(tvShowId: TmdbTvShowId, rating: Rating): Either<DataError.Remote, Unit>

    suspend fun removeFromWatchlist(tvShowId: TmdbTvShowId): Either<DataError.Remote, Unit>
}
