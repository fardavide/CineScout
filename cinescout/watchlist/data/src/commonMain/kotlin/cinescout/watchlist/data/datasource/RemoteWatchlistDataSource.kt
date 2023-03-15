package cinescout.watchlist.data.datasource

import arrow.core.Either
import arrow.core.left
import cinescout.error.NetworkError
import cinescout.model.NetworkOperation
import cinescout.screenplay.domain.model.Movie
import cinescout.screenplay.domain.model.Screenplay
import cinescout.screenplay.domain.model.ScreenplayType
import cinescout.screenplay.domain.model.TmdbScreenplayId
import cinescout.screenplay.domain.model.TvShow

interface RemoteWatchlistDataSource {

    suspend fun getAllWatchlistIds(type: ScreenplayType): Either<NetworkOperation, List<TmdbScreenplayId>>

    suspend fun getWatchlist(type: ScreenplayType, page: Int): Either<NetworkOperation, List<Screenplay>>

    suspend fun postAddToWatchlist(id: TmdbScreenplayId): Either<NetworkOperation, Unit>

    suspend fun postRemoveFromWatchlist(id: TmdbScreenplayId): Either<NetworkOperation, Unit>
}

@Suppress("unused")
class FakeRemoteWatchlistDataSource(
    private val isConnected: Boolean,
    private val movies: List<Movie>,
    private val pageSize: Int,
    private val tvShows: List<TvShow>
) : RemoteWatchlistDataSource {

    override suspend fun getAllWatchlistIds(
        type: ScreenplayType
    ): Either<NetworkOperation, List<TmdbScreenplayId>> {
        TODO("Not yet implemented")
    }

    override suspend fun getWatchlist(
        type: ScreenplayType,
        page: Int
    ): Either<NetworkOperation, List<Screenplay>> {
        TODO("Not yet implemented")
    }

    override suspend fun postAddToWatchlist(id: TmdbScreenplayId): Either<NetworkOperation, Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun postRemoveFromWatchlist(id: TmdbScreenplayId): Either<NetworkOperation, Unit> {
        TODO("Not yet implemented")
    }

    private fun notFound() = NetworkOperation.Error(NetworkError.NotFound).left()
    private fun Int.index0() = this - 1
}
