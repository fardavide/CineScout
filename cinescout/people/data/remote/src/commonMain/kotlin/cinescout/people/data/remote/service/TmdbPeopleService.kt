package cinescout.people.data.remote.service

import arrow.core.Either
import cinescout.error.NetworkError
import cinescout.network.Try
import cinescout.network.tmdb.TmdbNetworkQualifier
import cinescout.people.data.remote.model.GetScreenplayCreditsResponse
import cinescout.people.data.remote.model.GetScreenplayCreditsResponseWithId
import cinescout.people.data.remote.model.withId
import cinescout.screenplay.domain.model.ids.TmdbMovieId
import cinescout.screenplay.domain.model.ids.TmdbScreenplayId
import cinescout.screenplay.domain.model.ids.TmdbTvShowId
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

    suspend fun getScreenplayCredits(
        screenplayId: TmdbScreenplayId
    ): Either<NetworkError, GetScreenplayCreditsResponseWithId> = Either.Try {
        val type = when (screenplayId) {
            is TmdbMovieId -> "movie"
            is TmdbTvShowId -> "tv"
        }
        client.get { url.path(type, screenplayId.value.toString(), "credits") }
            .body<GetScreenplayCreditsResponse>() withId screenplayId
    }

}
