package cinescout.screenplay.data.remote.tmdb.service

import arrow.core.Either
import cinescout.error.NetworkError
import cinescout.network.Try
import cinescout.network.tmdb.TmdbNetworkQualifier
import cinescout.screenplay.data.remote.tmdb.model.GetMovieRecommendationsResponse
import cinescout.screenplay.data.remote.tmdb.model.GetMovieResponse
import cinescout.screenplay.data.remote.tmdb.model.GetScreenplayKeywordsResponse
import cinescout.screenplay.data.remote.tmdb.model.GetScreenplayKeywordsResponseWithId
import cinescout.screenplay.data.remote.tmdb.model.GetTmdbTvShowRecommendationsResponse
import cinescout.screenplay.data.remote.tmdb.model.GetTvShowResponse
import cinescout.screenplay.data.remote.tmdb.model.withId
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

    suspend fun getMovie(movieId: TmdbScreenplayId.Movie): Either<NetworkError, GetMovieResponse> =
        Either.Try {
            client.get { url.path("movie", movieId.value.toString()) }.body()
        }

    suspend fun getMovieGenres(
        movieId: TmdbScreenplayId.Movie
    ): Either<NetworkError, List<GetMovieResponse.Genre>> = getMovie(movieId).map { it.genres }

    suspend fun getMovieRecommendationsFor(
        movieId: TmdbScreenplayId.Movie,
        page: Int
    ): Either<NetworkError, GetMovieRecommendationsResponse> = Either.Try {
        client.get {
            url.path("movie", movieId.value.toString(), "recommendations")
            parameter("page", page)
        }.body()
    }

    suspend fun getScreenplayKeywords(
        screenplayId: TmdbScreenplayId
    ): Either<NetworkError, GetScreenplayKeywordsResponseWithId> = Either.Try {
        val type = when (screenplayId) {
            is TmdbScreenplayId.Movie -> "movie"
            is TmdbScreenplayId.TvShow -> "tv"
        }
        client.get { url.path(type, screenplayId.value.toString(), "keywords") }
            .body<GetScreenplayKeywordsResponse>() withId screenplayId
    }

    suspend fun getTvShow(tvShowId: TmdbScreenplayId.TvShow): Either<NetworkError, GetTvShowResponse> =
        Either.Try {
            client.get { url.path("tv", tvShowId.value.toString()) }.body()
        }

    suspend fun getTvShowGenres(
        tvShowId: TmdbScreenplayId.TvShow
    ): Either<NetworkError, List<GetTvShowResponse.Genre>> = getTvShow(tvShowId).map { it.genres }

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
