package cinescout.movies.data.remote.tmdb.testutil

import cinescout.movies.domain.model.TmdbMovieId
import cinescout.movies.domain.testdata.TmdbMovieIdTestData
import cinescout.network.testutil.setHandler
import cinescout.network.tmdb.testutil.TmdbGenericJson
import io.ktor.client.engine.mock.*
import io.ktor.http.*

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

        "/${TmdbMovieIdTestData.Inception.value}/credits" in fullPath -> TmdbMovieCreditsJson.Inception
        "/${TmdbMovieIdTestData.TheWolfOfWallStreet.value}/credits" in fullPath ->
            TmdbMovieCreditsJson.TheWolfOfWallStreet
        "/${TmdbMovieIdTestData.War.value}/credits" in fullPath -> TmdbMovieCreditsJson.War

        "/${TmdbMovieIdTestData.Inception.value}/images" in fullPath -> TmdbMovieImagesJson.Inception
        "/${TmdbMovieIdTestData.TheWolfOfWallStreet.value}/images" in fullPath ->
            TmdbMovieImagesJson.TheWolfOfWallStreet
        "/${TmdbMovieIdTestData.War.value}/images" in fullPath -> TmdbMovieImagesJson.War

        "/${TmdbMovieIdTestData.Inception.value}/keywords" in fullPath -> TmdbMovieKeywordsJson.Inception
        "/${TmdbMovieIdTestData.TheWolfOfWallStreet.value}/keywords" in fullPath ->
            TmdbMovieKeywordsJson.TheWolfOfWallStreet
        "/${TmdbMovieIdTestData.War.value}/keywords" in fullPath -> TmdbMovieKeywordsJson.War

        "/${TmdbMovieIdTestData.Inception.value}/videos" in fullPath -> TmdbMovieVideosJson.Inception
        "/${TmdbMovieIdTestData.TheWolfOfWallStreet.value}/videos" in fullPath ->
            TmdbMovieVideosJson.TheWolfOfWallStreet
        "/${TmdbMovieIdTestData.War.value}/videos" in fullPath -> TmdbMovieVideosJson.War

        TmdbMovieIdTestData.Inception.value.toString() == movieId -> TmdbMovieDetailsJson.Inception
        TmdbMovieIdTestData.TheWolfOfWallStreet.value.toString() == movieId -> TmdbMovieDetailsJson.TheWolfOfWallStreet
        TmdbMovieIdTestData.War.value.toString() == movieId -> TmdbMovieDetailsJson.War

        else -> throw UnsupportedOperationException(fullPath)
    }
}

fun MockEngine.addMovieDetailsHandler(movieId: TmdbMovieId, responseJson: String) {
    setHandler { requestData ->
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
