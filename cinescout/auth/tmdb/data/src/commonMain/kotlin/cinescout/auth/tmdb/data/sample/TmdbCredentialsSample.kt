package cinescout.auth.tmdb.data.sample

import cinescout.auth.tmdb.data.model.TmdbCredentials

object TmdbCredentialsSample {

    val Credentials = TmdbCredentials(
        accessToken = TmdbAccessTokenSample.AccessToken,
        accountId = TmdbAccountIdSample.AccountId,
        sessionId = TmdbSessionIdSample.SessionId
    )
}
