package cinescout.watchlist.data.remote.service

import arrow.core.Either
import cinescout.error.NetworkError
import cinescout.network.Try
import cinescout.network.trakt.TraktNetworkQualifier
import cinescout.network.trakt.model.TraktExtended
import cinescout.network.trakt.model.extendedParameter
import cinescout.screenplay.domain.model.ScreenplayType
import cinescout.screenplay.domain.model.TmdbScreenplayId
import cinescout.watchlist.data.remote.model.TraktScreenplaysWatchlistExtendedResponse
import cinescout.watchlist.data.remote.model.TraktScreenplaysWatchlistMetadataResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.path
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named
import screenplay.data.remote.trakt.model.TraktMultiRequest

@Factory
internal class TraktWatchlistService(
    @Named(TraktNetworkQualifier.Client) private val client: HttpClient
) {

    suspend fun getAllWatchlistIds(
        type: ScreenplayType
    ): Either<NetworkError, TraktScreenplaysWatchlistMetadataResponse> = Either.Try {
        client.get { url { path("sync", "watchlist", type.name) } }.body()
    }

    suspend fun getWatchlist(
        type: ScreenplayType,
        page: Int
    ): Either<NetworkError, TraktScreenplaysWatchlistExtendedResponse> = Either.Try {
        client.get {
            url {
                path("sync", "watchlist", type.name)
                parameter("page", page)
                extendedParameter(TraktExtended.Full)
            }
        }.body()
    }

    suspend fun postAddToWatchlist(screenplayId: TmdbScreenplayId): Either<NetworkError, Unit> = Either.Try {
        client.post {
            url { path("sync", "watchlist") }
            setBody(TraktMultiRequest.Single(screenplayId))
        }.body()
    }
    
    suspend fun postRemoveFromWatchlist(screenplayId: TmdbScreenplayId): Either<NetworkError, Unit> =
        Either.Try {
            client.post {
                url { path("sync", "watchlist", "remove") }
                setBody(TraktMultiRequest.Single(screenplayId))
            }.body()
        }
}
