package cinescout.account.tmdb.data.remote.testutil

import cinescout.account.tmdb.data.remote.model.GetAccount
import cinescout.account.tmdb.domain.sample.Sample

object TmdbAccountJson {

    val Account = """
        {
            "${GetAccount.Response.Avatar}": {
                "${GetAccount.Response.Avatar.Gravatar}": {
                    "${GetAccount.Response.Avatar.Gravatar.Hash}": "${Sample.Account.gravatar?.hash}"
                }
            },
            "${GetAccount.Response.Username}": "${Sample.Username.value}"
        }
    """.trimIndent()
}
