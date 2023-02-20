package cinescout.auth.trakt.data.sample

import cinescout.auth.trakt.data.model.TraktAccessToken

object TraktAccessTokenSample {

    val AccessToken = TraktAccessToken("Access token")
    const val CreatedAt = 123L
    const val ExpiresIn = 456L
    const val Scope = "public"
    const val TokenType = "bearer"
}
