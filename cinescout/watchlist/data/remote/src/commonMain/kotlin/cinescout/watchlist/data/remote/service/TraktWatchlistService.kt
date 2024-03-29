package cinescout.watchlist.data.remote.service

import arrow.core.Either
import cinescout.error.NetworkError
import cinescout.network.Try
import cinescout.network.trakt.TraktClient
import cinescout.network.trakt.model.TraktExtended
import cinescout.network.trakt.model.extendedParameter
import cinescout.network.trakt.model.noLimit
import cinescout.network.trakt.model.toTraktTypeQueryString
import cinescout.network.trakt.model.withPaging
import cinescout.screenplay.domain.model.ScreenplayTypeFilter
import cinescout.screenplay.domain.model.id.TmdbScreenplayId
import cinescout.watchlist.data.remote.model.TraktScreenplaysWatchlistExtendedResponse
import cinescout.watchlist.data.remote.model.TraktScreenplaysWatchlistMetadataResponse
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
internal class TraktWatchlistService(
    @Named(TraktClient) private val client: HttpClient
) {

    suspend fun getAllWatchlistIds(
        type: ScreenplayTypeFilter
    ): Either<NetworkError, TraktScreenplaysWatchlistMetadataResponse> = Either.Try {
        client.get {
            url {
                path("sync", "watchlist", type.toTraktTypeQueryString())
                noLimit()
            }
        }.body()
    }

    suspend fun getAllWatchlist(
        type: ScreenplayTypeFilter
    ): Either<NetworkError, TraktScreenplaysWatchlistExtendedResponse> = Either.Try {
        client.get {
            url {
                path("sync", "watchlist", type.toTraktTypeQueryString())
                noLimit()
                extendedParameter(TraktExtended.Full)
            }
        }.body()
    }

    suspend fun getWatchlist(
        type: ScreenplayTypeFilter,
        page: Int
    ): Either<NetworkError, TraktScreenplaysWatchlistExtendedResponse> = Either.Try {
        client.get {
            url {
                path("sync", "watchlist", type.toTraktTypeQueryString())
                withPaging(page)
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
