package cinescout.trending.data.remote.mock

import cinescout.network.testutil.respondJson
import cinescout.network.testutil.unhandled
import cinescout.trending.data.remote.res.TraktTrendingMetadataJson
import io.ktor.client.engine.mock.MockEngine
import io.ktor.http.Url
import io.ktor.http.fullPath

fun TraktTrendingMockEngine() = MockEngine { requestData ->
    respondJson(getContent(requestData.url))
}

private fun getContent(url: Url): String {
    val fullPath = url.fullPath
    return when {
        "movies/anticipated" in fullPath -> TraktTrendingMetadataJson.OneMovie
        "shows/anticipated" in fullPath -> TraktTrendingMetadataJson.OneTvShow
        else -> unhandled(url)
    }
}
