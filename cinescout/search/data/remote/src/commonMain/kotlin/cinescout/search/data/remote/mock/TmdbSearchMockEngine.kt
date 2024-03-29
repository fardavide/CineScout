package cinescout.search.data.remote.mock

import cinescout.network.testutil.respondJson
import cinescout.network.testutil.unhandled
import cinescout.search.data.remote.res.TraktSearchJson
import io.ktor.client.engine.mock.MockEngine
import io.ktor.http.Url
import io.ktor.http.fullPath

fun TraktSearchMockEngine(): MockEngine = MockEngine { requestData ->
    respondJson(getContent(requestData.url))
}

private fun getContent(url: Url): String {
    val fullPath = url.fullPath
    return when {
        "search" in fullPath -> TraktSearchJson.forQuery(url.parameters["query"]!!)
        else -> unhandled(url)
    }
}
