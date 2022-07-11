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
    val path = url.fullPath
    return when {
        "auth/request_token" in path -> TmdbAuthJson.RequestToken
        "auth/access_token" in path -> TmdbAuthJson.AccessToken
        "authentication/session/convert/4" in path -> TmdbAuthJson.ConvertV4Session
        else -> throw java.lang.UnsupportedOperationException(path)
    }
}
