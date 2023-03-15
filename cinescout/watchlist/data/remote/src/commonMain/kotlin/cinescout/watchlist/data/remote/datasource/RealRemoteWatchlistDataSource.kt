package cinescout.watchlist.data.remote.datasource

import arrow.core.Either
import cinescout.auth.domain.usecase.CallWithTraktAccount
import cinescout.model.NetworkOperation
import cinescout.screenplay.domain.model.Screenplay
import cinescout.screenplay.domain.model.ScreenplayType
import cinescout.screenplay.domain.model.TmdbScreenplayId
import cinescout.watchlist.data.datasource.RemoteWatchlistDataSource
import cinescout.watchlist.data.remote.service.TraktWatchlistService
import org.koin.core.annotation.Factory
import screenplay.data.remote.trakt.mapper.TraktScreenplayIdMapper
import screenplay.data.remote.trakt.mapper.TraktScreenplayMapper

@Factory
internal class RealRemoteWatchlistDataSource(
    private val callWithTraktAccount: CallWithTraktAccount,
    private val screenplayIdMapper: TraktScreenplayIdMapper,
    private val screenplayMapper: TraktScreenplayMapper,
    private val service: TraktWatchlistService
) : RemoteWatchlistDataSource {

    override suspend fun getAllWatchlistIds(
        type: ScreenplayType
    ): Either<NetworkOperation, List<TmdbScreenplayId>> = callWithTraktAccount {
        service.getAllWatchlistIds(type).map(screenplayIdMapper::toScreenplayIds)
    }

    override suspend fun getWatchlist(
        type: ScreenplayType,
        page: Int
    ): Either<NetworkOperation, List<Screenplay>> = callWithTraktAccount {
        service.getWatchlist(type, page).map(screenplayMapper::toScreenplays)
    }

    override suspend fun postAddToWatchlist(id: TmdbScreenplayId): Either<NetworkOperation, Unit> =
        callWithTraktAccount { service.postAddToWatchlist(id) }

    override suspend fun postRemoveFromWatchlist(id: TmdbScreenplayId): Either<NetworkOperation, Unit> =
        callWithTraktAccount { service.postRemoveFromWatchlist(id) }
}
