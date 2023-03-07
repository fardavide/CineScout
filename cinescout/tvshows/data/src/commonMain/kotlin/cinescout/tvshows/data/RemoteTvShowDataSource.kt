package cinescout.tvshows.data

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import cinescout.error.NetworkError
import cinescout.model.NetworkOperation
import cinescout.screenplay.domain.model.Rating
import cinescout.tvshows.domain.model.TmdbTvShowId
import cinescout.tvshows.domain.model.TvShow
import cinescout.tvshows.domain.model.TvShowCredits
import cinescout.tvshows.domain.model.TvShowIdWithPersonalRating
import cinescout.tvshows.domain.model.TvShowImages
import cinescout.tvshows.domain.model.TvShowKeywords
import cinescout.tvshows.domain.model.TvShowVideos
import cinescout.tvshows.domain.model.TvShowWithDetails
import store.PagedData
import store.Paging

interface RemoteTvShowDataSource {
    
    suspend fun getRatedTvShows(): Either<NetworkOperation, List<TvShowIdWithPersonalRating>>

    suspend fun getRecommendationsFor(
        tvShowId: TmdbTvShowId,
        page: Paging.Page
    ): Either<NetworkError, PagedData.Remote<TvShow>>
    
    suspend fun getTvShowCredits(movieId: TmdbTvShowId): Either<NetworkError, TvShowCredits>

    suspend fun getTvShowDetails(tvShowId: TmdbTvShowId): Either<NetworkError, TvShowWithDetails>

    suspend fun getTvShowImages(tvShowId: TmdbTvShowId): Either<NetworkError, TvShowImages>

    suspend fun getTvShowKeywords(tvShowId: TmdbTvShowId): Either<NetworkError, TvShowKeywords>

    suspend fun getTvShowVideos(tvShowId: TmdbTvShowId): Either<NetworkError, TvShowVideos>

    suspend fun getWatchlistTvShows(): Either<NetworkOperation, List<TmdbTvShowId>>

    suspend fun postAddToWatchlist(tvShowId: TmdbTvShowId): Either<NetworkError, Unit>

    suspend fun postRating(tvShowId: TmdbTvShowId, rating: Rating): Either<NetworkError, Unit>

    suspend fun postRemoveFromWatchlist(tvShowId: TmdbTvShowId): Either<NetworkError, Unit>

    suspend fun searchTvShow(query: String, page: Paging.Page): Either<NetworkError, PagedData.Remote<TvShow>>
}

class FakeRemoteTvShowDataSource(
    private val ratedTvShowIds: List<TvShowIdWithPersonalRating>? = null,
    private val tvShowsDetails: List<TvShowWithDetails>? = null,
    private val watchlistTvShowIds: List<TmdbTvShowId>? = null
) : RemoteTvShowDataSource {

    override suspend fun getRatedTvShows(): Either<NetworkOperation, List<TvShowIdWithPersonalRating>> =
        ratedTvShowIds?.right() ?: NetworkOperation.Error(NetworkError.Unknown).left()

    override suspend fun getRecommendationsFor(
        tvShowId: TmdbTvShowId,
        page: Paging.Page
    ): Either<NetworkError, PagedData.Remote<TvShow>> {
        TODO("Not yet implemented")
    }

    override suspend fun getTvShowCredits(movieId: TmdbTvShowId): Either<NetworkError, TvShowCredits> {
        TODO("Not yet implemented")
    }

    override suspend fun getTvShowDetails(tvShowId: TmdbTvShowId): Either<NetworkError, TvShowWithDetails> =
        tvShowsDetails?.find { it.tvShow.tmdbId == tvShowId }?.right()
            ?: NetworkError.NotFound.left()

    override suspend fun getTvShowImages(tvShowId: TmdbTvShowId): Either<NetworkError, TvShowImages> {
        TODO("Not yet implemented")
    }

    override suspend fun getTvShowKeywords(tvShowId: TmdbTvShowId): Either<NetworkError, TvShowKeywords> {
        TODO("Not yet implemented")
    }

    override suspend fun getTvShowVideos(tvShowId: TmdbTvShowId): Either<NetworkError, TvShowVideos> {
        TODO("Not yet implemented")
    }

    override suspend fun getWatchlistTvShows(): Either<NetworkOperation, List<TmdbTvShowId>> =
        watchlistTvShowIds?.right() ?: NetworkOperation.Error(NetworkError.Unknown).left()

    override suspend fun postAddToWatchlist(tvShowId: TmdbTvShowId): Either<NetworkError, Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun postRating(tvShowId: TmdbTvShowId, rating: Rating): Either<NetworkError, Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun postRemoveFromWatchlist(tvShowId: TmdbTvShowId): Either<NetworkError, Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun searchTvShow(
        query: String,
        page: Paging.Page
    ): Either<NetworkError, PagedData.Remote<TvShow>> {
        TODO("Not yet implemented")
    }
}
