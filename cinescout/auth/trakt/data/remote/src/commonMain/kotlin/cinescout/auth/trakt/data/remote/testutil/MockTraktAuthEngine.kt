package cinescout.auth.trakt.data.remote.testutil

import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.Url
import io.ktor.http.fullPath
import io.ktor.http.headersOf

fun MockTraktAuthEngine() = MockEngine { requestData ->
    respond(
        content = getContent(requestData.url),
        status = HttpStatusCode.OK,
        headers = headersOf(HttpHeaders.ContentType, "application/json")
    )
}

private fun getContent(url: Url): String {
    val path = url.fullPath
    return if ("oauth/token" in path) TraktAuthJson.AccessToken
    else throw UnsupportedOperationException(path)
}
