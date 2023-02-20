package cinescout.auth.trakt.data

import arrow.core.Either
import arrow.core.right
import cinescout.auth.trakt.data.model.TraktAccessAndRefreshTokens
import cinescout.auth.trakt.data.model.TraktRefreshToken
import cinescout.auth.trakt.data.sample.TraktAccessAndRefreshTokensSample
import cinescout.auth.trakt.domain.model.TraktAuthorizationCode
import cinescout.error.NetworkError

interface TraktAuthRemoteDataSource {

    suspend fun createAccessToken(
        authorizationCode: TraktAuthorizationCode
    ): Either<NetworkError, TraktAccessAndRefreshTokens>

    suspend fun createAccessToken(
        refreshToken: TraktRefreshToken
    ): Either<NetworkError, TraktAccessAndRefreshTokens>

    fun getAppAuthorizationUrl(): String
}

class FakeTraktAuthRemoteDataSource(
    private val accessAndRefreshTokens: TraktAccessAndRefreshTokens =
        TraktAccessAndRefreshTokensSample.AccessAndRefreshToken
) : TraktAuthRemoteDataSource {

    override suspend fun createAccessToken(
        authorizationCode: TraktAuthorizationCode
    ): Either<NetworkError, TraktAccessAndRefreshTokens> = accessAndRefreshTokens.right()

    override suspend fun createAccessToken(
        refreshToken: TraktRefreshToken
    ): Either<NetworkError, TraktAccessAndRefreshTokens> = accessAndRefreshTokens.right()

    override fun getAppAuthorizationUrl(): String = "https://trakt.tv/oauth/authorize"
}
