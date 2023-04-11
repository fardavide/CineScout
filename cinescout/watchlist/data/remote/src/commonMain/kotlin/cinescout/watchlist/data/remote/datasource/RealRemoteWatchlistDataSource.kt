package cinescout.watchlist.data.remote.datasource

import arrow.core.Either
import cinescout.auth.domain.usecase.CallWithTraktAccount
import cinescout.lists.domain.ListSorting
import cinescout.model.NetworkOperation
import cinescout.screenplay.domain.model.Screenplay
import cinescout.screenplay.domain.model.ScreenplayType
import cinescout.screenplay.domain.model.TmdbScreenplayId
import cinescout.watchlist.data.datasource.RemoteWatchlistDataSource
import cinescout.watchlist.data.remote.mapper.TraktWatchlistMapper
import cinescout.watchlist.data.remote.service.TraktWatchlistService
import org.koin.core.annotation.Factory

@Factory
internal class RealRemoteWatchlistDataSource(
    private val callWithTraktAccount: CallWithTraktAccount,
    private val service: TraktWatchlistService,
    private val watchlistMapper: TraktWatchlistMapper
) : RemoteWatchlistDataSource {

    override suspend fun getAllWatchlistIds(
        type: ScreenplayType
    ): Either<NetworkOperation, List<TmdbScreenplayId>> = callWithTraktAccount {
        service.getAllWatchlistIds(type).map(watchlistMapper::toScreenplayIds)
    }

    override suspend fun getWatchlist(
        sorting: ListSorting,
        type: ScreenplayType,
        page: Int
    ): Either<NetworkOperation, List<Screenplay>> = callWithTraktAccount {
        service.getWatchlist(sorting, type, page).map(watchlistMapper::toScreenplays)
    }

    override suspend fun postAddToWatchlist(id: TmdbScreenplayId): Either<NetworkOperation, Unit> =
        callWithTraktAccount { service.postAddToWatchlist(id) }

    override suspend fun postRemoveFromWatchlist(id: TmdbScreenplayId): Either<NetworkOperation, Unit> =
        callWithTraktAccount { service.postRemoveFromWatchlist(id) }
}
