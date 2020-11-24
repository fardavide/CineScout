package stats.remote.tmdb

import entities.DefaultErrorDelay
import entities.Either
import entities.NetworkError
import entities.left
import entities.movies.Movie
import entities.right
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import movies.remote.tmdb.model.MoviePageResult
import network.Try
import stats.remote.tmdb.model.AddToWatchlistRequest
import stats.remote.tmdb.model.MediaType

internal class AccountService (
    private val client: HttpClient,
    private val v3AccountId: String,
    private val v4accountId: String
) {

    /**
     * @return [Flow] where every element contains a [MoviePageResult]
     */
    fun getPagedMoviesWatchlist(): Flow<Either<NetworkError, List<MoviePageResult>>> = flow {
        var maxPage = Int.MAX_VALUE
        val pages = mutableListOf<MoviePageResult>()
        while (pages.size < maxPage) {

            val nextPage = pages.size + 1
            when (val either = getMoviesWatchlist(nextPage)) {
                is Either.Right -> {
                    val newPage = either.rightOrThrow()
                    pages += either.rightOrThrow()
                    emit(pages.toList().right())
                    maxPage = newPage.totalPages
                }
                is Either.Left -> {
                    emit(either.leftOrThrow().left())
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
