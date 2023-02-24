package cinescout.movies.data.remote.tmdb.testutil

import cinescout.movies.domain.model.TmdbMovieId
import cinescout.movies.domain.sample.TmdbMovieIdSample
import cinescout.network.testutil.addHandler
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
        "discover/movie" in fullPath -> TmdbDiscoverMoviesJson.TwoMovies
        "rated/movies" in fullPath -> TmdbMoviesRatingJson.OneMovie
        "rating" in fullPath -> TmdbGenericJson.EmptySuccess
        "recommendations" in fullPath && "movie" in fullPath -> TmdbMovieRecommendationsJson.TwoMovies
        "watchlist" in fullPath && method == HttpMethod.Post -> TmdbGenericJson.EmptySuccess
        "watchlist/movies" in fullPath && method == HttpMethod.Get -> TmdbMoviesWatchlistJson.OneMovie

        "/${TmdbMovieIdSample.Inception.value}/credits" in fullPath -> TmdbMovieCreditsJson.Inception
        "/${TmdbMovieIdSample.TheWolfOfWallStreet.value}/credits" in fullPath ->
            TmdbMovieCreditsJson.TheWolfOfWallStreet
        "/${TmdbMovieIdSample.War.value}/credits" in fullPath -> TmdbMovieCreditsJson.War

        "/${TmdbMovieIdSample.Inception.value}/images" in fullPath -> TmdbMovieImagesJson.Inception
        "/${TmdbMovieIdSample.TheWolfOfWallStreet.value}/images" in fullPath ->
            TmdbMovieImagesJson.TheWolfOfWallStreet
        "/${TmdbMovieIdSample.War.value}/images" in fullPath -> TmdbMovieImagesJson.War

        "/${TmdbMovieIdSample.Inception.value}/keywords" in fullPath -> TmdbMovieKeywordsJson.Inception
        "/${TmdbMovieIdSample.TheWolfOfWallStreet.value}/keywords" in fullPath ->
            TmdbMovieKeywordsJson.TheWolfOfWallStreet
        "/${TmdbMovieIdSample.War.value}/keywords" in fullPath -> TmdbMovieKeywordsJson.War

        "/${TmdbMovieIdSample.Inception.value}/videos" in fullPath -> TmdbMovieVideosJson.Inception
        "/${TmdbMovieIdSample.TheWolfOfWallStreet.value}/videos" in fullPath ->
            TmdbMovieVideosJson.TheWolfOfWallStreet
        "/${TmdbMovieIdSample.War.value}/videos" in fullPath -> TmdbMovieVideosJson.War

        TmdbMovieIdSample.Inception.value.toString() == movieId -> TmdbMovieDetailsJson.Inception
        TmdbMovieIdSample.TheWolfOfWallStreet.value.toString() == movieId -> TmdbMovieDetailsJson.TheWolfOfWallStreet
        TmdbMovieIdSample.War.value.toString() == movieId -> TmdbMovieDetailsJson.War

        else -> throw UnsupportedOperationException(fullPath)
    }
}

fun MockEngine.addMovieDetailsHandler(movieId: TmdbMovieId, responseJson: String) {
    addHandler { requestData ->
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
}
