package cinescout.account.trakt.data.remote.testdata

import cinescout.account.domain.model.Gravatar
import cinescout.account.domain.sample.AccountSample
import cinescout.account.domain.sample.AccountUsernameSample
import cinescout.account.trakt.data.remote.model.GetAccount

object GetAccountResponseTestData {

    val Account = run {
        val images = run {
            val gravatar = checkNotNull(AccountSample.Trakt.gravatar)
            val full = gravatar.getUrl(Gravatar.Size.MEDIUM)
            val avatar = GetAccount.Response.User.Images.Avatar(full = full)
            GetAccount.Response.User.Images(avatar = avatar)
        }
        GetAccount.Response(
            GetAccount.Response.User(
                images = images,
                username = AccountUsernameSample.Trakt.value
            )
        )
    }
}
