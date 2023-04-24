package cinescout.account.trakt.data.remote.testutil

import cinescout.network.testutil.hasValidAccessToken
import cinescout.network.testutil.respondJson
import cinescout.network.testutil.respondUnauthorized
import cinescout.network.testutil.unhandled
import io.ktor.client.engine.mock.MockEngine
import io.ktor.http.Url
import io.ktor.http.fullPath

fun TraktAccountMockEngine() = MockEngine { requestData ->
    val json = getContent(requestData.url)
    when (requestData.hasValidAccessToken()) {
        true -> respondJson(json)
        false -> respondUnauthorized()
    }
}

private fun getContent(url: Url): String {
    val fullPath = url.fullPath
    return when {
        "users/settings" in fullPath -> TraktAccountJson.Account
        else -> unhandled(url)
    }
}
