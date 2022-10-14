package cinescout.tvshows.data.remote.trakt.service

import arrow.core.Either
import cinescout.error.NetworkError
import cinescout.network.Try
import cinescout.network.trakt.getPaging
import cinescout.tvshows.data.remote.trakt.model.GetWatchlist
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.path
import store.PagedData
import store.Paging

internal class TraktTvShowService(
    private val client: HttpClient
) {

    suspend fun getWatchlistTvShows(
        page: Int
    ): Either<NetworkError, PagedData.Remote<GetWatchlist.Result.TvShow, Paging.Page.SingleSource>> =
        Either.Try {
            val response = client.get {
                url { path("sync", "watchlist", "shows") }
                parameter("page", page)
            }
            PagedData.Remote(data = response.body(), paging = response.headers.getPaging())
        }
}
