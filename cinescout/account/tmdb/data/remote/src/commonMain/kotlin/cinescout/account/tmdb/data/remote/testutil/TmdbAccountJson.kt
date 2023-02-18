package cinescout.account.tmdb.data.remote.testutil

import cinescout.account.tmdb.data.remote.model.GetAccount
import cinescout.account.tmdb.domain.sample.TmdbAccountSample
import cinescout.account.tmdb.domain.sample.TmdbAccountUsernameSample

object TmdbAccountJson {

    val Account = """
        {
            "${GetAccount.Response.Avatar}": {
                "${GetAccount.Response.Avatar.Gravatar}": {
                    "${GetAccount.Response.Avatar.Gravatar.Hash}": "${TmdbAccountSample.Account.gravatar?.hash}"
                }
            },
            "${GetAccount.Response.Username}": "${TmdbAccountUsernameSample.Username.value}"
        }
    """.trimIndent()
}
