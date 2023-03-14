package cinescout.people.data.remote.service

import arrow.core.Either
import cinescout.error.NetworkError
import cinescout.network.Try
import cinescout.network.tmdb.TmdbNetworkQualifier
import cinescout.people.data.remote.model.GetScreenplayCreditsResponse
import cinescout.screenplay.domain.model.TmdbScreenplayId
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.path
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named

@Factory
internal class TmdbPeopleService(
    @Named(TmdbNetworkQualifier.Client) private val client: HttpClient
) {
    
    suspend fun getTvShowCredits(
        tvShowId: TmdbScreenplayId.TvShow
    ): Either<NetworkError, GetScreenplayCreditsResponse> =
        Either.Try { client.get { url.path("tv", tvShowId.value.toString(), "credits") }.body() }
}
