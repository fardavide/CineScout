package cinescout.movies.data.remote.tmdb.testutil

import cinescout.movies.domain.testdata.TmdbMovieIdTestData
import cinescout.network.tmdb.testutil.TmdbGenericJson
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
    val movieId = fullPath.substringAfter("/movie/").substringBefore("/").substringBefore("?")
    return when {
        "rated/movies" in fullPath -> TmdbMoviesRatingJson.OneMovie
        "rating" in fullPath -> TmdbGenericJson.EmptySuccess
        TmdbMovieIdTestData.Inception.value.toString() == movieId -> TmdbMovieJson.Inception
        TmdbMovieIdTestData.TheWolfOfWallStreet.value.toString() == movieId -> TmdbMovieJson.TheWolfOfWallStreet
        else -> throw java.lang.UnsupportedOperationException(fullPath)
    }
}
