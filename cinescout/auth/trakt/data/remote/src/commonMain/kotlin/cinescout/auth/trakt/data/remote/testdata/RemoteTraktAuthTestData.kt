package cinescout.auth.trakt.data.remote.testdata

import cinescout.auth.trakt.data.remote.TraktRedirectUrl
import cinescout.auth.trakt.data.remote.model.CreateAccessToken
import cinescout.auth.trakt.data.sample.TraktAccessTokenSample
import cinescout.auth.trakt.data.sample.TraktRefreshTokenSample
import cinescout.network.trakt.TRAKT_CLIENT_ID
import cinescout.network.trakt.TRAKT_CLIENT_SECRET

internal object RemoteTraktAuthTestData {

    val CreateAccessTokenResponse = CreateAccessToken.Response(
        accessToken = TraktAccessTokenSample.AccessToken.value,
        createdAt = TraktAccessTokenSample.CreatedAt,
        expiresIn = TraktAccessTokenSample.ExpiresIn,
        refreshToken = TraktRefreshTokenSample.RefreshToken.value,
        scope = TraktAccessTokenSample.Scope,
        tokenType = TraktAccessTokenSample.TokenType
    )
    val CreateAccessTokenFromCodeRequest = CreateAccessToken.Request.FromCode(
        code = "code",
        clientId = TRAKT_CLIENT_ID,
        clientSecret = TRAKT_CLIENT_SECRET,
        redirectUri = TraktRedirectUrl,
        grantType = "authorization_code"
    )
    val CreateAccessTokenFromRefreshTokenRequest = CreateAccessToken.Request.FromRefreshToken(
        refreshToken = TraktRefreshTokenSample.RefreshToken.value,
        clientId = TRAKT_CLIENT_ID,
        clientSecret = TRAKT_CLIENT_SECRET,
        redirectUri = TraktRedirectUrl,
        grantType = "refresh_token"
    )
}
