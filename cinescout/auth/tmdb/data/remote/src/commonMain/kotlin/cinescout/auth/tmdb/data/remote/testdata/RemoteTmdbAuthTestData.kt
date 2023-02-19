package cinescout.auth.tmdb.data.remote.testdata

import cinescout.auth.tmdb.data.model.Authorized
import cinescout.auth.tmdb.data.remote.model.ConvertV4Session
import cinescout.auth.tmdb.data.remote.model.CreateAccessToken
import cinescout.auth.tmdb.data.remote.model.CreateRequestToken
import cinescout.auth.tmdb.data.sample.TmdbAccessTokenSample
import cinescout.auth.tmdb.data.sample.TmdbAccountIdSample
import cinescout.auth.tmdb.data.sample.TmdbRequestTokenSample
import cinescout.auth.tmdb.data.sample.TmdbSessionIdSample

internal object RemoteTmdbAuthTestData {

    val AuthorizedRequestToken = Authorized(TmdbRequestTokenSample.RequestToken)
    val ConvertV4SessionResponse = ConvertV4Session.Response(sessionId = TmdbSessionIdSample.SessionId.value)
    val CreateAccessTokenResponse = CreateAccessToken.Response(
        accessToken = TmdbAccessTokenSample.AccessToken.value,
        accountId = TmdbAccountIdSample.AccountId.value
    )
    val CreateRequestTokenResponse =
        CreateRequestToken.Response(requestToken = TmdbRequestTokenSample.RequestToken.value)
}
