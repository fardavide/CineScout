package screenplay.data.remote.trakt.service

import arrow.core.Either
import cinescout.error.NetworkError
import cinescout.network.Try
import cinescout.network.trakt.TraktNetworkQualifier
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.path
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named
import screenplay.data.remote.trakt.model.TraktMoviesMetadataResponse
import screenplay.data.remote.trakt.model.TraktTvShowsMetadataResponse

interface TraktRecommendationService {

    suspend fun getRecommendedMovies(): Either<NetworkError, TraktMoviesMetadataResponse>

    suspend fun getRecommendedTvShows(): Either<NetworkError, TraktTvShowsMetadataResponse>
}

@Factory
internal class RealTraktRecommendationService(
    @Named(TraktNetworkQualifier.Client) private val client: HttpClient
) : TraktRecommendationService {

    override suspend fun getRecommendedMovies(): Either<NetworkError, TraktMoviesMetadataResponse> =
        Either.Try {
            client.get {
                url {
                    path("recommendations", "movies")
                    parameter("ignore_collected", true)
                    parameter("ignore_watchlisted", true)
                }
            }.body()
        }

    override suspend fun getRecommendedTvShows(): Either<NetworkError, TraktTvShowsMetadataResponse> =
        Either.Try {
            client.get {
                url {
                    path("recommendations", "shows")
                    parameter("ignore_collected", true)
                    parameter("ignore_watchlisted", true)
                }
            }.body()
        }
}
