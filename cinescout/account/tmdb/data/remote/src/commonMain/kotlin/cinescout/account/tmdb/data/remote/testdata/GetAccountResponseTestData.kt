package cinescout.account.tmdb.data.remote.testdata

import cinescout.account.domain.sample.AccountSample
import cinescout.account.domain.sample.AccountUsernameSample
import cinescout.account.tmdb.data.remote.model.GetAccount

object GetAccountResponseTestData {

    val Account = run {
        val avatar = when (val gravatar = AccountSample.Tmdb.gravatar) {
            null -> null
            else -> GetAccount.Response.Avatar(GetAccount.Response.Avatar.Gravatar(hash = gravatar.hash))
        }
        GetAccount.Response(
            avatar = avatar,
            username = AccountUsernameSample.Tmdb.value
        )
    }
}
