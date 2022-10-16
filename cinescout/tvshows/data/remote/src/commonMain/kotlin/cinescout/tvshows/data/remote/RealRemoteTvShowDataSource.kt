package cinescout.tvshows.data.remote

import arrow.core.Either
import cinescout.error.NetworkError
import cinescout.model.NetworkOperation
import cinescout.network.dualSourceCallWithResult
import cinescout.tvshows.data.RemoteTvShowDataSource
import cinescout.tvshows.domain.model.TmdbTvShowId
import cinescout.tvshows.domain.model.TvShowWithDetails
import store.PagedData
import store.Paging

class RealRemoteTvShowDataSource(
    private val tmdbSource: TmdbRemoteTvShowDataSource,
    private val traktSource: TraktRemoteTvShowDataSource
) : RemoteTvShowDataSource {

    override suspend fun getTvShowDetails(id: TmdbTvShowId): Either<NetworkError, TvShowWithDetails> =
        tmdbSource.getTvShowDetails(id)

    override suspend fun getWatchlistTvShows(
        page: Paging.Page.DualSources
    ): Either<NetworkOperation, PagedData.Remote<TmdbTvShowId, Paging.Page.DualSources>> =
        dualSourceCallWithResult(
            page = page,
            firstSourceCall = { paging ->
                tmdbSource.getWatchlistTvShows(paging.page).map { pagedData ->
                    pagedData.map { movie -> movie.tmdbId }
                }
            },
            secondSourceCall = { paging ->
                traktSource.getWatchlistTvShows(paging.page)
            }
        )
}
