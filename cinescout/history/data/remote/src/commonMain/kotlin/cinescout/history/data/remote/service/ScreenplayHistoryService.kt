package cinescout.history.data.remote.service

import arrow.core.Either
import cinescout.error.NetworkError
import cinescout.history.data.remote.model.TraktHistoryMetadataResponse
import cinescout.network.Try
import cinescout.network.trakt.TraktClient
import cinescout.network.trakt.model.noLimit
import cinescout.network.trakt.model.toTraktQueryString
import cinescout.network.trakt.model.withPaging
import cinescout.screenplay.domain.model.ids.ScreenplayIds
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

    suspend fun getHistoryIds(
        screenplayIds: ScreenplayIds,
        page: Int? = null
    ): Either<NetworkError, TraktHistoryMetadataResponse> = Either.Try {
        client.get {
            url {
                path("sync", "history", screenplayIds.toTraktQueryString(), screenplayIds.trakt.value.toString())
                if (page != null) withPaging(page)
                else noLimit()
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
