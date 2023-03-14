package cinescout.search.data.remote.service

import arrow.core.Either
import cinescout.error.NetworkError
import cinescout.network.Try
import cinescout.network.tmdb.TmdbNetworkQualifier
import cinescout.search.data.remote.model.SearchMovieResponse
import cinescout.search.data.remote.model.SearchTvShowResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.path
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named

@Factory
internal class SearchService(
    @Named(TmdbNetworkQualifier.Client) private val client: HttpClient
) {

    suspend fun searchMovie(query: String, page: Int): Either<NetworkError, SearchMovieResponse> =
        Either.Try {
            client.get {
                url.path("search", "movie")
                parameter("include_adult", true)
                parameter("page", page)
                parameter("query", query)
            }.body()
        }

    suspend fun searchTvShow(query: String, page: Int): Either<NetworkError, SearchTvShowResponse> =
        Either.Try {
            client.get {
                url.path("search", "tv")
                parameter("include_adult", true)
                parameter("page", page)
                parameter("query", query)
            }.body()
        }
}
