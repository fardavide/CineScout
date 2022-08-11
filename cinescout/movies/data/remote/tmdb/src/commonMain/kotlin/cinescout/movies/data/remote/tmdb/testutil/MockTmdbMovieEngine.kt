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

fun MockTmdbMovieEngine() = MockEngine { requestData ->
    respond(
        content = getContent(requestData.url),
        status = HttpStatusCode.OK,
        headers = headersOf(HttpHeaders.ContentType, "application/json")
    )
}

private fun getContent(url: Url): String {
    val fullPath = url.fullPath
    val movieId = fullPath.substringAfter("/movie/")
        .substringBefore("/")
        .substringBefore("?")
    return when {
        "discover" in fullPath -> TmdbDiscoverMoviesJson.TwoMovies
        "rated/movies" in fullPath -> TmdbMoviesRatingJson.OneMovie
        "keywords" in fullPath -> TmdbMovieKeywordsJson.Inception
        "rating" in fullPath -> TmdbGenericJson.EmptySuccess
        "watchlist" in fullPath -> TmdbGenericJson.EmptySuccess
        "/${TmdbMovieIdTestData.Inception.value}/credits" in fullPath -> TmdbMovieCreditsJson.Inception
        TmdbMovieIdTestData.Inception.value.toString() == movieId -> TmdbMovieDetailsJson.Inception
        TmdbMovieIdTestData.TheWolfOfWallStreet.value.toString() == movieId -> TmdbMovieDetailsJson.TheWolfOfWallStreet
        else -> throw UnsupportedOperationException(fullPath)
    }
}
