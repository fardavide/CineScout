package cinescout.auth.tmdb.data.remote.testutil

import cinescout.auth.tmdb.data.sample.TmdbAccessTokenSample
import cinescout.auth.tmdb.data.sample.TmdbAccountIdSample
import cinescout.auth.tmdb.data.sample.TmdbRequestTokenSample
import cinescout.auth.tmdb.data.sample.TmdbSessionIdSample
import cinescout.network.tmdb.TmdbParameters

object TmdbAuthJson {

    val AccessToken = """
        {
            "account_id": "${TmdbAccountIdSample.AccountId.value}",
            "access_token": "${TmdbAccessTokenSample.AccessToken.value}"
        }
    """.trimIndent()

    val ConvertV4Session = """
        {
            "${TmdbParameters.SessionId}": "${TmdbSessionIdSample.SessionId.value}"
        }
    """.trimIndent()

    val RequestToken = """
        {
            "request_token": "${TmdbRequestTokenSample.RequestToken.value}"
        }
    """.trimIndent()
}
