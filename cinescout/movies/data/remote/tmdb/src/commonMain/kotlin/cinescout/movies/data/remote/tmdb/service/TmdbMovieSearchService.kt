package cinescout.movies.data.remote.tmdb.service

import arrow.core.Either
import cinescout.error.NetworkError
import cinescout.movies.data.remote.tmdb.model.SearchMovie
import cinescout.network.Try
import cinescout.network.tmdb.TmdbAuthProvider
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.path

internal class TmdbMovieSearchService(
    private val authProvider: TmdbAuthProvider,
    private val client: HttpClient
) {

    suspend fun searchMovie(
        query: String,
        page: Int
    ): Either<NetworkError, SearchMovie.Response> =
        Either.Try {
            client.get {
                url.path("search", "movie")
                parameter("include_adult", true)
                parameter("page", page)
                parameter("query", query)
            }.body()
        }
}
