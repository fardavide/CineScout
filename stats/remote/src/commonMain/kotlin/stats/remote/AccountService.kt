package stats.remote

import entities.Either
import entities.NetworkError
import entities.movies.Movie
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import movies.remote.tmdb.model.MoviePageResult
import network.Try
import stats.remote.model.AddToWatchlistRequest
import stats.remote.model.MediaType

internal class AccountService (
    private val client: HttpClient,
    private val accountId: String
) {

    suspend fun getMoviesWatchlist(): Either<NetworkError, MoviePageResult> = Either.Try {
        client.get {
            url.path("4", "account", accountId, "movie", "watchlist")
            parameter("append_to_response", "credits")
        }
    }

    suspend fun addToWatchList(movie: Movie): Either<NetworkError, Unit> = Either.Try {
        client.post {
            url.path("3", "account", accountId, "watchlist")
            body = AddToWatchlistRequest(MediaType.Movie, movie.id, AddToWatchlistRequest.Action.Add)
        }
    }

    suspend fun removeFromWatchlist(movie: Movie): Either<NetworkError, Unit> = Either.Try {
        client.post {
            url.path("3", "account", accountId, "watchlist")
            body = AddToWatchlistRequest(MediaType.Movie, movie.id, AddToWatchlistRequest.Action.Remove)
        }
    }
}
