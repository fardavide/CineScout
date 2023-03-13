package cinescout.watchlist.data.remote

import arrow.core.Either
import cinescout.error.NetworkError
import cinescout.network.Try
import cinescout.network.trakt.TraktNetworkQualifier
import cinescout.network.trakt.model.TraktMovieExtended
import cinescout.network.trakt.model.TraktTvShowExtended
import cinescout.network.trakt.model.movieExtended
import cinescout.network.trakt.model.tvShowExtended
import cinescout.screenplay.domain.model.TmdbScreenplayId
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.path
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named
import screenplay.data.remote.trakt.model.TraktMoviesExtendedResponse
import screenplay.data.remote.trakt.model.TraktMoviesMetadataResponse
import screenplay.data.remote.trakt.model.TraktMultiRequest
import screenplay.data.remote.trakt.model.TraktTvShowsExtendedResponse
import screenplay.data.remote.trakt.model.TraktTvShowsMetadataResponse

@Factory
internal class TraktWatchlistService(
    @Named(TraktNetworkQualifier.Client) private val client: HttpClient
) {

    suspend fun getAllWatchlistMovieIds(): Either<NetworkError, TraktMoviesMetadataResponse> = Either.Try {
        client.get { url { path("sync", "watchlist", "movies") } }.body()
    }

    suspend fun getAllWatchlistTvShowIds(): Either<NetworkError, TraktTvShowsMetadataResponse> = Either.Try {
        client.get { url { path("sync", "watchlist", "shows") } }.body()
    }
    
    suspend fun getWatchlistMovies(page: Int): Either<NetworkError, TraktMoviesExtendedResponse> =
        Either.Try {
            client.get {
                url {
                    path("sync", "watchlist", "movies")
                    parameter("page", page)
                    movieExtended(TraktMovieExtended.Full)
                }
            }.body()
        }

    suspend fun getWatchlistTvShows(page: Int): Either<NetworkError, TraktTvShowsExtendedResponse> =
        Either.Try {
            client.get {
                url {
                    path("sync", "watchlist", "shows")
                    parameter("page", page)
                    tvShowExtended(TraktTvShowExtended.Full)
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
