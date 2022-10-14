package cinescout.tvshows.data.remote.tmdb.service

import arrow.core.Either
import arrow.core.left
import cinescout.error.NetworkError
import cinescout.network.Try
import cinescout.network.tmdb.TmdbAuthProvider
import cinescout.tvshows.data.remote.tmdb.model.GetTvShowDetails
import cinescout.tvshows.data.remote.tmdb.model.GetTvShowWatchlist
import cinescout.tvshows.domain.model.TmdbTvShowId
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.path

internal class TmdbTvShowService(
    private val authProvider: TmdbAuthProvider,
    private val v3client: HttpClient
) {

    suspend fun getTvShowDetails(id: TmdbTvShowId): Either<NetworkError, GetTvShowDetails.Response> =
        Either.Try {
            v3client.get { url.path("tv", id.value.toString()) }.body()
        }

    suspend fun getTvShowWatchlist(page: Int): Either<NetworkError, GetTvShowWatchlist.Response> {
        val accountId = authProvider.accountId()
            ?: return NetworkError.Unauthorized.left()
        return Either.Try {
            v3client.get {
                url { path("account", accountId, "watchlist", "tv") }
                parameter("page", page)
            }.body()
        }
    }
}
