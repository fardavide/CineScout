package cinescout.movies.data.remote.tmdb.service

import arrow.core.Either
import arrow.core.left
import cinescout.error.NetworkError
import cinescout.movies.data.remote.model.TmdbMovie
import cinescout.movies.data.remote.tmdb.model.GetRatedMovies
import cinescout.movies.data.remote.tmdb.model.PostRating
import cinescout.movies.domain.model.TmdbMovieId
import cinescout.network.Try
import cinescout.network.tmdb.TmdbAuthProvider
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.path

internal class TmdbMovieService(
    private val authProvider: TmdbAuthProvider,
    private val client: HttpClient
) {

    suspend fun getMovie(id: TmdbMovieId): Either<NetworkError, TmdbMovie> =
        Either.Try {
            client.get { url.path("movie", id.value.toString()) }.body()
        }

    suspend fun getRatedMovies(): Either<NetworkError, GetRatedMovies.Response> {
        val accountId = authProvider.accountId()
            ?: return NetworkError.Unauthorized.left()
        return Either.Try {
            client.get { url.path("account", accountId, "rated", "movies") }.body()
        }
    }

    suspend fun postRating(id: TmdbMovieId, rating: PostRating.Request): Either<NetworkError, Unit> =
        Either.Try {
            client.post {
                url.path("movie", id.value.toString(), "rating")
                setBody(rating)
            }.body()
        }
}
