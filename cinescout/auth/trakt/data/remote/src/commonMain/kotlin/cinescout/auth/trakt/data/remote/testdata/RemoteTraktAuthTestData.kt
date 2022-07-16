package cinescout.auth.trakt.data.remote.testdata

import cinescout.auth.trakt.data.remote.TraktRedirectUrl
import cinescout.auth.trakt.data.remote.model.CreateAccessToken
import cinescout.auth.trakt.data.testdata.TraktAuthTestData
import cinescout.network.trakt.TRAKT_CLIENT_ID
import cinescout.network.trakt.TRAKT_CLIENT_SECRET

internal object RemoteTraktAuthTestData {

    val CreateAccessTokenResponse = CreateAccessToken.Response(
        accessToken = TraktAuthTestData.AccessToken.value,
        createdAt = TraktAuthTestData.CreatedAt,
        expiresIn = TraktAuthTestData.ExpiresIn,
        refreshToken = TraktAuthTestData.RefreshToken.value,
        scope = TraktAuthTestData.Scope,
        tokenType = TraktAuthTestData.TokenType
    )
    val CreateAccessTokenFromCodeRequest = CreateAccessToken.Request.FromCode(
        code = "code",
        clientId = TRAKT_CLIENT_ID,
        clientSecret = TRAKT_CLIENT_SECRET,
        redirectUri = TraktRedirectUrl
    )
    val CreateAccessTokenFromRefreshTokenRequest = CreateAccessToken.Request.FromRefreshToken(
        refreshToken = TraktAuthTestData.RefreshToken.value,
        clientId = TRAKT_CLIENT_ID,
        clientSecret = TRAKT_CLIENT_SECRET,
        redirectUri = TraktRedirectUrl
    )
}
