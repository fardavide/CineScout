package stats.remote

import entities.DefaultErrorDelay
import entities.Either
import entities.NetworkError
import entities.movies.Movie
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import movies.remote.tmdb.model.MoviePageResult
import network.Try
import stats.remote.model.AddToWatchlistRequest
import stats.remote.model.MediaType

internal class AccountService (
    private val client: HttpClient,
    private val v3AccountId: String,
    private val v4accountId: String
) {

    suspend fun getMoviesWatchlist(): Either<NetworkError, MoviePageResult> = Either.Try {
        client.get {
            url.path("4", "account", v4accountId, "movie", "watchlist")
            parameter("append_to_response", "credits")
        }
    }

    fun getMoviesWatchlistX(): Flow<Either<NetworkError, MoviePageResult>> = flow {
        var page = 1
        var maxPage = Int.MAX_VALUE
        while (page <= maxPage) {

            when (val either = getMoviesWatchlist(page)) {
                is Either.Right -> {
                    emit(either)
                    page++
                    maxPage = either.rightOrThrow().totalPages
                }
                is Either.Left -> {
                    emit(either)
                    delay(DefaultErrorDelay)
                }
            }
        }
    }

    private suspend fun getMoviesWatchlist(page: Int): Either<NetworkError, MoviePageResult> = Either.Try {
        client.get {
            url.path("4", "account", v4accountId, "movie", "watchlist")
            parameter("append_to_response", "credits")
            parameter("page", page)
        }
    }

    suspend fun addToWatchList(movie: Movie): Either<NetworkError, Unit> = Either.Try {
        client.post {
            url.path("3", "account", v4accountId, "watchlist")
            body = AddToWatchlistRequest(MediaType.Movie, movie.id, AddToWatchlistRequest.Action.Add)
        }
    }

    suspend fun removeFromWatchlist(movie: Movie): Either<NetworkError, Unit> = Either.Try {
        client.post {
            url.path("3", "account", v3AccountId, "watchlist")
            body = AddToWatchlistRequest(MediaType.Movie, movie.id, AddToWatchlistRequest.Action.Remove)
        }
    }
}
