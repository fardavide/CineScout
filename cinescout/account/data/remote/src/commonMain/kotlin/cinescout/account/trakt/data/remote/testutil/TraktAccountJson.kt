package cinescout.account.trakt.data.remote.testutil

import cinescout.account.domain.model.Gravatar
import cinescout.account.domain.sample.AccountSample
import cinescout.account.domain.sample.AccountUsernameSample
import cinescout.account.trakt.data.remote.model.GetAccount

object TraktAccountJson {

    val Account = """
        {
            "${GetAccount.Response.User}": {
                "${GetAccount.Response.User.Images}": {
                    "${GetAccount.Response.User.Images.Avatar}": {
                        "${GetAccount.Response.User.Images.Avatar.Full}": 
                            "${AccountSample.Trakt.gravatar?.getUrl(Gravatar.Size.MEDIUM)}"
                    }
                },
                "${GetAccount.Response.User.Username}": "${AccountUsernameSample.Trakt.value}"
            }
        }
    """.trimIndent()
}
