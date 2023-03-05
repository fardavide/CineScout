package cinescout.tvshows.data.remote.tmdb

import arrow.core.Either
import cinescout.error.NetworkError
import cinescout.tvshows.data.remote.TmdbRemoteTvShowDataSource
import cinescout.tvshows.data.remote.tmdb.mapper.TmdbTvShowCreditsMapper
import cinescout.tvshows.data.remote.tmdb.mapper.TmdbTvShowImagesMapper
import cinescout.tvshows.data.remote.tmdb.mapper.TmdbTvShowKeywordMapper
import cinescout.tvshows.data.remote.tmdb.mapper.TmdbTvShowMapper
import cinescout.tvshows.data.remote.tmdb.mapper.TmdbTvShowVideosMapper
import cinescout.tvshows.data.remote.tmdb.service.TmdbTvShowSearchService
import cinescout.tvshows.data.remote.tmdb.service.TmdbTvShowService
import cinescout.tvshows.domain.model.TmdbTvShowId
import cinescout.tvshows.domain.model.TvShow
import cinescout.tvshows.domain.model.TvShowCredits
import cinescout.tvshows.domain.model.TvShowImages
import cinescout.tvshows.domain.model.TvShowKeywords
import cinescout.tvshows.domain.model.TvShowVideos
import cinescout.tvshows.domain.model.TvShowWithDetails
import org.koin.core.annotation.Factory
import store.PagedData
import store.Paging
import store.builder.toRemotePagedData

@Factory
internal class RealTmdbTvShowDataSource(
    private val tvShowCreditsMapper: TmdbTvShowCreditsMapper,
    private val tvShowKeywordMapper: TmdbTvShowKeywordMapper,
    private val tvShowImagesMapper: TmdbTvShowImagesMapper,
    private val tvShowMapper: TmdbTvShowMapper,
    private val tvShowSearchService: TmdbTvShowSearchService,
    private val tvShowService: TmdbTvShowService,
    private val tvShowVideosMapper: TmdbTvShowVideosMapper
) : TmdbRemoteTvShowDataSource {

    override suspend fun getRecommendationsFor(
        tvShowId: TmdbTvShowId,
        page: Int
    ): Either<NetworkError, PagedData.Remote<TvShow>> =
        tvShowService.getRecommendationsFor(tvShowId, page).map { response ->
            tvShowMapper.toTvShows(response.tmdbTvShows())
                .toRemotePagedData(Paging.Page(response.page, response.totalPages))
        }

    override suspend fun getTvShowCredits(tvShowId: TmdbTvShowId): Either<NetworkError, TvShowCredits> =
        tvShowService.getTvShowCredits(tvShowId)
            .map { tmdbTvShowCredits -> tvShowCreditsMapper.toTvShowCredits(tmdbTvShowCredits) }

    override suspend fun getTvShowDetails(tvShowId: TmdbTvShowId): Either<NetworkError, TvShowWithDetails> =
        tvShowService.getTvShowDetails(tvShowId).map { tvShow -> tvShowMapper.toTvShowWithDetails(tvShow) }

    override suspend fun getTvShowImages(tvShowId: TmdbTvShowId): Either<NetworkError, TvShowImages> =
        tvShowService.getTvShowImages(tvShowId)
            .map { tmdbTvShowImages -> tvShowImagesMapper.toTvShowImages(tmdbTvShowImages) }

    override suspend fun getTvShowKeywords(tvShowId: TmdbTvShowId): Either<NetworkError, TvShowKeywords> =
        tvShowService.getTvShowKeywords(tvShowId)
            .map { tmdbTvShowKeywords -> tvShowKeywordMapper.toTvShowKeywords(tmdbTvShowKeywords) }

    override suspend fun getTvShowVideos(tvShowId: TmdbTvShowId): Either<NetworkError, TvShowVideos> =
        tvShowService.getTvShowVideos(tvShowId)
            .map { tmdbTvShowVideos -> tvShowVideosMapper.toTvShowVideos(tmdbTvShowVideos) }

    override suspend fun searchTvShow(
        query: String,
        page: Int
    ): Either<NetworkError, PagedData.Remote<TvShow>> =
        tvShowSearchService.searchTvShow(query, page).map { response ->
            tvShowMapper.toTvShows(response.tmdbTvShows())
                .toRemotePagedData(Paging.Page(response.page, response.totalPages))
        }
}
