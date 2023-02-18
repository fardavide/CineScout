package cinescout.account.tmdb.data.remote.testdata

import cinescout.account.tmdb.data.remote.model.GetAccount
import cinescout.account.tmdb.domain.sample.TmdbAccountSample
import cinescout.account.tmdb.domain.sample.TmdbAccountUsernameSample

object GetAccountResponseTestData {

    val Account = run {
        val avatar = when (val gravatar = TmdbAccountSample.Account.gravatar) {
            null -> null
            else -> GetAccount.Response.Avatar(GetAccount.Response.Avatar.Gravatar(hash = gravatar.hash))
        }
        GetAccount.Response(
            avatar = avatar,
            username = TmdbAccountUsernameSample.Username.value
        )
    }
}
