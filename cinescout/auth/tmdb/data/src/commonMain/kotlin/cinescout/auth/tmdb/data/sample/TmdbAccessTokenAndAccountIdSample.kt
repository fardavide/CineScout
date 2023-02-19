package cinescout.auth.tmdb.data.sample

import cinescout.auth.tmdb.data.model.TmdbAccessTokenAndAccountId

object TmdbAccessTokenAndAccountIdSample {

    val AccessTokenAndAccountId = TmdbAccessTokenAndAccountId(
        accessToken = TmdbAccessTokenSample.AccessToken,
        accountId = TmdbAccountIdSample.AccountId
    )
}
