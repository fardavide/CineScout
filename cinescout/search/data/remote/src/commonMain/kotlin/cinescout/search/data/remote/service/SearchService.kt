package cinescout.search.data.remote.service

import arrow.core.Either
import cinescout.error.NetworkError
import cinescout.network.Try
import cinescout.network.trakt.TraktClient
import cinescout.network.trakt.model.TraktExtended
import cinescout.network.trakt.model.extendedParameter
import cinescout.network.trakt.model.withPaging
import cinescout.search.data.remote.model.TraktMovieSearchExtendedResponse
import cinescout.search.data.remote.model.TraktTvShowSearchExtendedResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.path
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named

@Factory
internal class SearchService(
    @Named(TraktClient) private val client: HttpClient
) {

    suspend fun searchMovie(
        query: String,
        page: Int
    ): Either<NetworkError, TraktMovieSearchExtendedResponse> = Either.Try {
        client.get {
            url.path("search", "movie")
            parameter("query", query)
            withPaging(page)
            extendedParameter(TraktExtended.Full)
        }.body()
    }

    suspend fun searchTvShow(
        query: String,
        page: Int
    ): Either<NetworkError, TraktTvShowSearchExtendedResponse> = Either.Try {
        client.get {
            url.path("search", "show")
            parameter("query", query)
            withPaging(page)
            extendedParameter(TraktExtended.Full)
        }.body()
    }
}
