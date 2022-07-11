package cinescout.movies.data.remote.tmdb.testutil

import cinescout.movies.domain.testdata.TmdbMovieIdTestData
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.Url
import io.ktor.http.fullPath
import io.ktor.http.headersOf

fun MockTmdbMovieEngine() = MockEngine { request ->
    respond(
        content = getContent(request.url),
        status = HttpStatusCode.OK,
        headers = headersOf(HttpHeaders.ContentType, "application/json")
    )
}

private fun getContent(url: Url): String {
    val fullPath = url.fullPath
    return when (fullPath.substringAfter("/movie/").substringBefore("/").substringBefore("?")) {
        TmdbMovieIdTestData.Inception.value.toString() -> TmdbMovieJson.Inception
        TmdbMovieIdTestData.TheWolfOfWallStreet.value.toString() -> TmdbMovieJson.TheWolfOfWallStreet
        else -> throw java.lang.UnsupportedOperationException(fullPath)
    }
}
