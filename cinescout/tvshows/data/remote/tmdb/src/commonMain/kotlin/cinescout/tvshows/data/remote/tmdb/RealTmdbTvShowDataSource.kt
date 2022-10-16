package cinescout.tvshows.data.remote.tmdb

import arrow.core.Either
import cinescout.auth.tmdb.domain.usecase.CallWithTmdbAccount
import cinescout.error.NetworkError
import cinescout.model.NetworkOperation
import cinescout.tvshows.data.remote.TmdbRemoteTvShowDataSource
import cinescout.tvshows.data.remote.tmdb.mapper.TmdbTvShowMapper
import cinescout.tvshows.data.remote.tmdb.service.TmdbTvShowService
import cinescout.tvshows.domain.model.TmdbTvShowId
import cinescout.tvshows.domain.model.TvShow
import cinescout.tvshows.domain.model.TvShowWithDetails
import store.PagedData
import store.Paging
import store.builder.toPagedData

internal class RealTmdbTvShowDataSource(
    private val callWithTmdbAccount: CallWithTmdbAccount,
    private val tvShowMapper: TmdbTvShowMapper,
    private val tvShowService: TmdbTvShowService
) : TmdbRemoteTvShowDataSource {

    override suspend fun getTvShowDetails(id: TmdbTvShowId): Either<NetworkError, TvShowWithDetails> =
        tvShowService.getTvShowDetails(id).map { tvShow -> tvShowMapper.toTvShowWithDetails(tvShow) }

    override suspend fun getWatchlistTvShows(
        page: Int
    ): Either<NetworkOperation, PagedData.Remote<TvShow, Paging.Page.SingleSource>> =
        callWithTmdbAccount {
            tvShowService.getTvShowWatchlist(page).map { response ->
                tvShowMapper.toTvShows(response)
                    .toPagedData(Paging.Page(response.page, response.totalPages))
            }
        }
}
