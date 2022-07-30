package cinescout.auth.tmdb.data.remote.testutil

import cinescout.auth.tmdb.data.testdata.TmdbAuthTestData
import cinescout.network.tmdb.TmdbParameters

object TmdbAuthJson {

    val AccessToken = """
    {
        "account_id": "${TmdbAuthTestData.AccountId.value}",
        "access_token": "${TmdbAuthTestData.AccessToken.value}"
    }
    """

    val ConvertV4Session = """
    {
        "${TmdbParameters.SessionId}": "${TmdbAuthTestData.SessionId.value}"
    }
    """

    val RequestToken = """
    {
        "request_token": "${TmdbAuthTestData.RequestToken.value}"
    }
    """
}
