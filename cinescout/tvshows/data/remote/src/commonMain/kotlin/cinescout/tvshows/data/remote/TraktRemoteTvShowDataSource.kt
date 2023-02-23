package cinescout.tvshows.data.remote

import arrow.core.Either
import cinescout.common.model.Rating
import cinescout.error.NetworkError
import cinescout.tvshows.data.remote.model.TraktPersonalTvShowRating
import cinescout.tvshows.domain.model.TmdbTvShowId
import store.PagedData

interface TraktRemoteTvShowDataSource {

    suspend fun getRatedTvShows(page: Int): Either<NetworkError, PagedData.Remote<TraktPersonalTvShowRating>>

    suspend fun getWatchlistTvShows(page: Int): Either<NetworkError, PagedData.Remote<TmdbTvShowId>>

    suspend fun postRating(tvShowId: TmdbTvShowId, rating: Rating): Either<NetworkError, Unit>

    suspend fun postAddToWatchlist(tvShowId: TmdbTvShowId): Either<NetworkError, Unit>

    suspend fun postRemoveFromWatchlist(tvShowId: TmdbTvShowId): Either<NetworkError, Unit>
}
