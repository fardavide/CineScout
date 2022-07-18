package cinescout.movies.data.remote.trakt.testutil

import cinescout.network.trakt.TraktHeaders
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.engine.mock.respondError
import io.ktor.client.utils.buildHeaders
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.Url
import io.ktor.http.fullPath

fun MockTraktMovieEngine() = MockEngine { requestData ->
    val accessToken = requestData.headers[HttpHeaders.Authorization]
    val content = getContent(requestData.url, isAuthenticated = accessToken != null)
    if (content != null) {
        respond(
            content = content,
            status = HttpStatusCode.OK,
            headers = buildHeaders {
                append(HttpHeaders.ContentType, "application/json")
                append(TraktHeaders.Pagination.Page, "1")
                append(TraktHeaders.Pagination.TotalPages, "1")
            }
        )
    } else {
        respondError(HttpStatusCode.Unauthorized)
    }
}

private fun getContent(url: Url, isAuthenticated: Boolean): String? {
    val fullPath = url.fullPath
    return when {
        "ratings" in fullPath -> if (isAuthenticated) TraktMoviesRatingJson.OneMovie else null
        else -> throw UnsupportedOperationException(fullPath)
    }
}
