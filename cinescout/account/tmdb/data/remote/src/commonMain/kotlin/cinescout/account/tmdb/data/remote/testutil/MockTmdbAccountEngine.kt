package cinescout.account.tmdb.data.remote.testutil

import cinescout.network.tmdb.TmdbParameters
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.engine.mock.respondError
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.Url
import io.ktor.http.fullPath
import io.ktor.http.headersOf

fun MockTmdbAccountEngine() = MockEngine { requestData ->
    val sessionId = requestData.url.parameters[TmdbParameters.SessionId]
    if (sessionId != null) {
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
        "account" in fullPath -> TmdbAccountJson.Account
        else -> throw UnsupportedOperationException(fullPath)
    }
}
