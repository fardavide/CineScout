package cinescout.tvshows.data.remote.tmdb.testutil

import cinescout.network.tmdb.testutil.TmdbGenericJson
import cinescout.tvshows.domain.model.TmdbTvShowId
import cinescout.tvshows.domain.testdata.TmdbTvShowIdTestData
import io.ktor.client.engine.mock.*
import io.ktor.http.*

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
        "rated/tv" in fullPath -> TODO("TmdbTvShowsRatingJson.OneTvShow")
        "/${TmdbTvShowIdTestData.Grimm.value}/keywords" in fullPath -> TmdbTvShowKeywordsJson.Grimm
        "rating" in fullPath -> TmdbGenericJson.EmptySuccess
        "recommendations" in fullPath -> TODO("TmdbTvShowRecommendationsJson.TwoTvShows")
        "watchlist/tv" in fullPath && method == HttpMethod.Get -> TmdbTvShowsWatchlistJson.OneTvShow
        "watchlist/tv" in fullPath && method == HttpMethod.Post -> TmdbGenericJson.EmptySuccess
        "/${TmdbTvShowIdTestData.Grimm.value}/credits" in fullPath -> TmdbTvShowCreditsJson.Grimm
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

