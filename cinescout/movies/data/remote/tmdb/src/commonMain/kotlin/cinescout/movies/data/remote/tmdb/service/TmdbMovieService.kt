package cinescout.movies.data.remote.tmdb.service

import arrow.core.Either
import cinescout.error.NetworkError
import cinescout.movies.data.remote.model.TmdbMovie
import cinescout.movies.domain.model.TmdbMovieId
import cinescout.network.Try
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.path

internal class TmdbMovieService(
    private val client: HttpClient
) {

    suspend fun getMovie(id: TmdbMovieId): Either<NetworkError, TmdbMovie> =
        Either.Try {
            client.get { url.path("movie", id.value.toString()) }.body()
        }
}
