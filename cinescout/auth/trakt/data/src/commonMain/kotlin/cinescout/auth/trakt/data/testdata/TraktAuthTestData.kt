package cinescout.auth.trakt.data.testdata

import cinescout.auth.trakt.data.model.TraktAccessToken
import cinescout.auth.trakt.data.model.TraktAuthorizationCode
import cinescout.auth.trakt.data.model.TraktRefreshToken

object TraktAuthTestData {

    val AccessToken = TraktAccessToken("Access token")
    val AuthorizationCode = TraktAuthorizationCode("Authorization code")
    val RefreshToken = TraktRefreshToken("Refresh token")
}
