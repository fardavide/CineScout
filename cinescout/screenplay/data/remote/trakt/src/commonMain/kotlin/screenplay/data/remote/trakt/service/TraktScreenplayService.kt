package screenplay.data.remote.trakt.service

import arrow.core.Either
import cinescout.error.NetworkError
import cinescout.network.Try
import cinescout.network.trakt.TraktClient
import cinescout.network.trakt.model.TraktExtended
import cinescout.network.trakt.model.extendedParameter
import cinescout.network.trakt.model.toTraktTypeQueryString
import cinescout.network.trakt.model.withPaging
import cinescout.screenplay.domain.model.ids.TraktMovieId
import cinescout.screenplay.domain.model.ids.TraktScreenplayId
import cinescout.screenplay.domain.model.ids.TraktTvShowId
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
    @Named(TraktClient) private val client: HttpClient
) : TraktScreenplayService {

    override suspend fun getScreenplay(
        id: TraktScreenplayId
    ): Either<NetworkError, TraktScreenplayExtendedBody> = when (id) {
        is TraktMovieId -> getMovie(id)
        is TraktTvShowId -> getTvShow(id)
    }

    override suspend fun getSimilar(
        screenplayId: TraktScreenplayId,
        page: Int
    ): Either<NetworkError, TraktScreenplaysExtendedResponse> = when (screenplayId) {
        is TraktMovieId -> getSimilarMovies(screenplayId, page)
        is TraktTvShowId -> getSimilarTvShows(screenplayId, page)
    }

    private suspend fun getMovie(id: TraktMovieId): Either<NetworkError, TraktMovieExtendedBody> =
        Either.Try {
            client.get {
                url {
                    path(id.toTraktTypeQueryString(), id.value.toString())
                    extendedParameter(TraktExtended.Full)
                }
            }.body()
        }

    private suspend fun getTvShow(id: TraktTvShowId): Either<NetworkError, TraktTvShowExtendedBody> =
        Either.Try {
            client.get {
                url {
                    path(id.toTraktTypeQueryString(), id.value.toString())
                    extendedParameter(TraktExtended.Full)
                }
            }.body()
        }

    private suspend fun getSimilarMovies(
        screenplayId: TraktMovieId,
        page: Int
    ): Either<NetworkError, TraktMoviesExtendedResponse> = Either.Try {
        client.get {
            url {
                path(screenplayId.toTraktTypeQueryString(), screenplayId.value.toString(), "related")
                extendedParameter(TraktExtended.Full)
                withPaging(page)
            }
        }.body()
    }

    private suspend fun getSimilarTvShows(
        screenplayId: TraktTvShowId,
        page: Int
    ): Either<NetworkError, TraktTvShowsExtendedResponse> = Either.Try {
        client.get {
            url {
                path(screenplayId.toTraktTypeQueryString(), screenplayId.value.toString(), "related")
                extendedParameter(TraktExtended.Full)
                withPaging(page)
            }
        }.body()
    }
}
