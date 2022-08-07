package cinescout.movies.data.remote.tmdb.service

import arrow.core.Either
import arrow.core.left
import cinescout.error.NetworkError
import cinescout.movies.data.remote.model.TmdbMovie
import cinescout.movies.data.remote.tmdb.model.DiscoverMovies
import cinescout.movies.data.remote.tmdb.model.GetMovieCredits
import cinescout.movies.data.remote.tmdb.model.GetRatedMovies
import cinescout.movies.data.remote.tmdb.model.PostRating
import cinescout.movies.data.remote.tmdb.model.PostWatchlist
import cinescout.movies.domain.model.DiscoverMoviesParams
import cinescout.movies.domain.model.TmdbMovieId
import cinescout.network.Try
import cinescout.network.tmdb.TmdbAuthProvider
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.path

internal class TmdbMovieService(
    private val authProvider: TmdbAuthProvider,
    private val client: HttpClient
) {

    suspend fun discoverMovies(params: DiscoverMoviesParams): Either<NetworkError, DiscoverMovies.Response> =
        Either.Try {
            client.get {
                url {
                    path("discover", "movie")
                    parameter("primary_release_year", params.releaseYear.value)
                }
            }.body()
        }

    suspend fun getMovie(id: TmdbMovieId): Either<NetworkError, TmdbMovie> =
        Either.Try {
            client.get { url.path("movie", id.value.toString()) }.body()
        }

    suspend fun getMovieCredits(movieId: TmdbMovieId): Either<NetworkError, GetMovieCredits.Response> =
        Either.Try { client.get { url.path("movie", movieId.value.toString(), "credits") }.body() }

    suspend fun getRatedMovies(page: Int): Either<NetworkError, GetRatedMovies.Response> {
        val accountId = authProvider.accountId()
            ?: return NetworkError.Unauthorized.left()
        return Either.Try {
            client.get {
                url { path("account", accountId, "rated", "movies") }
                parameter("page", page)
            }.body()
        }
    }

    suspend fun postRating(id: TmdbMovieId, rating: PostRating.Request): Either<NetworkError, Unit> =
        Either.Try {
            client.post {
                url.path("movie", id.value.toString(), "rating")
                setBody(rating)
            }.body()
        }

    suspend fun postToWatchlist(id: TmdbMovieId, shouldBeInWatchlist: Boolean): Either<NetworkError, Unit> {
        val accountId = authProvider.accountId()
            ?: return NetworkError.Unauthorized.left()
        val request = PostWatchlist.Request(
            mediaId = "${id.value}",
            mediaType = "movie",
            shouldBeInWatchlist = shouldBeInWatchlist
        )
        return Either.Try {
            client.post {
                url.path("account", accountId, "watchlist")
                setBody(request)
            }.body()
        }
    }
}
