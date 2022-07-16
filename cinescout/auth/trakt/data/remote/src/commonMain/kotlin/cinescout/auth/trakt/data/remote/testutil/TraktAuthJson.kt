package cinescout.auth.trakt.data.remote.testutil

import cinescout.auth.trakt.data.testdata.TraktAuthTestData

object TraktAuthJson {

    val AccessToken = """
        {
            "access_token": "${TraktAuthTestData.AccessToken.value}",
            "token_type": "${TraktAuthTestData.TokenType}",
            "expires_in": ${TraktAuthTestData.ExpiresIn},
            "refresh_token": "${TraktAuthTestData.RefreshToken.value}"
            "scope": "${TraktAuthTestData.Scope}"
            "created_at": ${TraktAuthTestData.CreatedAt}
        }
    """.trimIndent()
}
