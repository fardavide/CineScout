package cinescout.account.trakt.data.remote.testdata

import cinescout.account.domain.model.Gravatar
import cinescout.account.trakt.data.remote.model.GetAccount
import cinescout.account.trakt.domain.testData.TraktAccountTestData

object GetAccountResponseTestData {

    val Account = run {
        val images = run {
            val gravatar = checkNotNull(TraktAccountTestData.Account.gravatar)
            val full = gravatar.getUrl(Gravatar.Size.MEDIUM)
            val avatar = GetAccount.Response.User.Images.Avatar(full = full)
            GetAccount.Response.User.Images(avatar = avatar)
        }
        GetAccount.Response(
            GetAccount.Response.User(
                images = images,
                username = TraktAccountTestData.Username.value
            )
        )
    }
}
