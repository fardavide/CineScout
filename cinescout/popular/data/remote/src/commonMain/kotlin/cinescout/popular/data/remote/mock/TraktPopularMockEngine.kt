package cinescout.popular.data.remote.mock

import cinescout.network.testutil.respondJson
import cinescout.network.testutil.unhandled
import cinescout.popular.data.remote.res.TraktPopularMetadataJson
import io.ktor.client.engine.mock.MockEngine
import io.ktor.http.Url
import io.ktor.http.fullPath

fun TraktPopularMockEngine() = MockEngine { requestData ->
    respondJson(getContent(requestData.url))
}

private fun getContent(url: Url): String {
    val fullPath = url.fullPath
    return when {
        "movies/popular" in fullPath -> TraktPopularMetadataJson.OneMovie
        "shows/popular" in fullPath -> TraktPopularMetadataJson.OneTvShow
        else -> unhandled(url)
    }
}
