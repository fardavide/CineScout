package cinescout.auth.trakt.data

import arrow.core.Either
import cinescout.auth.trakt.data.model.TraktAccessAndRefreshTokens
import cinescout.auth.trakt.data.model.TraktRefreshToken
import cinescout.auth.trakt.domain.model.TraktAuthorizationCode
import cinescout.error.NetworkError

interface TraktAuthRemoteDataSource {

    suspend fun createAccessToken(
        authorizationCode: TraktAuthorizationCode
    ): Either<NetworkError, TraktAccessAndRefreshTokens>

    fun getAppAuthorizationUrl(): String
    suspend fun createAccessToken(refreshToken: TraktRefreshToken): Either<NetworkError, TraktAccessAndRefreshTokens>
}
