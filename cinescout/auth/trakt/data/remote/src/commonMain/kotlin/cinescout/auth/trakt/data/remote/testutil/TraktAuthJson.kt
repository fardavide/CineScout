package cinescout.auth.trakt.data.remote.testutil

import cinescout.auth.trakt.data.sample.TraktAccessTokenSample
import cinescout.auth.trakt.data.sample.TraktRefreshTokenSample

object TraktAuthJson {

    val AccessToken = """
        {
            "access_token": "${TraktAccessTokenSample.AccessToken.value}",
            "token_type": "${TraktAccessTokenSample.TokenType}",
            "expires_in": ${TraktAccessTokenSample.ExpiresIn},
            "refresh_token": "${TraktRefreshTokenSample.RefreshToken.value}"
            "scope": "${TraktAccessTokenSample.Scope}"
            "created_at": ${TraktAccessTokenSample.CreatedAt}
        }
    """.trimIndent()
}
