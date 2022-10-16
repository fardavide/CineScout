package cinescout.tvshows.data.remote

import arrow.core.Either
import cinescout.model.NetworkOperation
import cinescout.tvshows.domain.model.TmdbTvShowId
import store.PagedData
import store.Paging

interface TraktRemoteTvShowDataSource {

    suspend fun getWatchlistTvShows(
        page: Int
    ): Either<NetworkOperation, PagedData.Remote<TmdbTvShowId, Paging.Page.SingleSource>>
}
