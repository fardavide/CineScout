package cinescout.watchlist.data.remote

import arrow.core.Either
import cinescout.auth.domain.usecase.CallWithTraktAccount
import cinescout.model.NetworkOperation
import cinescout.screenplay.domain.model.Movie
import cinescout.screenplay.domain.model.TmdbScreenplayId
import cinescout.screenplay.domain.model.TvShow
import cinescout.watchlist.data.RemoteWatchlistDataSource
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

    override suspend fun getAllWatchlistMovieIds(): Either<NetworkOperation, List<TmdbScreenplayId.Movie>> =
        callWithTraktAccount(service::getAllWatchlistMovieIds)
            .map(screenplayIdMapper::toScreenplayIds)

    override suspend fun getAllWatchlistTvShowIds(): Either<NetworkOperation, List<TmdbScreenplayId.TvShow>> =
        callWithTraktAccount(service::getAllWatchlistTvShowIds)
            .map(screenplayIdMapper::toScreenplayIds)

    override suspend fun getWatchlistMovies(page: Int): Either<NetworkOperation, List<Movie>> =
        callWithTraktAccount { service.getWatchlistMovies(page) }
            .map(screenplayMapper::toScreenplays)

    override suspend fun getWatchlistTvShows(page: Int): Either<NetworkOperation, List<TvShow>> =
        callWithTraktAccount { service.getWatchlistTvShows(page) }
            .map(screenplayMapper::toScreenplays)
}
