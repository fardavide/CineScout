package cinescout.tvshows.data.remote.trakt.service

import arrow.core.Either
import cinescout.common.model.Rating
import cinescout.error.NetworkError
import cinescout.network.Try
import cinescout.network.trakt.TraktNetworkQualifier
import cinescout.network.trakt.getPaging
import cinescout.tvshows.data.remote.trakt.model.GetRatings
import cinescout.tvshows.data.remote.trakt.model.GetWatchlist
import cinescout.tvshows.data.remote.trakt.model.PostAddToWatchlist
import cinescout.tvshows.data.remote.trakt.model.PostRating
import cinescout.tvshows.data.remote.trakt.model.PostRemoveFromWatchlist
import cinescout.tvshows.domain.model.TmdbTvShowId
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named
import store.PagedData
import store.Paging
import kotlin.math.roundToInt

@Factory
internal class TraktTvShowService(
    @Named(TraktNetworkQualifier.Client) private val client: HttpClient
) {

    suspend fun getRatedTvShows(
        page: Int
    ): Either<NetworkError, PagedData.Remote<GetRatings.Result.TvShow, Paging.Page.SingleSource>> =
        Either.Try {
            val response = client.get {
                url { path("sync", "ratings", "shows") }
                parameter("page", page)
            }
            PagedData.Remote(data = response.body(), paging = response.headers.getPaging())
        }

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

    suspend fun postAddToWatchlist(tvShowId: TmdbTvShowId): Either<NetworkError, Unit> {
        val tvShow = PostAddToWatchlist.Request.TvShow(
            ids = PostAddToWatchlist.Request.TvShow.Ids(tmdb = tvShowId.value.toString())
        )
        val request = PostAddToWatchlist.Request(tvShows = listOf(tvShow))
        return Either.Try {
            client.post {
                url { path("sync", "watchlist") }
                setBody(request)
            }.body()
        }
    }

    suspend fun postRating(movieId: TmdbTvShowId, rating: Rating): Either<NetworkError, Unit> {
        val tvShow = PostRating.Request.TvShow(
            ids = PostRating.Request.TvShow.Ids(tmdb = movieId.value.toString()),
            rating = rating.value.roundToInt()
        )
        val request = PostRating.Request(tvShows = listOf(tvShow))
        return Either.Try {
            client.post {
                url.path("sync", "ratings")
                setBody(request)
            }.body()
        }
    }

    suspend fun postRemoveFromWatchlist(tvShowId: TmdbTvShowId): Either<NetworkError, Unit> {
        val tvShow = PostRemoveFromWatchlist.Request.TvShow(
            ids = PostRemoveFromWatchlist.Request.TvShow.Ids(tmdb = tvShowId.value.toString())
        )
        val request = PostRemoveFromWatchlist.Request(tvShows = listOf(tvShow))
        return Either.Try {
            client.post {
                url { path("sync", "watchlist", "remove") }
                setBody(request)
            }.body()
        }
    }
}
