package cinescout.account.trakt.data.remote.testutil

import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.engine.mock.respondError
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.Url
import io.ktor.http.fullPath
import io.ktor.http.headersOf

fun MockTraktAccountEngine() = MockEngine { requestData ->
    val accessToken = requestData.headers[HttpHeaders.Authorization]
    if (accessToken != null) {
        respond(
            content = getContent(requestData.url),
            status = HttpStatusCode.OK,
            headers = headersOf(HttpHeaders.ContentType, "application/json")
        )
    } else {
        respondError(HttpStatusCode.Unauthorized)
    }
}

private fun getContent(url: Url): String {
    val fullPath = url.fullPath
    return when {
        "users/settings" in fullPath -> TraktAccountJson.Account
        else -> throw UnsupportedOperationException(fullPath)
    }
}
