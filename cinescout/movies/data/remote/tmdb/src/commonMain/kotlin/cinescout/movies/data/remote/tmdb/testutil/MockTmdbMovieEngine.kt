package cinescout.movies.data.remote.tmdb.testutil

import cinescout.movies.domain.model.TmdbMovieId
import cinescout.movies.domain.testdata.TmdbMovieIdTestData
import cinescout.network.tmdb.testutil.TmdbGenericJson
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.Url
import io.ktor.http.fullPath
import io.ktor.http.headersOf

fun MockTmdbMovieEngine() = MockEngine { requestData ->
    respond(
        content = getContent(requestData.method, requestData.url),
        status = HttpStatusCode.OK,
        headers = headersOf(HttpHeaders.ContentType, "application/json")
    )
}

private fun getContent(method: HttpMethod, url: Url): String {
    val fullPath = url.fullPath
    val movieId = fullPath.substringAfter("/movie/")
        .substringBefore("/")
        .substringBefore("?")
    return when {
        "discover/movies" in fullPath -> TmdbDiscoverMoviesJson.TwoMovies
        "rated/movies" in fullPath -> TmdbMoviesRatingJson.OneMovie
        "/${TmdbMovieIdTestData.Inception.value}/keywords" in fullPath -> TmdbMovieKeywordsJson.Inception
        "/${TmdbMovieIdTestData.TheWolfOfWallStreet.value}/keywords" in fullPath ->
            TmdbMovieKeywordsJson.TheWolfOfWallStreet
        "rating" in fullPath -> TmdbGenericJson.EmptySuccess
        "recommendations" in fullPath -> TmdbMovieRecommendationsJson.TwoMovies
        "watchlist/movies" in fullPath && method == HttpMethod.Get -> TmdbMoviesWatchlistJson.OneMovie
        "watchlist/movies" in fullPath && method == HttpMethod.Post -> TmdbGenericJson.EmptySuccess
        "/${TmdbMovieIdTestData.Inception.value}/credits" in fullPath -> TmdbMovieCreditsJson.Inception
        "/${TmdbMovieIdTestData.TheWolfOfWallStreet.value}/credits" in fullPath ->
            TmdbMovieCreditsJson.TheWolfOfWallStreet
        TmdbMovieIdTestData.Inception.value.toString() == movieId -> TmdbMovieDetailsJson.Inception
        TmdbMovieIdTestData.TheWolfOfWallStreet.value.toString() == movieId -> TmdbMovieDetailsJson.TheWolfOfWallStreet
        else -> throw UnsupportedOperationException(fullPath)
    }
}

fun MockEngine.addMovieDetailsHandler(movieId: TmdbMovieId, responseJson: String) {
    val oldHandlers = config.requestHandlers + emptyList()
    config.requestHandlers.clear()
    config.addHandler { requestData ->
        val fullPath = requestData.url.fullPath
        val movieIdParam = fullPath.substringAfter("/movie/")
            .substringBefore("?")
        respond(
            content = when (movieIdParam) {
                movieId.value.toString() -> responseJson
                else -> throw UnsupportedOperationException(fullPath)
            },
            status = HttpStatusCode.OK,
            headers = headersOf(HttpHeaders.ContentType, "application/json")
        )
    }
    config.requestHandlers.addAll(oldHandlers)
}
