package cinescout.tvshows.data.remote.tmdb.service

import arrow.core.Either
import arrow.core.left
import cinescout.error.NetworkError
import cinescout.network.Try
import cinescout.network.tmdb.TmdbAuthProvider
import cinescout.tvshows.data.remote.tmdb.model.GetRatedTvShows
import cinescout.tvshows.data.remote.tmdb.model.GetTvShowCredits
import cinescout.tvshows.data.remote.tmdb.model.GetTvShowDetails
import cinescout.tvshows.data.remote.tmdb.model.GetTvShowImages
import cinescout.tvshows.data.remote.tmdb.model.GetTvShowKeywords
import cinescout.tvshows.data.remote.tmdb.model.GetTvShowRecommendations
import cinescout.tvshows.data.remote.tmdb.model.GetTvShowVideos
import cinescout.tvshows.data.remote.tmdb.model.GetTvShowWatchlist
import cinescout.tvshows.data.remote.tmdb.model.PostRating
import cinescout.tvshows.data.remote.tmdb.model.PostWatchlist
import cinescout.tvshows.domain.model.TmdbTvShowId
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.path

internal class TmdbTvShowService(
    private val authProvider: TmdbAuthProvider,
    private val v3client: HttpClient
) {

    suspend fun getRatedTvShows(page: Int): Either<NetworkError, GetRatedTvShows.Response> {
        val accountId = authProvider.accountId()
            ?: return NetworkError.Unauthorized.left()
        return Either.Try {
            v3client.get {
                url.path("account", accountId, "rated", "tv")
                parameter("page", page)
            }.body()
        }
    }

    suspend fun getRecommendationsFor(
        movieId: TmdbTvShowId,
        page: Int
    ): Either<NetworkError, GetTvShowRecommendations.Response> =
        Either.Try {
            v3client.get {
                url.path("tv", movieId.value.toString(), "recommendations")
                parameter("page", page)
            }.body()
        }

    suspend fun getTvShowCredits(tvShowId: TmdbTvShowId): Either<NetworkError, GetTvShowCredits.Response> =
        Either.Try { v3client.get { url.path("tv", tvShowId.value.toString(), "credits") }.body() }

    suspend fun getTvShowDetails(tvShowId: TmdbTvShowId): Either<NetworkError, GetTvShowDetails.Response> =
        Either.Try {
            v3client.get { url.path("tv", tvShowId.value.toString()) }.body()
        }

    suspend fun getTvShowImages(tvShowId: TmdbTvShowId): Either<NetworkError, GetTvShowImages.Response> =
        Either.Try { v3client.get { url.path("tv", tvShowId.value.toString(), "images") }.body() }
    
    suspend fun getTvShowKeywords(tvShowId: TmdbTvShowId): Either<NetworkError, GetTvShowKeywords.Response> =
        Either.Try { v3client.get { url.path("tv", tvShowId.value.toString(), "keywords") }.body() }
    
    suspend fun getTvShowVideos(tvShowId: TmdbTvShowId): Either<NetworkError, GetTvShowVideos.Response> =
        Either.Try { v3client.get { url.path("tv", tvShowId.value.toString(), "videos") }.body() }

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

    suspend fun postRating(id: TmdbTvShowId, rating: PostRating.Request): Either<NetworkError, Unit> =
        Either.Try {
            v3client.post {
                url.path("tv", id.value.toString(), "rating")
                setBody(rating)
            }.body()
        }

    suspend fun postToWatchlist(id: TmdbTvShowId, shouldBeInWatchlist: Boolean): Either<NetworkError, Unit> {
        val accountId = authProvider.accountId()
            ?: return NetworkError.Unauthorized.left()
        val request = PostWatchlist.Request(
            mediaId = "${id.value}",
            mediaType = "tv",
            shouldBeInWatchlist = shouldBeInWatchlist
        )
        return Either.Try {
            v3client.post {
                url.path("account", accountId, "watchlist")
                setBody(request)
            }.body()
        }
    }
}
