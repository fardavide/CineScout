package cinescout.auth.trakt.data.sample

import cinescout.auth.trakt.data.model.TraktAccessAndRefreshTokens

object TraktAccessAndRefreshTokensSample {

    val AccessAndRefreshToken = TraktAccessAndRefreshTokens(
        accessToken = TraktAccessTokenSample.AccessToken,
        refreshToken = TraktRefreshTokenSample.RefreshToken
    )
}
