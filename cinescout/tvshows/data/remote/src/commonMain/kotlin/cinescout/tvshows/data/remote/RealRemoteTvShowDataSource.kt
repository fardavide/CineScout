package cinescout.tvshows.data.remote

import arrow.core.Either
import arrow.core.continuations.either
import cinescout.auth.tmdb.domain.usecase.IsTmdbLinked
import cinescout.auth.trakt.domain.usecase.IsTraktLinked
import cinescout.error.NetworkError
import cinescout.network.DualSourceCall
import cinescout.tvshows.data.RemoteTvShowDataSource
import cinescout.tvshows.domain.model.TmdbTvShowId
import cinescout.tvshows.domain.model.TvShowWithDetails
import co.touchlab.kermit.Logger
import kotlinx.coroutines.flow.first
import store.PagedData
import store.Paging
import store.builder.mergePagedData

class RealRemoteTvShowDataSource(
    private val dualSourceCall: DualSourceCall,
    private val isTmdbLinked: IsTmdbLinked,
    private val isTraktLinked: IsTraktLinked,
    private val tmdbSource: TmdbRemoteTvShowDataSource,
    private val traktSource: TraktRemoteTvShowDataSource
) : RemoteTvShowDataSource {

    override suspend fun getTvShowDetails(id: TmdbTvShowId): Either<NetworkError, TvShowWithDetails> =
        tmdbSource.getTvShowDetails(id)

    override suspend fun getWatchlistTvShows(
        page: Paging.Page.DualSources
    ): Either<NetworkError, PagedData.Remote<TmdbTvShowId, Paging.Page.DualSources>> =
        either {
            val isTmdbLinked = isTmdbLinked().first()
            val isTraktLinked = isTraktLinked().first()

            val fromTmdb = if (isTmdbLinked && page.first.isValid()) {
                Logger.v("Fetching Tmdb watchlist: ${page.first}")
                tmdbSource.getWatchlistTvShows(page.first.page).bind().map { it.tmdbId }
            } else {
                PagedData.Remote(emptyList(), page.first)
            }
            val fromTrakt = if (isTraktLinked && page.second.isValid()) {
                Logger.v("Fetching Trakt watchlist: ${page.second}")
                traktSource.getWatchlistTvShows(page.second.page).bind()
            } else {
                PagedData.Remote(emptyList(), page.second)
            }
            mergePagedData(
                first = fromTmdb,
                second = fromTrakt,
                onConflict = { first, _ -> first }
            )
        }
}
