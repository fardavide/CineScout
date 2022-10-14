package cinescout.tvshows.data.remote

import arrow.core.Either
import cinescout.error.NetworkError
import cinescout.tvshows.domain.model.TmdbTvShowId
import cinescout.tvshows.domain.model.TvShow
import cinescout.tvshows.domain.model.TvShowWithDetails
import store.PagedData
import store.Paging

interface TmdbRemoteTvShowDataSource {

    suspend fun getTvShowDetails(id: TmdbTvShowId): Either<NetworkError, TvShowWithDetails>

    suspend fun getWatchlistTvShows(
        page: Int
    ): Either<NetworkError, PagedData.Remote<TvShow, Paging.Page.SingleSource>>
}
