package cinescout.tvshows.data.remote

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import cinescout.common.model.Rating
import cinescout.error.NetworkError
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
import store.builder.toRemotePagedData

interface TmdbRemoteTvShowDataSource {

    suspend fun getRatedTvShows(page: Int): Either<NetworkError, PagedData.Remote<TvShowWithPersonalRating>>

    suspend fun getRecommendationsFor(
        tvShowId: TmdbTvShowId,
        page: Int
    ): Either<NetworkError, PagedData.Remote<TvShow>>

    suspend fun getTvShowCredits(tvShowId: TmdbTvShowId): Either<NetworkError, TvShowCredits>

    suspend fun getTvShowDetails(tvShowId: TmdbTvShowId): Either<NetworkError, TvShowWithDetails>

    suspend fun getTvShowImages(tvShowId: TmdbTvShowId): Either<NetworkError, TvShowImages>

    suspend fun getTvShowKeywords(tvShowId: TmdbTvShowId): Either<NetworkError, TvShowKeywords>

    suspend fun getTvShowVideos(tvShowId: TmdbTvShowId): Either<NetworkError, TvShowVideos>

    suspend fun getWatchlistTvShows(page: Int): Either<NetworkError, PagedData.Remote<TvShow>>

    suspend fun postRating(tvShowId: TmdbTvShowId, rating: Rating): Either<NetworkError, Unit>

    suspend fun postAddToWatchlist(tvShowId: TmdbTvShowId): Either<NetworkError, Unit>

    suspend fun postRemoveFromWatchlist(tvShowId: TmdbTvShowId): Either<NetworkError, Unit>

    suspend fun searchTvShow(query: String, page: Int): Either<NetworkError, PagedData.Remote<TvShow>>
}

class FakeTmdbRemoteTvShowDataSource(
    private val watchlistTvShows: List<TvShow>? = null
) : TmdbRemoteTvShowDataSource {

    override suspend fun getRatedTvShows(
        page: Int
    ): Either<NetworkError, PagedData.Remote<TvShowWithPersonalRating>> {
        TODO("Not yet implemented")
    }

    override suspend fun getRecommendationsFor(
        tvShowId: TmdbTvShowId,
        page: Int
    ): Either<NetworkError, PagedData.Remote<TvShow>> {
        TODO("Not yet implemented")
    }

    override suspend fun getTvShowCredits(tvShowId: TmdbTvShowId): Either<NetworkError, TvShowCredits> {
        TODO("Not yet implemented")
    }

    override suspend fun getTvShowDetails(tvShowId: TmdbTvShowId): Either<NetworkError, TvShowWithDetails> {
        TODO("Not yet implemented")
    }

    override suspend fun getTvShowImages(tvShowId: TmdbTvShowId): Either<NetworkError, TvShowImages> {
        TODO("Not yet implemented")
    }

    override suspend fun getTvShowKeywords(tvShowId: TmdbTvShowId): Either<NetworkError, TvShowKeywords> {
        TODO("Not yet implemented")
    }

    override suspend fun getTvShowVideos(tvShowId: TmdbTvShowId): Either<NetworkError, TvShowVideos> {
        TODO("Not yet implemented")
    }

    override suspend fun getWatchlistTvShows(page: Int): Either<NetworkError, PagedData.Remote<TvShow>> =
        watchlistTvShows?.toRemotePagedData(Paging.Page.Initial)?.right() ?: NetworkError.Unknown.left()

    override suspend fun postRating(tvShowId: TmdbTvShowId, rating: Rating): Either<NetworkError, Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun postAddToWatchlist(tvShowId: TmdbTvShowId): Either<NetworkError, Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun postRemoveFromWatchlist(tvShowId: TmdbTvShowId): Either<NetworkError, Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun searchTvShow(
        query: String,
        page: Int
    ): Either<NetworkError, PagedData.Remote<TvShow>> {
        TODO("Not yet implemented")
    }
}
