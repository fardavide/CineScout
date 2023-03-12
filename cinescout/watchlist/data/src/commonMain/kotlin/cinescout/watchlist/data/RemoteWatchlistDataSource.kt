package cinescout.watchlist.data

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import cinescout.error.NetworkError
import cinescout.model.NetworkOperation
import cinescout.screenplay.domain.model.Movie
import cinescout.screenplay.domain.model.TmdbScreenplayId
import cinescout.screenplay.domain.model.TvShow

interface RemoteWatchlistDataSource {

    suspend fun getAllWatchlistMovieIds(): Either<NetworkOperation, List<TmdbScreenplayId.Movie>>

    suspend fun getAllWatchlistTvShowIds(): Either<NetworkOperation, List<TmdbScreenplayId.TvShow>>

    suspend fun getWatchlistMovies(page: Int): Either<NetworkOperation, List<Movie>>

    suspend fun getWatchlistTvShows(page: Int): Either<NetworkOperation, List<TvShow>>
}

class FakeRemoteWatchlistDataSource(
    private val isConnected: Boolean,
    private val movies: List<Movie>,
    private val pageSize: Int,
    private val tvShows: List<TvShow>
) : RemoteWatchlistDataSource {
    
    override suspend fun getAllWatchlistMovieIds(): Either<NetworkOperation, List<TmdbScreenplayId.Movie>> {
        TODO("Not yet implemented")
    }

    override suspend fun getAllWatchlistTvShowIds(): Either<NetworkOperation, List<TmdbScreenplayId.TvShow>> {
        TODO("Not yet implemented")
    }

    override suspend fun getWatchlistMovies(page: Int): Either<NetworkOperation, List<Movie>> =
        when (isConnected) {
            true -> movies.chunked(pageSize).getOrNull(page.index0())?.right() ?: notFound()
            false -> NetworkOperation.Skipped.left()
        }

    override suspend fun getWatchlistTvShows(page: Int): Either<NetworkOperation, List<TvShow>> =
        when (isConnected) {
            true -> tvShows.chunked(pageSize).getOrNull(page.index0())?.right() ?: notFound()
            false -> NetworkOperation.Skipped.left()
        }
    
    private fun notFound() = NetworkOperation.Error(NetworkError.NotFound).left()
    private fun Int.index0() = this - 1
}
