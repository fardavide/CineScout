package cinescout.recommended.data.remote.mock

import cinescout.network.testutil.respondJson
import cinescout.network.testutil.unhandled
import cinescout.recommended.data.remote.res.TraktRecommendedMetadataJson
import io.ktor.client.engine.mock.MockEngine
import io.ktor.http.Url
import io.ktor.http.fullPath

fun TraktRecommendedMockEngine() = MockEngine { requestData ->
    respondJson(getContent(requestData.url))
}

private fun getContent(url: Url): String {
    val fullPath = url.fullPath
    return when {
        "movies/recommended" in fullPath -> TraktRecommendedMetadataJson.OneMovie
        "shows/recommended" in fullPath -> TraktRecommendedMetadataJson.OneTvShow
        else -> unhandled(url)
    }
}
