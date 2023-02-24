package cinescout.auth.trakt.data.sample

import cinescout.auth.trakt.data.model.TraktAccessAndRefreshTokens

object TraktAccessAndRefreshTokensSample {

    val RefreshedTokens = TraktAccessAndRefreshTokens(
        accessToken = TraktAccessTokenSample.RefreshedAccessToken,
        refreshToken = TraktRefreshTokenSample.RefreshToken
    )

    val Tokens = TraktAccessAndRefreshTokens(
        accessToken = TraktAccessTokenSample.AccessToken,
        refreshToken = TraktRefreshTokenSample.RefreshToken
    )
}
