package cinescout.account.tmdb.data.remote.testutil

import cinescout.account.domain.sample.AccountSample
import cinescout.account.domain.sample.AccountUsernameSample
import cinescout.account.tmdb.data.remote.model.GetAccount

object TmdbAccountJson {

    val Account = """
        {
            "${GetAccount.Response.Avatar}": {
                "${GetAccount.Response.Avatar.Gravatar}": {
                    "${GetAccount.Response.Avatar.Gravatar.Hash}": "${AccountSample.Tmdb.gravatar?.hash}"
                }
            },
            "${GetAccount.Response.Username}": "${AccountUsernameSample.Tmdb.value}"
        }
    """.trimIndent()
}
