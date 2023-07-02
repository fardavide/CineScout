package cinescout.history.data.remote.service

import arrow.core.Either
import cinescout.error.NetworkError
import cinescout.history.data.remote.model.TraktHistoryMetadataResponse
import cinescout.network.Try
import cinescout.network.trakt.TraktClient
import cinescout.network.trakt.model.noLimit
import cinescout.network.trakt.model.toTraktTypeQueryString
import cinescout.screenplay.domain.model.ScreenplayTypeFilter
import cinescout.screenplay.domain.model.id.ScreenplayIds
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
internal class ScreenplayHistoryService(
    @Named(TraktClient) private val client: HttpClient
) {

    suspend fun postAddToHistory(request: TraktMultiRequest): Either<NetworkError, Unit> = Either.Try {
        client.post {
            url.path("sync", "history")
            setBody(request)
        }.body()
    }

    suspend fun getAllHistoryIds(
        type: ScreenplayTypeFilter
    ): Either<NetworkError, TraktHistoryMetadataResponse> = Either.Try {
        client.get {
            url {
                path("sync", "history", type.toTraktTypeQueryString())
                noLimit()
            }
        }.body()
    }

    suspend fun getHistoryIds(
        screenplayIds: ScreenplayIds
    ): Either<NetworkError, TraktHistoryMetadataResponse> = Either.Try {
        client.get {
            url {
                path("sync", "history", screenplayIds.toTraktTypeQueryString(), screenplayIds.trakt.value.toString())
                noLimit()
            }
        }.body()
    }

    suspend fun postRemoveFromHistory(request: TraktMultiRequest): Either<NetworkError, Unit> = Either.Try {
        client.post {
            url.path("sync", "history", "remove")
            setBody(request)
        }.body()
    }
}
