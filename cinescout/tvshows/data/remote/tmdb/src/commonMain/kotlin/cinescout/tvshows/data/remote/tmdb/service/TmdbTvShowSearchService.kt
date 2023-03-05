package cinescout.tvshows.data.remote.tmdb.service

import arrow.core.Either
import cinescout.error.NetworkError
import cinescout.network.Try
import cinescout.network.tmdb.TmdbNetworkQualifier
import cinescout.tvshows.data.remote.tmdb.model.SearchTvShow
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.path
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named

@Factory
internal class TmdbTvShowSearchService(
    @Named(TmdbNetworkQualifier.Client) private val client: HttpClient
) {

    suspend fun searchTvShow(query: String, page: Int): Either<NetworkError, SearchTvShow.Response> =
        Either.Try {
            client.get {
                url.path("search", "tv")
                parameter("include_adult", true)
                parameter("page", page)
                parameter("query", query)
            }.body()
        }
}
