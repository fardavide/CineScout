package cinescout.tvshows.data

import arrow.core.Either
import cinescout.common.model.Rating
import cinescout.error.NetworkError
import cinescout.model.NetworkOperation
import cinescout.tvshows.domain.model.TmdbTvShowId
import cinescout.tvshows.domain.model.TvShowCredits
import cinescout.tvshows.domain.model.TvShowIdWithPersonalRating
import cinescout.tvshows.domain.model.TvShowImages
import cinescout.tvshows.domain.model.TvShowKeywords
import cinescout.tvshows.domain.model.TvShowVideos
import cinescout.tvshows.domain.model.TvShowWithDetails
import store.PagedData
import store.Paging

interface RemoteTvShowDataSource {
    
    suspend fun getRatedTvShows(
        page: Paging.Page.DualSources
    ): Either<NetworkOperation, PagedData.Remote<TvShowIdWithPersonalRating, Paging.Page.DualSources>>
    
    suspend fun getTvShowCredits(movieId: TmdbTvShowId): Either<NetworkError, TvShowCredits>

    suspend fun getTvShowDetails(tvShowId: TmdbTvShowId): Either<NetworkError, TvShowWithDetails>

    suspend fun getTvShowImages(tvShowId: TmdbTvShowId): Either<NetworkError, TvShowImages>

    suspend fun getTvShowKeywords(tvShowId: TmdbTvShowId): Either<NetworkError, TvShowKeywords>

    suspend fun getTvShowVideos(tvShowId: TmdbTvShowId): Either<NetworkError, TvShowVideos>

    suspend fun getWatchlistTvShows(
        page: Paging.Page.DualSources
    ): Either<NetworkOperation, PagedData.Remote<TmdbTvShowId, Paging.Page.DualSources>>

    suspend fun postAddToWatchlist(tvShowId: TmdbTvShowId): Either<NetworkError, Unit>

    suspend fun postRating(tvShowId: TmdbTvShowId, rating: Rating): Either<NetworkError, Unit>

    suspend fun postRemoveFromWatchlist(tvShowId: TmdbTvShowId): Either<NetworkError, Unit>
}
