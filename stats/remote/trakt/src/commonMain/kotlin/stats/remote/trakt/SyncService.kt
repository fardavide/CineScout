package stats.remote.trakt

import entities.Either
import entities.NetworkError
import entities.movies.Movie
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.post
import kotlinx.coroutines.flow.Flow
import network.Try
import stats.remote.trakt.model.WatchlistResultItem

internal class SyncService(
    private val client: HttpClient
) {

    suspend fun getMoviesWatchlist(): Either<NetworkError, List<WatchlistResultItem>> = Either.Try {
        client.get(path = "sync/watchlist/movies")
    }

    suspend fun addToWatchList(movie: Movie): Either<NetworkError, Unit> = Either.Try {
        TODO("Not implemented")
//        client.post {
//            url.path("3", "account", v4accountId, "watchlist")
//            body = AddToWatchlistRequest(MediaType.Movie, movie.id, AddToWatchlistRequest.Action.Add)
//        }
    }

    suspend fun removeFromWatchlist(movie: Movie): Either<NetworkError, Unit> = Either.Try {
        TODO("Not implemented")
//        client.post {
//            url.path("3", "account", v3AccountId, "watchlist")
//            body = AddToWatchlistRequest(MediaType.Movie, movie.id, AddToWatchlistRequest.Action.Remove)
//        }
    }
}
