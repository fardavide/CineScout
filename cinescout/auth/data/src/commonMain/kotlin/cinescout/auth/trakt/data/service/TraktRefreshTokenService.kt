package cinescout.auth.trakt.data.service

import arrow.core.Either
import arrow.core.right
import cinescout.auth.trakt.data.model.CreateAccessToken
import cinescout.auth.trakt.data.model.TraktRefreshToken
import cinescout.auth.trakt.data.sample.CreateAccessTokenResponseSample
import cinescout.error.NetworkError

interface TraktRefreshTokenService {

    suspend fun createAccessToken(
        refreshToken: TraktRefreshToken
    ): Either<NetworkError, CreateAccessToken.Response>
}

class FakeTraktRefreshTokenService(
    private val result: Either<NetworkError, CreateAccessToken.Response> =
        CreateAccessTokenResponseSample.Refreshed.right()
) : TraktRefreshTokenService {

    override suspend fun createAccessToken(
        refreshToken: TraktRefreshToken
    ): Either<NetworkError, CreateAccessToken.Response> = result
}
