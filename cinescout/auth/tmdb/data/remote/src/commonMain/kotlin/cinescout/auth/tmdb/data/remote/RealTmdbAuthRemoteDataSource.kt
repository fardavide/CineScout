package cinescout.auth.tmdb.data.remote

import arrow.core.Either
import cinescout.auth.tmdb.data.TmdbAuthRemoteDataSource
import cinescout.auth.tmdb.data.model.TmdbAccessToken
import cinescout.auth.tmdb.data.model.TmdbAccessTokenAndAccountId
import cinescout.auth.tmdb.data.model.TmdbAccountId
import cinescout.auth.tmdb.data.model.TmdbAuthorizedRequestToken
import cinescout.auth.tmdb.data.model.TmdbCredentials
import cinescout.auth.tmdb.data.model.TmdbRequestToken
import cinescout.auth.tmdb.data.model.TmdbSessionId
import cinescout.auth.tmdb.data.remote.service.TmdbAuthService
import cinescout.error.NetworkError

class RealTmdbAuthRemoteDataSource(
    private val authService: TmdbAuthService
) : TmdbAuthRemoteDataSource {

    override suspend fun createRequestToken(): Either<NetworkError, TmdbRequestToken> =
        authService.createRequestToken().map { response -> TmdbRequestToken(response.requestToken) }

    override suspend fun createAccessToken(
        token: TmdbAuthorizedRequestToken
    ): Either<NetworkError, TmdbAccessTokenAndAccountId> =
        authService.createAccessToken(token).map { response ->
            TmdbAccessTokenAndAccountId(
                accessToken = TmdbAccessToken(response.accessToken),
                accountId = TmdbAccountId(response.accountId)
            )
        }

    override suspend fun convertV4Session(
        accessToken: TmdbAccessToken,
        accountId: TmdbAccountId
    ): Either<NetworkError, TmdbCredentials> =
        authService.convertV4Session(accessToken).map { response ->
            TmdbCredentials(
                accountId = accountId,
                accessToken = accessToken,
                sessionId = TmdbSessionId(response.sessionId)
            )
        }
}
