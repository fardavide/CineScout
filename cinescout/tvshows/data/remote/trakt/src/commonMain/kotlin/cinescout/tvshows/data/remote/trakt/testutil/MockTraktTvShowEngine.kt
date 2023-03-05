package cinescout.tvshows.data.remote.trakt.testutil

import cinescout.network.testutil.hasValidAccessToken
import cinescout.network.trakt.testutil.TraktGenericJson
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.engine.mock.respondError
import io.ktor.client.utils.buildHeaders
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.Url
import io.ktor.http.fullPath

fun MockTraktTvShowEngine() = MockEngine { requestData ->
    if (requestData.hasValidAccessToken()) {
        respond(
            content = getContent(requestData.method, requestData.url),
            status = HttpStatusCode.OK,
            headers = buildHeaders {
                append(HttpHeaders.ContentType, "application/json")
            }
        )
    } else {
        respondError(HttpStatusCode.Unauthorized)
    }
}

private fun getContent(method: HttpMethod, url: Url): String {
    val fullPath = url.fullPath
    return when {
        "sync/ratings/shows" in fullPath -> TraktTvShowsRatingJson.OneTvShow
        "sync/ratings" in fullPath && method == HttpMethod.Post -> TraktGenericJson.EmptySuccess
        "sync/watchlist/shows" in fullPath -> TraktTvShowsWatchlistJson.OneTvShow
        "watchlist" in fullPath -> TraktGenericJson.EmptySuccess
        else -> throw UnsupportedOperationException(fullPath)
    }
}
