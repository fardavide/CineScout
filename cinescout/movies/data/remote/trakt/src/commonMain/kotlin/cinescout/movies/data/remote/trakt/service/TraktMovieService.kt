package cinescout.movies.data.remote.trakt.service

import arrow.core.Either
import cinescout.error.NetworkError
import cinescout.movies.data.remote.trakt.model.GetRatings
import cinescout.movies.data.remote.trakt.model.GetWatchlist
import cinescout.movies.data.remote.trakt.model.PostAddToWatchlist
import cinescout.movies.data.remote.trakt.model.PostRating
import cinescout.movies.data.remote.trakt.model.PostRemoveFromWatchlist
import cinescout.movies.domain.model.Rating
import cinescout.movies.domain.model.TmdbMovieId
import cinescout.network.Try
import cinescout.network.trakt.getPaging
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.path
import store.PagedData
import store.Paging
import kotlin.math.roundToInt

internal class TraktMovieService(
    private val client: HttpClient
) {

    suspend fun getRatedMovies(
        page: Int
    ): Either<NetworkError, PagedData.Remote<GetRatings.Result.Movie, Paging.Page.SingleSource>> =
        Either.Try {
            val response = client.get {
                url { path("sync", "ratings", "movies") }
                parameter("page", page)
            }
            PagedData.Remote(data = response.body(), paging = response.headers.getPaging())
        }

    suspend fun getWatchlistMovies(
        page: Int
    ): Either<NetworkError, PagedData.Remote<GetWatchlist.Result.Movie, Paging.Page.SingleSource>> =
        Either.Try {
            val response = client.get {
                url { path("sync", "watchlist", "movies") }
                parameter("page", page)
            }
            PagedData.Remote(data = response.body(), paging = response.headers.getPaging())
        }

    suspend fun postAddToWatchlist(movieId: TmdbMovieId): Either<NetworkError, Unit> {
        val movie = PostAddToWatchlist.Request.Movie(
            ids = PostAddToWatchlist.Request.Movie.Ids(tmdb = movieId.value.toString())
        )
        val request = PostAddToWatchlist.Request(movies = listOf(movie))
        return Either.Try {
            client.post {
                url { path("sync", "watchlist") }
                setBody(request)
            }.body()
        }
    }

    suspend fun postRating(movieId: TmdbMovieId, rating: Rating): Either<NetworkError, Unit> {
        val movie = PostRating.Request.Movie(
            ids = PostRating.Request.Movie.Ids(tmdb = movieId.value.toString()),
            rating = rating.value.roundToInt()
        )
        val request = PostRating.Request(movies = listOf(movie))
        return Either.Try {
            client.post {
                url.path("sync", "ratings")
                setBody(request)
            }.body()
        }
    }

    suspend fun postRemoveFromWatchlist(movieId: TmdbMovieId): Either<NetworkError, Unit> {
        val movie = PostRemoveFromWatchlist.Request.Movie(
            ids = PostRemoveFromWatchlist.Request.Movie.Ids(tmdb = movieId.value.toString())
        )
        val request = PostRemoveFromWatchlist.Request(movies = listOf(movie))
        return Either.Try {
            client.post {
                url { path("sync", "watchlist") }
                setBody(request)
            }.body()
        }
    }
}
