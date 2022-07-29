package cinescout.auth.tmdb.data.testdata

import cinescout.auth.tmdb.data.model.Authorized
import cinescout.auth.tmdb.data.model.TmdbAccessToken
import cinescout.auth.tmdb.data.model.TmdbAccessTokenAndAccountId
import cinescout.auth.tmdb.data.model.TmdbAccountId
import cinescout.auth.tmdb.data.model.TmdbCredentials
import cinescout.auth.tmdb.data.model.TmdbRequestToken
import cinescout.auth.tmdb.data.model.TmdbSessionId

object TmdbAuthTestData {

    val AccessToken = TmdbAccessToken("Access token")
    val AccountId = TmdbAccountId("Account id")
    val AccessTokenAndAccountId = TmdbAccessTokenAndAccountId(AccessToken, AccountId)
    val RequestToken = TmdbRequestToken("Request token")
    val SessionId = TmdbSessionId("Session id")
    const val TokenAuthorizationUrl = "https://www.themoviedb.org/auth/access?request_token=token"

    val AuthorizedRequestToken = Authorized(RequestToken)
    val Credentials = TmdbCredentials(
        accessToken = AccessToken,
        accountId = AccountId,
        sessionId = SessionId
    )
}
