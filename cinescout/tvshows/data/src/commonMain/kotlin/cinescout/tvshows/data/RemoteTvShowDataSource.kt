package cinescout.tvshows.data

import arrow.core.Either
import cinescout.error.NetworkError
import cinescout.tvshows.domain.model.TmdbTvShowId
import cinescout.tvshows.domain.model.TvShowWithDetails
import store.PagedData
import store.Paging

interface RemoteTvShowDataSource {

    suspend fun getTvShowDetails(id: TmdbTvShowId): Either<NetworkError, TvShowWithDetails>

    suspend fun getWatchlistTvShows(
        page: Paging.Page.DualSources
    ): Either<NetworkError, PagedData.Remote<TmdbTvShowId, Paging.Page.DualSources>>
}
