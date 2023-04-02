package screenplay.data.remote.trakt.mock

import cinescout.network.testutil.hasValidAccessToken
import cinescout.network.testutil.isGet
import cinescout.network.testutil.respondJson
import cinescout.network.testutil.respondUnauthorized
import io.ktor.client.engine.mock.MockEngine
import io.ktor.http.HttpMethod
import io.ktor.http.Url
import io.ktor.http.fullPath
import screenplay.data.remote.trakt.res.TraktRecommendationsJson

fun TraktRecommendationMockEngine() = MockEngine { requestData ->
    when (requestData.hasValidAccessToken()) {
        true -> respondJson(getContent(requestData.method, requestData.url))
        false -> respondUnauthorized()
    }
}

private fun getContent(method: HttpMethod, url: Url): String {
    val fullPath = url.fullPath
    return when {
        method.isGet() && "recommendations/movies" in fullPath -> TraktRecommendationsJson.ThreeMovies
        method.isGet() && "recommendations/shows" in fullPath -> TraktRecommendationsJson.ThreeTvShows
        method.isGet() && "recommendations" in fullPath -> TraktRecommendationsJson.ThreeMoviesAndThreeTvShows
        else -> throw UnsupportedOperationException(fullPath)
    }
}
