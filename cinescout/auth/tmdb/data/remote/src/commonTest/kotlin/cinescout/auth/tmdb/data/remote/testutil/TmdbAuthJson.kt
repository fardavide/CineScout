package cinescout.auth.tmdb.data.remote.testutil

import cinescout.auth.tmdb.data.testdata.TmdbAuthTestData

object TmdbAuthJson {

    val AccessToken = """
    {
        "account_id": "${TmdbAuthTestData.AccountId.value}",
        "access_token": "${TmdbAuthTestData.AccessToken.value}"
    }
    """

    val ConvertV4Session = """
    {
        "session_id": "${TmdbAuthTestData.SessionId.value}"
    }
    """

    val RequestToken = """
    {
        "request_token": "${TmdbAuthTestData.RequestToken.value}"
    }
    """
}
