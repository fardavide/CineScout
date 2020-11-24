package stats.remote.trakt

import entities.Either
import entities.NetworkError
import entities.movies.Movie
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.post
import network.Try
import stats.remote.trakt.model.AddToWatchlistRequest
import stats.remote.trakt.model.WatchlistResultItem

internal class SyncService(
    private val client: HttpClient
) {

    suspend fun getMoviesWatchlist(): Either<NetworkError, List<WatchlistResultItem>> = Either.Try {
        client.get(path = "sync/watchlist/movies")
    }

    suspend fun addToWatchList(movie: Movie): Either<NetworkError, Unit> = Either.Try {
        client.post {
            url.path("sync", "watchlist")
            body = AddToWatchlistRequest(movie.id)
        }
    }

    suspend fun removeFromWatchlist(movie: Movie): Either<NetworkError, Unit> = Either.Try {
        client.post {
            url.path("sync", "watchlist", "remove")
            body = AddToWatchlistRequest(movie.id)
        }
    }
}
