package cinescout.anticipated.data.remote.service

import arrow.core.Either
import cinescout.error.NetworkError
import cinescout.network.Try
import cinescout.network.trakt.TraktNetworkQualifier
import cinescout.network.trakt.model.TraktExtended
import cinescout.network.trakt.model.extendedParameter
import cinescout.network.trakt.model.withLimit
import cinescout.screenplay.domain.model.ScreenplayType
import cinescout.utils.kotlin.plus
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.path
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.koin.core.annotation.Factory
import screenplay.data.remote.trakt.model.TraktMoviesExtendedResponse
import screenplay.data.remote.trakt.model.TraktScreenplaysExtendedResponse
import screenplay.data.remote.trakt.model.TraktTvShowsExtendedResponse

@Factory
internal class AnticipatedService(
    @TraktNetworkQualifier.Client private val client: HttpClient
) {

    suspend fun getMostAnticipated(
        type: ScreenplayType
    ): Either<NetworkError, TraktScreenplaysExtendedResponse> = when (type) {
        ScreenplayType.All -> {
            coroutineScope {
                val movies = async { getMostAnticipatedMovies() }
                val tvShows = async { getMostAnticipatedTvShows() }

                movies.await() + tvShows.await()
            }
        }
        ScreenplayType.Movies -> getMostAnticipatedMovies()
        ScreenplayType.TvShows -> getMostAnticipatedTvShows()
    }

    private suspend fun getMostAnticipatedMovies(): Either<NetworkError, TraktMoviesExtendedResponse> =
        Either.Try {
            client.get {
                url {
                    path("movies", "anticipated")
                    withLimit(Limit)
                    extendedParameter(TraktExtended.Full)
                }
            }.body()
        }

    private suspend fun getMostAnticipatedTvShows(): Either<NetworkError, TraktTvShowsExtendedResponse> =
        Either.Try {
            client.get {
                url {
                    path("shows", "anticipated")
                    withLimit(Limit)
                    extendedParameter(TraktExtended.Full)
                }
            }.body()
        }

    companion object {

        private const val Limit = 20
    }
}
