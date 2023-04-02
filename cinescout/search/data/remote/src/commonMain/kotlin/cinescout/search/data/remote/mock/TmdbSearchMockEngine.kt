package cinescout.search.data.remote.mock

import cinescout.network.testutil.respondJson
import cinescout.search.data.remote.res.TmdbSearchJson
import io.ktor.client.engine.mock.MockEngine
import io.ktor.http.Url
import io.ktor.http.fullPath

fun TmdbSearchMockEngine(): MockEngine = MockEngine { requestData ->
    respondJson(getContent(requestData.url))
}

private fun getContent(url: Url): String {
    val fullPath = url.fullPath
    return when {
        "search" in fullPath -> TmdbSearchJson.forQuery(url.parameters["query"]!!)
        else -> throw UnsupportedOperationException(fullPath)
    }
}
