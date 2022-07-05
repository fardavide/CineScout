package cinescout.auth.tmdb.data.remote

import arrow.core.Either
import cinescout.auth.tmdb.data.TmdbAuthRemoteDataSource
import cinescout.auth.tmdb.data.model.TmdbAccessToken
import cinescout.auth.tmdb.data.model.TmdbAuthorizedRequestToken
import cinescout.auth.tmdb.data.model.TmdbRequestToken
import cinescout.auth.tmdb.data.remote.service.TmdbAuthService
import cinescout.error.NetworkError

class RealTmdbAuthRemoteDataSource(
    private val authService: TmdbAuthService
) : TmdbAuthRemoteDataSource {

    override suspend fun createRequestToken(): Either<NetworkError, TmdbRequestToken> =
        authService.createRequestToken().map { response -> TmdbRequestToken(response.requestToken) }

    override suspend fun createAccessToken(token: TmdbAuthorizedRequestToken): Either<NetworkError, TmdbAccessToken> =
        authService.createAccessToken(token).map { response -> TmdbAccessToken(response.accessToken) }
}
