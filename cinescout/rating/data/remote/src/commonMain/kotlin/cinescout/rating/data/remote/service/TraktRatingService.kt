package cinescout.rating.data.remote.service

import arrow.core.Either
import cinescout.error.NetworkError
import cinescout.network.Try
import cinescout.network.trakt.TraktNetworkQualifier
import cinescout.rating.data.remote.model.TraktMoviesRatingsMetadataResponse
import cinescout.rating.data.remote.model.TraktMultiRatingMetadataBody
import cinescout.screenplay.domain.model.ScreenplayType
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.path
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named
import screenplay.data.remote.trakt.model.TraktMultiRequest

@Factory
internal class TraktRatingService(
    @Named(TraktNetworkQualifier.Client) private val client: HttpClient
) {

    suspend fun getRatings(type: ScreenplayType): Either<NetworkError, TraktMoviesRatingsMetadataResponse> =
        Either.Try {
            client.get { url.path("sync", type.name, "ratings") }.body()
        }


    suspend fun postAddRating(request: TraktMultiRatingMetadataBody): Either<NetworkError, Unit> =
        Either.Try {
            client.post {
                url.path("sync", "ratings")
                setBody(request)
            }.body()
        }

    suspend fun postRemoveRating(request: TraktMultiRequest): Either<NetworkError, Unit> = Either.Try {
        client.post {
            url.path("sync", "ratings", "remove")
            setBody(request)
        }.body()
    }
}
