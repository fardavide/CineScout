package cinescout.movies.data.remote.tmdb.service

import arrow.core.Either
import cinescout.error.NetworkError
import cinescout.movies.data.remote.tmdb.model.SearchMovie
import cinescout.network.Try
import cinescout.network.tmdb.TmdbNetworkQualifier
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.path
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named

@Factory
internal class TmdbMovieSearchService(
    @Named(TmdbNetworkQualifier.V3.Client) private val client: HttpClient
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
