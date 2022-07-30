package cinescout.account.tmdb.data.remote.testutil

import cinescout.account.tmdb.data.remote.model.GetAccount
import cinescout.account.tmdb.domain.testdata.TmdbAccountTestData

object TmdbAccountJson {

    val Account = """
    {
        "${GetAccount.Response.Username}": "${TmdbAccountTestData.Account.username.value}"
    }
    """
}
