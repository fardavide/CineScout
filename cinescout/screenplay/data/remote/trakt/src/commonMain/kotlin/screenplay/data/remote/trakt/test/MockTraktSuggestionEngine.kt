package screenplay.data.remote.trakt.test

import cinescout.network.testutil.hasValidAccessToken
import cinescout.network.testutil.isGet
import cinescout.network.testutil.respondJson
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respondError
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.Url
import io.ktor.http.fullPath

fun MockTraktSuggestionEngine() = MockEngine { requestData ->
    if (requestData.hasValidAccessToken()) {
        respondJson(getContent(requestData.method, requestData.url))
    } else {
        respondError(HttpStatusCode.Unauthorized)
    }
}

private fun getContent(method: HttpMethod, url: Url): String {
    val fullPath = url.fullPath
    return when {
        method.isGet() && "recommendations/movies" in fullPath -> TraktRecommendedMoviesJson.ThreeMovies
        method.isGet() && "recommendations/shows" in fullPath -> TraktRecommendedTvShowsJson.ThreeTvShows
        else -> throw UnsupportedOperationException(fullPath)
    }
}
