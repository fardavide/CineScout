package cinescout.tvshows.data.remote

import arrow.core.Either
import arrow.core.getOrElse
import arrow.core.left
import arrow.core.right
import cinescout.error.NetworkError
import cinescout.model.NetworkOperation
import cinescout.network.DualSourceCall
import cinescout.tvshows.data.RemoteTvShowDataSource
import cinescout.tvshows.domain.model.TmdbTvShowId
import cinescout.tvshows.domain.model.TvShowWithDetails
import co.touchlab.kermit.Logger
import store.PagedData
import store.Paging
import store.builder.mergePagedData
import store.builder.pagedDataOf

class RealRemoteTvShowDataSource(
    private val dualSourceCall: DualSourceCall,
    private val tmdbSource: TmdbRemoteTvShowDataSource,
    private val traktSource: TraktRemoteTvShowDataSource
) : RemoteTvShowDataSource {

    override suspend fun getTvShowDetails(id: TmdbTvShowId): Either<NetworkError, TvShowWithDetails> =
        tmdbSource.getTvShowDetails(id)

    override suspend fun getWatchlistTvShows(
        page: Paging.Page.DualSources
    ): Either<NetworkOperation, PagedData.Remote<TmdbTvShowId, Paging.Page.DualSources>> {

        val fromTmdb = if (page.first.isValid()) {
            Logger.v("Fetching Tmdb watchlist: ${page.first}")
            tmdbSource.getWatchlistTvShows(page.first.page)
                .also { either -> Logger.v("Fetched Tmdb tv shows watchlist: $either") }
                .tapLeft { if (it !is NetworkOperation.Skipped) return it.left() }
                .map { pagedData -> pagedData.map { movie -> movie.tmdbId } }
        } else {
            NetworkOperation.Skipped.left()
        }
        val fromTrakt = if (page.second.isValid()) {
            Logger.v("Fetching Trakt watchlist: ${page.second}")
            traktSource.getWatchlistTvShows(page.second.page)
                .also { either -> Logger.v("Fetched Trakt tv shows watchlist: $either") }
                .tapLeft { if (it !is NetworkOperation.Skipped) return it.left() }
        } else {
            NetworkOperation.Skipped.left()
        }

        if (fromTmdb.isLeft() && fromTrakt.isLeft()) {
            return NetworkOperation.Skipped.left()
        }
        return mergePagedData(
            first = fromTmdb.getOrElse { pagedDataOf() },
            second = fromTrakt.getOrElse { pagedDataOf() },
            id = { movieId -> movieId },
            onConflict = { first, _ -> first }
        ).right()
    }
}
