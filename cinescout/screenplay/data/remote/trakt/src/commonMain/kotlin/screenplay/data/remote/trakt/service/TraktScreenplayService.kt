package screenplay.data.remote.trakt.service

import arrow.core.Either
import cinescout.error.NetworkError
import cinescout.network.Try
import cinescout.network.trakt.TraktNetworkQualifier
import cinescout.network.trakt.model.TraktExtended
import cinescout.network.trakt.model.extendedParameter
import cinescout.network.trakt.model.toTraktQueryString
import cinescout.network.trakt.model.withPaging
import cinescout.screenplay.domain.model.TraktScreenplayId
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.path
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named
import screenplay.data.remote.trakt.model.TraktMovieExtendedBody
import screenplay.data.remote.trakt.model.TraktMoviesExtendedResponse
import screenplay.data.remote.trakt.model.TraktScreenplayExtendedBody
import screenplay.data.remote.trakt.model.TraktScreenplaysExtendedResponse
import screenplay.data.remote.trakt.model.TraktTvShowExtendedBody
import screenplay.data.remote.trakt.model.TraktTvShowsExtendedResponse


interface TraktScreenplayService {

    suspend fun getScreenplay(id: TraktScreenplayId): Either<NetworkError, TraktScreenplayExtendedBody>
    suspend fun getSimilar(
        screenplayId: TraktScreenplayId,
        page: Int
    ): Either<NetworkError, TraktScreenplaysExtendedResponse>
}

@Factory
internal class RealTraktScreenplayService(
    @Named(TraktNetworkQualifier.Client) private val client: HttpClient
) : TraktScreenplayService {

    override suspend fun getScreenplay(
        id: TraktScreenplayId
    ): Either<NetworkError, TraktScreenplayExtendedBody> = when (id) {
        is TraktScreenplayId.Movie -> getMovie(id)
        is TraktScreenplayId.TvShow -> getTvShow(id)
    }

    override suspend fun getSimilar(
        screenplayId: TraktScreenplayId,
        page: Int
    ): Either<NetworkError, TraktScreenplaysExtendedResponse> = when (screenplayId) {
        is TraktScreenplayId.Movie -> getSimilarMovies(screenplayId, page)
        is TraktScreenplayId.TvShow -> getSimilarTvShows(screenplayId, page)
    }
    
    private suspend fun getMovie(id: TraktScreenplayId.Movie): Either<NetworkError, TraktMovieExtendedBody> =
        Either.Try {
            client.get {
                url {
                    path(id.toTraktQueryString(), id.value.toString())
                    extendedParameter(TraktExtended.Full)
                }
            }.body()
        }
    
    private suspend fun getTvShow(
        id: TraktScreenplayId.TvShow
    ): Either<NetworkError, TraktTvShowExtendedBody> = Either.Try {
        client.get {
            url {
                path(id.toTraktQueryString(), id.value.toString())
                extendedParameter(TraktExtended.Full)
            }
        }.body()
    }
    
    private suspend fun getSimilarMovies(
        screenplayId: TraktScreenplayId.Movie,
        page: Int
    ): Either<NetworkError, TraktMoviesExtendedResponse> = Either.Try {
        client.get {
            url {
                path(screenplayId.toTraktQueryString(), screenplayId.value.toString(), "related")
                extendedParameter(TraktExtended.Full)
                withPaging(page)
            }
        }.body()
    }
    
    private suspend fun getSimilarTvShows(
        screenplayId: TraktScreenplayId.TvShow,
        page: Int
    ): Either<NetworkError, TraktTvShowsExtendedResponse> = Either.Try {
        client.get {
            url {
                path(screenplayId.toTraktQueryString(), screenplayId.value.toString(), "related")
                extendedParameter(TraktExtended.Full)
                withPaging(page)
            }
        }.body()
    }
}
