package cinescout.account.tmdb.data.remote.testdata

import cinescout.account.tmdb.data.remote.model.GetAccount
import cinescout.account.tmdb.domain.sample.Sample

object GetAccountResponseTestData {

    val Account = run {
        val avatar = when (val gravatar = Sample.Account.gravatar) {
            null -> null
            else -> GetAccount.Response.Avatar(GetAccount.Response.Avatar.Gravatar(hash = gravatar.hash))
        }
        GetAccount.Response(
            avatar = avatar,
            username = Sample.Username.value
        )
    }
}
