package cinescout.auth.trakt.data.remote.testdata

import cinescout.auth.trakt.data.model.CreateAccessToken
import cinescout.auth.trakt.data.sample.TraktAccessTokenSample
import cinescout.auth.trakt.data.sample.TraktRefreshTokenSample

internal object RemoteTraktAuthTestData {

    val CreateAccessTokenResponse = CreateAccessToken.Response(
        accessToken = TraktAccessTokenSample.AccessToken.value,
        createdAt = TraktAccessTokenSample.CreatedAt,
        expiresIn = TraktAccessTokenSample.ExpiresIn,
        refreshToken = TraktRefreshTokenSample.RefreshToken.value,
        scope = TraktAccessTokenSample.Scope,
        tokenType = TraktAccessTokenSample.TokenType
    )
}
