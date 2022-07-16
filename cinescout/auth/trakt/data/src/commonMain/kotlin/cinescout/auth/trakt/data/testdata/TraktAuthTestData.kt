package cinescout.auth.trakt.data.testdata

import cinescout.auth.trakt.data.model.TraktAccessAndRefreshTokens
import cinescout.auth.trakt.data.model.TraktAccessToken
import cinescout.auth.trakt.data.model.TraktRefreshToken
import cinescout.auth.trakt.domain.model.TraktAuthorizationCode

object TraktAuthTestData {

    val AccessToken = TraktAccessToken("Access token")
    const val AppAuthorizationUrl = "https://trakt.tv/oauth/authorize"
    val AuthorizationCode = TraktAuthorizationCode("Authorization code")
    const val CreatedAt = 123L
    const val ExpiresIn = 456L
    val RefreshToken = TraktRefreshToken("Refresh token")
    val AccessAndRefreshToken = TraktAccessAndRefreshTokens(AccessToken, RefreshToken)
    const val Scope = "public"
    const val TokenType = "bearer"
}
