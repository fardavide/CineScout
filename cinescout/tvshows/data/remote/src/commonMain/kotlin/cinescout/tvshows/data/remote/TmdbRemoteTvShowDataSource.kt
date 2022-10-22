package cinescout.tvshows.data.remote

import arrow.core.Either
import cinescout.common.model.Rating
import cinescout.error.NetworkError
import cinescout.model.NetworkOperation
import cinescout.tvshows.domain.model.TmdbTvShowId
import cinescout.tvshows.domain.model.TvShow
import cinescout.tvshows.domain.model.TvShowCredits
import cinescout.tvshows.domain.model.TvShowImages
import cinescout.tvshows.domain.model.TvShowKeywords
import cinescout.tvshows.domain.model.TvShowVideos
import cinescout.tvshows.domain.model.TvShowWithDetails
import cinescout.tvshows.domain.model.TvShowWithPersonalRating
import store.PagedData
import store.Paging

interface TmdbRemoteTvShowDataSource {

    suspend fun getRatedTvShows(
        page: Int
    ): Either<NetworkOperation, PagedData.Remote<TvShowWithPersonalRating, Paging.Page.SingleSource>>

    suspend fun getRecommendationsFor(
        tvShowId: TmdbTvShowId,
        page: Int
    ): Either<NetworkError, PagedData.Remote<TvShow, Paging.Page.SingleSource>>

    suspend fun getTvShowCredits(tvShowId: TmdbTvShowId): Either<NetworkError, TvShowCredits>

    suspend fun getTvShowDetails(tvShowId: TmdbTvShowId): Either<NetworkError, TvShowWithDetails>

    suspend fun getTvShowImages(tvShowId: TmdbTvShowId): Either<NetworkError, TvShowImages>

    suspend fun getTvShowKeywords(tvShowId: TmdbTvShowId): Either<NetworkError, TvShowKeywords>

    suspend fun getTvShowVideos(tvShowId: TmdbTvShowId): Either<NetworkError, TvShowVideos>

    suspend fun getWatchlistTvShows(
        page: Int
    ): Either<NetworkOperation, PagedData.Remote<TvShow, Paging.Page.SingleSource>>

    suspend fun postRating(tvShowId: TmdbTvShowId, rating: Rating): Either<NetworkOperation, Unit>

    suspend fun postAddToWatchlist(tvShowId: TmdbTvShowId): Either<NetworkOperation, Unit>

    suspend fun postRemoveFromWatchlist(tvShowId: TmdbTvShowId): Either<NetworkOperation, Unit>
}
