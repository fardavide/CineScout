package cinescout.tvshows.data.remote.trakt.service

import arrow.core.Either
import cinescout.error.NetworkError
import cinescout.network.Try
import cinescout.network.trakt.TraktNetworkQualifier
import cinescout.screenplay.domain.model.Rating
import cinescout.tvshows.data.remote.trakt.model.GetRatings
import cinescout.tvshows.data.remote.trakt.model.GetWatchlist
import cinescout.tvshows.data.remote.trakt.model.PostAddToWatchlist
import cinescout.tvshows.data.remote.trakt.model.PostRating
import cinescout.tvshows.data.remote.trakt.model.PostRemoveFromWatchlist
import cinescout.tvshows.domain.model.TmdbTvShowId
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.path
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named
import kotlin.math.roundToInt

@Factory
internal class TraktTvShowService(
    @Named(TraktNetworkQualifier.Client) private val client: HttpClient
) {

    suspend fun getRatedTvShows(): Either<NetworkError, List<GetRatings.Result.TvShow>> = Either.Try {
        client.get { url { path("sync", "ratings", "shows") } }.body()
    }

    suspend fun getWatchlistTvShows(): Either<NetworkError, List<GetWatchlist.Result.TvShow>> = Either.Try {
        client.get { url { path("sync", "watchlist", "shows") } }.body()
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
