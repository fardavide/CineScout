package cinescout.auth.tmdb.data.remote.testdata

import cinescout.auth.tmdb.data.model.Authorized
import cinescout.auth.tmdb.data.remote.model.ConvertV4Session
import cinescout.auth.tmdb.data.remote.model.CreateAccessToken
import cinescout.auth.tmdb.data.remote.model.CreateRequestToken
import cinescout.auth.tmdb.data.testdata.TmdbAuthTestData
import cinescout.auth.tmdb.data.testdata.TmdbAuthTestData.SessionId
import cinescout.auth.tmdb.data.testdata.TmdbAuthTestData.AccountId
import cinescout.auth.tmdb.data.testdata.TmdbAuthTestData.RequestToken
import cinescout.auth.tmdb.data.testdata.TmdbAuthTestData.AccessToken

object RemoteTmdbAuthTestData {

    val AuthorizedRequestToken = Authorized(RequestToken)
    val ConvertV4SessionResponse = ConvertV4Session.Response(sessionId = SessionId.value)
    val CreateAccessTokenResponse = CreateAccessToken.Response(
        accessToken = AccessToken.value,
        accountId = AccountId.value
    )
    val CreateRequestTokenResponse = CreateRequestToken.Response(requestToken = RequestToken.value)
}