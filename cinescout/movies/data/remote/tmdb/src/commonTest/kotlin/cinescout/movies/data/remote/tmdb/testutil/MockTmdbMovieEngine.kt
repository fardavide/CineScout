package cinescout.movies.data.remote.tmdb.testutil

import cinescout.movies.data.remote.testdata.TmdbMovieTestData
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.Url
import io.ktor.http.fullPath
import io.ktor.http.headersOf

internal fun MockTmdbMovieEngine() = MockEngine { request ->
    respond(
        content = getContent(request.url),
        status = HttpStatusCode.OK,
        headers = headersOf(HttpHeaders.ContentType, "application/json")
    )
}

private fun getContent(url: Url): String {
    val fullPath = url.fullPath
    return when {
        "movie/${TmdbMovieTestData.Inception.id.value}" in fullPath -> TmdbMovieJson.Inception
        else -> throw java.lang.UnsupportedOperationException(fullPath)
    }
}
