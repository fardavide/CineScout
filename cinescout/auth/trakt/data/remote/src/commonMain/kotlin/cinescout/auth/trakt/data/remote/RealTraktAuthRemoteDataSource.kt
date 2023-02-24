package cinescout.auth.trakt.data.remote

import arrow.core.Either
import cinescout.auth.trakt.data.TraktAuthRemoteDataSource
import cinescout.auth.trakt.data.model.TraktAccessAndRefreshTokens
import cinescout.auth.trakt.data.model.TraktAccessToken
import cinescout.auth.trakt.data.model.TraktRefreshToken
import cinescout.auth.trakt.data.remote.service.TraktAuthService
import cinescout.auth.trakt.domain.model.TraktAuthorizationCode
import cinescout.error.NetworkError
import org.koin.core.annotation.Factory

@Factory
internal class RealTraktAuthRemoteDataSource(
    private val authService: TraktAuthService
) : TraktAuthRemoteDataSource {

    override suspend fun createAccessToken(
        authorizationCode: TraktAuthorizationCode
    ): Either<NetworkError, TraktAccessAndRefreshTokens> =
        authService.createAccessToken(authorizationCode).map { response ->
            TraktAccessAndRefreshTokens(
                TraktAccessToken(response.accessToken),
                TraktRefreshToken(response.refreshToken)
            )
        }

    override fun getAppAuthorizationUrl() = TraktAuthorizeAppUrl
}
