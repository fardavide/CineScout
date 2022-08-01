package cinescout.account.trakt.data.remote.testutil

import cinescout.account.domain.model.Gravatar
import cinescout.account.trakt.data.remote.model.GetAccount
import cinescout.account.trakt.domain.testData.TraktAccountTestData

object TraktAccountJson {

    val Account = """
    {
        "${GetAccount.Response.User}": {
            "${GetAccount.Response.User.Images}": {
                "${GetAccount.Response.User.Images.Avatar}": {
                    "${GetAccount.Response.User.Images.Avatar.Full}": 
                        "${TraktAccountTestData.Account.gravatar?.getUrl(Gravatar.Size.MEDIUM)}"
                }
            },
            "${GetAccount.Response.User.Username}": "${TraktAccountTestData.Username.value}"
        }
    }
    """
}
