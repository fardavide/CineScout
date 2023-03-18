package cinescout.rating.data.remote.service

import arrow.core.Either
import cinescout.error.NetworkError
import cinescout.network.Try
import cinescout.network.trakt.TraktNetworkQualifier
import cinescout.network.trakt.model.TraktExtended
import cinescout.network.trakt.model.extendedParameter
import cinescout.network.trakt.model.toTraktQueryString
import cinescout.network.trakt.model.withPaging
import cinescout.rating.data.remote.model.TraktMultiRatingIdsBody
import cinescout.rating.data.remote.model.TraktScreenplaysRatingsExtendedResponse
import cinescout.rating.data.remote.model.TraktScreenplaysRatingsMetadataResponse
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

    suspend fun getAllRatingIds(
        type: ScreenplayType
    ): Either<NetworkError, TraktScreenplaysRatingsMetadataResponse> = Either.Try {
        client.get { url.path("sync", "ratings", type.toTraktQueryString()) }.body()
    }

    suspend fun getRatings(
        type: ScreenplayType,
        page: Int
    ): Either<NetworkError, TraktScreenplaysRatingsExtendedResponse> = Either.Try {
        client.get {
            url {
                path("sync", "ratings", type.toTraktQueryString())
                withPaging(page)
                extendedParameter(TraktExtended.Full)
            }
        }.body()
    }

    suspend fun postAddRating(request: TraktMultiRatingIdsBody): Either<NetworkError, Unit> = Either.Try {
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
