package cinescout.movies.data.remote.trakt.testutil

import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpHeaders.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.Url
import io.ktor.http.fullPath
import io.ktor.http.headersOf

internal fun MockTraktMovieEngine() = MockEngine { request ->
    respond(
        content = getContent(request.url),
        status = HttpStatusCode.OK,
        headers = headersOf(ContentType, "application/json")
    )
}

private fun getContent(url: Url): String {
    val fullPath = url.fullPath
    return when {
        else -> throw java.lang.UnsupportedOperationException(fullPath)
    }
}
