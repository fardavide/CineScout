package cinescout.tvshows.data.remote

import arrow.core.Either
import cinescout.error.NetworkError
import cinescout.tvshows.domain.model.TmdbTvShowId
import cinescout.tvshows.domain.model.TvShow
import cinescout.tvshows.domain.model.TvShowCredits
import cinescout.tvshows.domain.model.TvShowImages
import cinescout.tvshows.domain.model.TvShowKeywords
import cinescout.tvshows.domain.model.TvShowVideos
import cinescout.tvshows.domain.model.TvShowWithDetails
import store.PagedData

interface TmdbRemoteTvShowDataSource {

    suspend fun getRecommendationsFor(
        tvShowId: TmdbTvShowId,
        page: Int
    ): Either<NetworkError, PagedData.Remote<TvShow>>

    suspend fun getTvShowCredits(tvShowId: TmdbTvShowId): Either<NetworkError, TvShowCredits>

    suspend fun getTvShowDetails(tvShowId: TmdbTvShowId): Either<NetworkError, TvShowWithDetails>

    suspend fun getTvShowImages(tvShowId: TmdbTvShowId): Either<NetworkError, TvShowImages>

    suspend fun getTvShowKeywords(tvShowId: TmdbTvShowId): Either<NetworkError, TvShowKeywords>

    suspend fun getTvShowVideos(tvShowId: TmdbTvShowId): Either<NetworkError, TvShowVideos>

    suspend fun searchTvShow(query: String, page: Int): Either<NetworkError, PagedData.Remote<TvShow>>
}

class FakeTmdbRemoteTvShowDataSource : TmdbRemoteTvShowDataSource {

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

    override suspend fun searchTvShow(
        query: String,
        page: Int
    ): Either<NetworkError, PagedData.Remote<TvShow>> {
        TODO("Not yet implemented")
    }
}
