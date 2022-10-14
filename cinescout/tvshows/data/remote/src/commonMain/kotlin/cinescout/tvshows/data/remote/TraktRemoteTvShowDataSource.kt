package cinescout.tvshows.data.remote

import arrow.core.Either
import cinescout.error.NetworkError
import cinescout.tvshows.domain.model.TmdbTvShowId
import store.PagedData
import store.Paging

interface TraktRemoteTvShowDataSource {

    suspend fun getWatchlistTvShows(
        page: Int
    ): Either<NetworkError, PagedData.Remote<TmdbTvShowId, Paging.Page.SingleSource>>
}
