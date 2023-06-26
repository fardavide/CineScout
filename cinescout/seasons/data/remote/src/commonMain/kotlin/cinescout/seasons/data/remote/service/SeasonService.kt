package cinescout.seasons.data.remote.service

import arrow.core.Either
import cinescout.error.NetworkError
import cinescout.network.Try
import cinescout.network.trakt.TraktClient
import cinescout.network.trakt.model.TraktSeasonsExtended
import cinescout.network.trakt.model.extendedSeasonsParameter
import cinescout.network.trakt.model.toTraktIdQueryString
import cinescout.screenplay.domain.model.id.TvShowIds
import cinescout.seasons.data.remote.model.TraktSeasonsExtendedWithEpisodesResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.path
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named

@Factory
internal class SeasonService(
    @Named(TraktClient) private val client: HttpClient
) {

    suspend fun getSeasonsWithEpisodes(
        tvShowIds: TvShowIds
    ): Either<NetworkError, TraktSeasonsExtendedWithEpisodesResponse> = Either.Try {
        client.get {
            url {
                path("shows", tvShowIds.toTraktIdQueryString(), "seasons")
                extendedSeasonsParameter(TraktSeasonsExtended.FullWithEpisodes)
            }
        }.body()
    }
}
