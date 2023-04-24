package cinescout.anticipated.data.remote.mock

import cinescout.anticipated.data.remote.res.TraktAnticipatedMetadataJson
import cinescout.network.testutil.respondJson
import cinescout.network.testutil.unhandled
import io.ktor.client.engine.mock.MockEngine
import io.ktor.http.Url
import io.ktor.http.fullPath

fun TraktAnticipatedMockEngine() = MockEngine { requestData ->
    respondJson(getContent(requestData.url))
}

private fun getContent(url: Url): String {
    val fullPath = url.fullPath
    return when {
        "movies/anticipated" in fullPath -> TraktAnticipatedMetadataJson.OneMovie
        "shows/anticipated" in fullPath -> TraktAnticipatedMetadataJson.OneTvShow
        else -> unhandled(url)
    }
}
