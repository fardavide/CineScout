package cinescout.account.tmdb.data.remote.testdata

import cinescout.account.tmdb.data.remote.model.GetAccount
import cinescout.account.tmdb.domain.testdata.TmdbAccountTestData

object GetAccountResponseTestData {

    val Account = GetAccount.Response(TmdbAccountTestData.Account.username.value)
}
