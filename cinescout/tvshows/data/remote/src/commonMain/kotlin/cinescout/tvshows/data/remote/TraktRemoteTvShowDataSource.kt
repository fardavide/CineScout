package cinescout.tvshows.data.remote

import arrow.core.Either
import cinescout.common.model.Rating
import cinescout.model.NetworkOperation
import cinescout.tvshows.data.remote.model.TraktPersonalTvShowRating
import cinescout.tvshows.domain.model.TmdbTvShowId
import store.PagedData
import store.Paging

interface TraktRemoteTvShowDataSource {

    suspend fun getRatedTvShows(
        page: Int
    ): Either<NetworkOperation, PagedData.Remote<TraktPersonalTvShowRating, Paging.Page.SingleSource>>

    suspend fun getWatchlistTvShows(
        page: Int
    ): Either<NetworkOperation, PagedData.Remote<TmdbTvShowId, Paging.Page.SingleSource>>

    suspend fun postRating(tvShowId: TmdbTvShowId, rating: Rating): Either<NetworkOperation, Unit>

    suspend fun postAddToWatchlist(tvShowId: TmdbTvShowId): Either<NetworkOperation, Unit>

    suspend fun postRemoveFromWatchlist(tvShowId: TmdbTvShowId): Either<NetworkOperation, Unit>
}
