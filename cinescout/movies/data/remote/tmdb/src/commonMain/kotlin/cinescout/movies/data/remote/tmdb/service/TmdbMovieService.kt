package cinescout.movies.data.remote.tmdb.service

import arrow.core.Either
import cinescout.error.NetworkError
import cinescout.movies.data.remote.tmdb.model.GetMovieCredits
import cinescout.movies.data.remote.tmdb.model.GetMovieDetails
import cinescout.movies.data.remote.tmdb.model.GetMovieImages
import cinescout.movies.data.remote.tmdb.model.GetMovieKeywords
import cinescout.movies.data.remote.tmdb.model.GetMovieRecommendations
import cinescout.movies.data.remote.tmdb.model.GetMovieVideos
import cinescout.movies.domain.model.TmdbMovieId
import cinescout.network.Try
import cinescout.network.tmdb.TmdbNetworkQualifier
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.path
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named

@Factory
internal class TmdbMovieService(
    @Named(TmdbNetworkQualifier.Client) private val client: HttpClient
) {

    suspend fun getMovieDetails(id: TmdbMovieId): Either<NetworkError, GetMovieDetails.Response> =
        Either.Try {
            client.get { url.path("movie", id.value.toString()) }.body()
        }

    suspend fun getMovieCredits(movieId: TmdbMovieId): Either<NetworkError, GetMovieCredits.Response> =
        Either.Try { client.get { url.path("movie", movieId.value.toString(), "credits") }.body() }

    suspend fun getMovieImages(movieId: TmdbMovieId): Either<NetworkError, GetMovieImages.Response> =
        Either.Try { client.get { url.path("movie", movieId.value.toString(), "images") }.body() }

    suspend fun getMovieKeywords(movieId: TmdbMovieId): Either<NetworkError, GetMovieKeywords.Response> =
        Either.Try { client.get { url.path("movie", movieId.value.toString(), "keywords") }.body() }

    suspend fun getMovieVideos(movieId: TmdbMovieId): Either<NetworkError, GetMovieVideos.Response> =
        Either.Try { client.get { url.path("movie", movieId.value.toString(), "videos") }.body() }

    suspend fun getRecommendationsFor(
        movieId: TmdbMovieId,
        page: Int
    ): Either<NetworkError, GetMovieRecommendations.Response> = Either.Try {
        client.get {
            url.path("movie", movieId.value.toString(), "recommendations")
            parameter("page", page)
        }.body()
    }
}
