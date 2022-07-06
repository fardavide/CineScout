package cinescout.auth.tmdb.data.remote.testutil

import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.Url
import io.ktor.http.fullPath
import io.ktor.http.headersOf

fun MockTmdbAuthEngine() = MockEngine { request ->
    respond(
        content = getContent(request.url),
        status = HttpStatusCode.OK,
        headers = headersOf(HttpHeaders.ContentType, "application/json")
    )
}

private fun getContent(url: Url): String {
    val fullPath = url.fullPath
    return when {
        "auth/request_token" in fullPath -> TmdbAuthJson.RequestToken
        "auth/access_token" in fullPath -> TmdbAuthJson.AccessToken
        "authentication/session/convert/4" in fullPath -> TmdbAuthJson.ConvertV4Session
        else -> throw java.lang.UnsupportedOperationException(fullPath)
    }
}
