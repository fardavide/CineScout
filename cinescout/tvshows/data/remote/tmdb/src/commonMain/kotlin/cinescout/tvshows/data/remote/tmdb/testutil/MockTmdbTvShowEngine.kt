package cinescout.tvshows.data.remote.tmdb.testutil

import cinescout.network.tmdb.testutil.TmdbGenericJson
import cinescout.tvshows.domain.model.TmdbTvShowId
import cinescout.tvshows.domain.sample.TmdbTvShowIdSample
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.Url
import io.ktor.http.fullPath
import io.ktor.http.headersOf

fun MockTmdbTvShowEngine() = MockEngine { requestData ->
    respond(
        content = getContent(requestData.url),
        status = HttpStatusCode.OK,
        headers = headersOf(HttpHeaders.ContentType, "application/json")
    )
}

private fun getContent(url: Url): String {
    val fullPath = url.fullPath
    val tvShowId = fullPath.substringAfter("/tv/")
        .substringBefore("/")
        .substringBefore("?")
    return when {
        "discover/tv" in fullPath -> TODO("TmdbDiscoverTvShowsJson.TwoTvShows")
        "rating" in fullPath -> TmdbGenericJson.EmptySuccess
        "recommendations" in fullPath && "tv" in fullPath -> TmdbTvShowRecommendationsJson.TwoTvShows

        "/${TmdbTvShowIdSample.BreakingBad.value}/credits" in fullPath -> TmdbTvShowCreditsJson.BreakingBad
        "/${TmdbTvShowIdSample.Dexter.value}/credits" in fullPath -> TmdbTvShowCreditsJson.Dexter
        "/${TmdbTvShowIdSample.Grimm.value}/credits" in fullPath -> TmdbTvShowCreditsJson.Grimm

        "/${TmdbTvShowIdSample.BreakingBad.value}/images" in fullPath -> TmdbTvShowImagesJson.BreakingBad
        "/${TmdbTvShowIdSample.Dexter.value}/images" in fullPath -> TmdbTvShowImagesJson.Dexter
        "/${TmdbTvShowIdSample.Grimm.value}/images" in fullPath -> TmdbTvShowImagesJson.Grimm

        "/${TmdbTvShowIdSample.BreakingBad.value}/keywords" in fullPath -> TmdbTvShowKeywordsJson.BreakingBad
        "/${TmdbTvShowIdSample.Dexter.value}/keywords" in fullPath -> TmdbTvShowKeywordsJson.Dexter
        "/${TmdbTvShowIdSample.Grimm.value}/keywords" in fullPath -> TmdbTvShowKeywordsJson.Grimm

        "/${TmdbTvShowIdSample.BreakingBad.value}/videos" in fullPath -> TmdbTvShowVideosJson.BreakingBad
        "/${TmdbTvShowIdSample.Dexter.value}/videos" in fullPath -> TmdbTvShowVideosJson.Dexter
        "/${TmdbTvShowIdSample.Grimm.value}/videos" in fullPath -> TmdbTvShowVideosJson.Grimm

        TmdbTvShowIdSample.BreakingBad.value.toString() == tvShowId -> TmdbTvShowDetailsJson.BreakingBad
        TmdbTvShowIdSample.Dexter.value.toString() == tvShowId -> TmdbTvShowDetailsJson.Dexter
        TmdbTvShowIdSample.Grimm.value.toString() == tvShowId -> TmdbTvShowDetailsJson.Grimm

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

