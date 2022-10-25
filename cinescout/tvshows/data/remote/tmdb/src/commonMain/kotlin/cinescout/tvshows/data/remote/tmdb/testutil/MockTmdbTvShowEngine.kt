package cinescout.tvshows.data.remote.tmdb.testutil

import cinescout.network.tmdb.testutil.TmdbGenericJson
import cinescout.tvshows.domain.model.TmdbTvShowId
import cinescout.tvshows.domain.testdata.TmdbTvShowIdTestData
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.Url
import io.ktor.http.fullPath
import io.ktor.http.headersOf

fun MockTmdbTvShowEngine() = MockEngine { requestData ->
    respond(
        content = getContent(requestData.method, requestData.url),
        status = HttpStatusCode.OK,
        headers = headersOf(HttpHeaders.ContentType, "application/json")
    )
}

private fun getContent(method: HttpMethod, url: Url): String {
    val fullPath = url.fullPath
    val tvShowId = fullPath.substringAfter("/tv/")
        .substringBefore("/")
        .substringBefore("?")
    return when {
        "discover/tv" in fullPath -> TODO("TmdbDiscoverTvShowsJson.TwoTvShows")
        "rated/tv" in fullPath -> TmdbTvShowsRatingJson.OneTvShow
        "rating" in fullPath -> TmdbGenericJson.EmptySuccess
        "recommendations" in fullPath && "tv" in fullPath -> TmdbTvShowRecommendationsJson.TwoTvShows
        "watchlist/tv" in fullPath && method == HttpMethod.Get -> TmdbTvShowsWatchlistJson.OneTvShow
        "watchlist/tv" in fullPath && method == HttpMethod.Post -> TmdbGenericJson.EmptySuccess

        "/${TmdbTvShowIdTestData.BreakingBad.value}/credits" in fullPath -> TmdbTvShowCreditsJson.BreakingBad
        "/${TmdbTvShowIdTestData.Dexter.value}/credits" in fullPath -> TmdbTvShowCreditsJson.Dexter
        "/${TmdbTvShowIdTestData.Grimm.value}/credits" in fullPath -> TmdbTvShowCreditsJson.Grimm

        "/${TmdbTvShowIdTestData.BreakingBad.value}/images" in fullPath -> TmdbTvShowImagesJson.BreakingBad
        "/${TmdbTvShowIdTestData.Dexter.value}/images" in fullPath -> TmdbTvShowImagesJson.Dexter
        "/${TmdbTvShowIdTestData.Grimm.value}/images" in fullPath -> TmdbTvShowImagesJson.Grimm

        "/${TmdbTvShowIdTestData.BreakingBad.value}/keywords" in fullPath -> TmdbTvShowKeywordsJson.BreakingBad
        "/${TmdbTvShowIdTestData.Dexter.value}/keywords" in fullPath -> TmdbTvShowKeywordsJson.Dexter
        "/${TmdbTvShowIdTestData.Grimm.value}/keywords" in fullPath -> TmdbTvShowKeywordsJson.Grimm

        "/${TmdbTvShowIdTestData.BreakingBad.value}/videos" in fullPath -> TmdbTvShowVideosJson.BreakingBad
        "/${TmdbTvShowIdTestData.Dexter.value}/videos" in fullPath -> TmdbTvShowVideosJson.Dexter
        "/${TmdbTvShowIdTestData.Grimm.value}/videos" in fullPath -> TmdbTvShowVideosJson.Grimm

        TmdbTvShowIdTestData.BreakingBad.value.toString() == tvShowId -> TmdbTvShowDetailsJson.BreakingBad
        TmdbTvShowIdTestData.Dexter.value.toString() == tvShowId -> TmdbTvShowDetailsJson.Dexter
        TmdbTvShowIdTestData.Grimm.value.toString() == tvShowId -> TmdbTvShowDetailsJson.Grimm

        else -> throw UnsupportedOperationException(fullPath)
    }
}

fun MockEngine.addTvShowDetailsHandler(movieId: TmdbTvShowId, responseJson: String) {
    val oldHandlers = config.requestHandlers + emptyList()
    config.requestHandlers.clear()
    config.addHandler { requestData ->
        val fullPath = requestData.url.fullPath
        val tvShowIdParam = fullPath.substringAfter("/tv/")
            .substringBefore("?")
        respond(
            content = when (tvShowIdParam) {
                movieId.value.toString() -> responseJson
                else -> throw UnsupportedOperationException(fullPath)
            },
            status = HttpStatusCode.OK,
            headers = headersOf(HttpHeaders.ContentType, "application/json")
        )
    }
    config.requestHandlers.addAll(oldHandlers)
}

