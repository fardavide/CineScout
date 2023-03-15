package cinescout.screenplay.data.remote.tmdb.service

import arrow.core.Either
import cinescout.error.NetworkError
import cinescout.network.Try
import cinescout.network.tmdb.TmdbNetworkQualifier
import cinescout.screenplay.data.remote.tmdb.model.GetMovieRecommendationsResponse
import cinescout.screenplay.data.remote.tmdb.model.GetScreenplayKeywordsResponse
import cinescout.screenplay.data.remote.tmdb.model.GetTmdbTvShowRecommendationsResponse
import cinescout.screenplay.domain.model.TmdbScreenplayId
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.path
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named

@Factory
internal class TmdbScreenplayService(
    @Named(TmdbNetworkQualifier.Client) private val client: HttpClient
) {

    suspend fun getMovieKeywords(
        movieId: TmdbScreenplayId.Movie
    ): Either<NetworkError, GetScreenplayKeywordsResponse> =
        Either.Try { client.get { url.path("movie", movieId.value.toString(), "keywords") }.body() }

    suspend fun getMovieRecommendationsFor(
        movieId: TmdbScreenplayId.Movie,
        page: Int
    ): Either<NetworkError, GetMovieRecommendationsResponse> = Either.Try {
        client.get {
            url.path("movie", movieId.value.toString(), "recommendations")
            parameter("page", page)
        }.body()
    }

    suspend fun getTvShowKeywords(
        tvShowId: TmdbScreenplayId.TvShow
    ): Either<NetworkError, GetScreenplayKeywordsResponse> =
        Either.Try { client.get { url.path("tv", tvShowId.value.toString(), "keywords") }.body() }

    suspend fun getTvShowRecommendationsFor(
        tvShowId: TmdbScreenplayId.TvShow,
        page: Int
    ): Either<NetworkError, GetTmdbTvShowRecommendationsResponse> = Either.Try {
        client.get {
            url.path("tv", tvShowId.value.toString(), "recommendations")
            parameter("page", page)
        }.body()
    }
}
