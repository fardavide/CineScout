package cinescout.tvshows.data.remote.trakt

import arrow.core.Either
import cinescout.error.NetworkError
import cinescout.tvshows.data.remote.TraktRemoteTvShowDataSource
import cinescout.tvshows.data.remote.trakt.service.TraktTvShowService
import cinescout.tvshows.domain.model.TmdbTvShowId
import store.PagedData
import store.Paging

internal class RealTraktTvShowDataSource(
    private val service: TraktTvShowService
) : TraktRemoteTvShowDataSource {

    override suspend fun getWatchlistTvShows(
        page: Int
    ): Either<NetworkError, PagedData.Remote<TmdbTvShowId, Paging.Page.SingleSource>> =
        service.getWatchlistTvShows(page).map { pagedData ->
            pagedData.map { tvShow ->
                tvShow.tvShow.ids.tmdb
            }
        }
}
