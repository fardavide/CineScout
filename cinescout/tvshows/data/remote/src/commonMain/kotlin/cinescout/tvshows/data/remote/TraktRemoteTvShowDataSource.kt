package cinescout.tvshows.data.remote

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import cinescout.common.model.Rating
import cinescout.error.NetworkError
import cinescout.tvshows.data.remote.model.TraktPersonalTvShowRating
import cinescout.tvshows.domain.model.TmdbTvShowId
import cinescout.tvshows.domain.model.TvShow
import store.PagedData
import store.Paging
import store.builder.toPagedData

interface TraktRemoteTvShowDataSource {

    suspend fun getRatedTvShows(page: Int): Either<NetworkError, PagedData.Remote<TraktPersonalTvShowRating>>

    suspend fun getWatchlistTvShows(page: Int): Either<NetworkError, PagedData.Remote<TmdbTvShowId>>

    suspend fun postRating(tvShowId: TmdbTvShowId, rating: Rating): Either<NetworkError, Unit>

    suspend fun postAddToWatchlist(tvShowId: TmdbTvShowId): Either<NetworkError, Unit>

    suspend fun postRemoveFromWatchlist(tvShowId: TmdbTvShowId): Either<NetworkError, Unit>
}

class FakeTraktRemoteTvShowDataSource(
    watchlistTvShows: List<TvShow>? = null
) : TraktRemoteTvShowDataSource {

    private val watchlistTvShows: List<TmdbTvShowId>? = watchlistTvShows?.map { it.tmdbId }

    override suspend fun getRatedTvShows(
        page: Int
    ): Either<NetworkError, PagedData.Remote<TraktPersonalTvShowRating>> {
        TODO("Not yet implemented")
    }

    override suspend fun getWatchlistTvShows(
        page: Int
    ): Either<NetworkError, PagedData.Remote<TmdbTvShowId>> =
        watchlistTvShows?.toPagedData(Paging.Page.Initial)?.right() ?: NetworkError.Unknown.left()

    override suspend fun postRating(tvShowId: TmdbTvShowId, rating: Rating): Either<NetworkError, Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun postAddToWatchlist(tvShowId: TmdbTvShowId): Either<NetworkError, Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun postRemoveFromWatchlist(tvShowId: TmdbTvShowId): Either<NetworkError, Unit> {
        TODO("Not yet implemented")
    }
}
