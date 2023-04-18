package cinescout.screenplay.data.remote.tmdb.mock

import cinescout.network.testutil.respondJson
import cinescout.screenplay.data.remote.tmdb.res.TmdbMovieDetailsJson
import cinescout.screenplay.data.remote.tmdb.res.TmdbTvShowDetailsJson
import cinescout.screenplay.domain.sample.TmdbScreenplayIdSample
import io.ktor.client.engine.mock.MockEngine
import io.ktor.http.Url
import io.ktor.http.fullPath

fun TmdbDetailsMockEngine() = MockEngine { requestData ->
    respondJson(getContent(requestData.url))
}

private fun getContent(url: Url): String {
    val fullPath = url.fullPath
    if ("append_to_response=images" in fullPath) {
        // images are handled by Media engine
        throw UnsupportedOperationException(fullPath)
    }
    val screenplayId = fullPath
        .substringAfter("/movie/")
        .substringAfter("/tv/")
        .substringBefore("?")
    return when (screenplayId) {
        TmdbScreenplayIdSample.BreakingBad.value.toString() -> TmdbTvShowDetailsJson.BreakingBad
        TmdbScreenplayIdSample.Dexter.value.toString() -> TmdbTvShowDetailsJson.Dexter
        TmdbScreenplayIdSample.Grimm.value.toString() -> TmdbTvShowDetailsJson.Grimm
        TmdbScreenplayIdSample.Inception.value.toString() -> TmdbMovieDetailsJson.Inception
        TmdbScreenplayIdSample.TheWolfOfWallStreet.value.toString() -> TmdbMovieDetailsJson.TheWolfOfWallStreet
        TmdbScreenplayIdSample.War.value.toString() -> TmdbMovieDetailsJson.War
        else -> throw UnsupportedOperationException(fullPath)
    }
}
