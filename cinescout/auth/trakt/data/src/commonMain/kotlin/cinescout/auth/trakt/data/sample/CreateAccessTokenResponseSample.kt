package cinescout.auth.trakt.data.sample

import cinescout.auth.trakt.data.model.CreateAccessToken

object CreateAccessTokenResponseSample {

    val Refreshed = CreateAccessToken.Response(
        accessToken = TraktAccessTokenSample.RefreshedAccessToken.value,
        createdAt = TraktAccessTokenSample.RefreshedCreatedAt,
        expiresIn = TraktAccessTokenSample.RefreshedExpiresIn,
        refreshToken = TraktRefreshTokenSample.RefreshToken.value,
        scope = TraktAccessTokenSample.Scope,
        tokenType = TraktAccessTokenSample.TokenType
    )
}
