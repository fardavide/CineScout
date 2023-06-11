package cinescout.rating.data.remote.service

import arrow.core.Either
import cinescout.error.NetworkError
import cinescout.network.Try
import cinescout.network.trakt.TraktClient
import cinescout.network.trakt.model.TraktExtended
import cinescout.network.trakt.model.extendedParameter
import cinescout.network.trakt.model.noLimit
import cinescout.network.trakt.model.toTraktTypeQueryString
import cinescout.network.trakt.model.withPaging
import cinescout.rating.data.remote.model.OptTraktMultiRatingIdsBody
import cinescout.rating.data.remote.model.TraktScreenplaysRatingsExtendedResponse
import cinescout.rating.data.remote.model.TraktScreenplaysRatingsMetadataResponse
import cinescout.screenplay.domain.model.ScreenplayTypeFilter
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
    @Named(TraktClient) private val client: HttpClient
) {

    suspend fun getAllRatingIds(
        type: ScreenplayTypeFilter
    ): Either<NetworkError, TraktScreenplaysRatingsMetadataResponse> = Either.Try {
        client.get {
            url {
                path("sync", "ratings", type.toTraktTypeQueryString())
                noLimit()
            }
        }.body()
    }

    suspend fun getRatings(
        type: ScreenplayTypeFilter,
        page: Int
    ): Either<NetworkError, TraktScreenplaysRatingsExtendedResponse> = Either.Try {
        client.get {
            url {
                path("sync", "ratings", type.toTraktTypeQueryString())
                withPaging(page)
                extendedParameter(TraktExtended.Full)
            }
        }.body()
    }

    suspend fun postAddRating(request: OptTraktMultiRatingIdsBody): Either<NetworkError, Unit> = Either.Try {
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
