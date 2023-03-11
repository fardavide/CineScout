package cinescout.watchlist.data

import arrow.core.Either
import cinescout.model.NetworkOperation
import cinescout.screenplay.domain.model.TmdbScreenplayId

interface RemoteWatchlistDataSource {

    suspend fun getAllWatchlistMovies(): Either<NetworkOperation, List<TmdbScreenplayId.Movie>>

    suspend fun getAllWatchlistTvShows(): Either<NetworkOperation, List<TmdbScreenplayId.TvShow>>

    suspend fun getWatchlistMovies(page: Int): Either<NetworkOperation, List<TmdbScreenplayId.Movie>>

    suspend fun getWatchlistTvShows(page: Int): Either<NetworkOperation, List<TmdbScreenplayId.TvShow>>
}

