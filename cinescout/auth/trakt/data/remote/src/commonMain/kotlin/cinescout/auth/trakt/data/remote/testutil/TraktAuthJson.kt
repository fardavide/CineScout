package cinescout.auth.trakt.data.remote.testutil

import cinescout.auth.trakt.data.testdata.TraktAuthTestData

object TraktAuthJson {

    val AccessToken = """
        {
            "access_token": "${TraktAuthTestData.AccessToken.value}",
            "token_type": "bearer",
            "expires_in": 7200,
            "refresh_token": "${TraktAuthTestData.RefreshToken.value}"
            "scope": "public",
            "created_at": 1487889741
        }
    """.trimIndent()
}
