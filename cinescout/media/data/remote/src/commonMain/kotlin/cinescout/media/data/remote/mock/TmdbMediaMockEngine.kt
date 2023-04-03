package cinescout.media.data.remote.mock

import cinescout.media.data.remote.res.TmdbScreenplayImagesJson
import cinescout.media.data.remote.res.TmdbScreenplayVideosJson
import cinescout.network.testutil.respondJson
import cinescout.screenplay.domain.sample.TmdbScreenplayIdSample
import io.ktor.client.engine.mock.MockEngine
import io.ktor.http.Url
import io.ktor.http.fullPath

fun TmdbMediaMockEngine() = MockEngine { requestData ->
    respondJson(getContent(requestData.url))
}

private fun getContent(url: Url): String {
    val fullPath = url.fullPath
    return when {
        "/${TmdbScreenplayIdSample.BreakingBad.value}?append_to_response=images" in fullPath ||
            "/${TmdbScreenplayIdSample.BreakingBad.value}/images" in fullPath -> TmdbScreenplayImagesJson.BreakingBad
        "/${TmdbScreenplayIdSample.BreakingBad.value}/videos" in fullPath -> TmdbScreenplayVideosJson.BreakingBad

        "/${TmdbScreenplayIdSample.Dexter.value}?append_to_response=images" in fullPath ||
            "/${TmdbScreenplayIdSample.Dexter.value}/images" in fullPath -> TmdbScreenplayImagesJson.Dexter
        "/${TmdbScreenplayIdSample.Dexter.value}/videos" in fullPath -> TmdbScreenplayVideosJson.Dexter

        "/${TmdbScreenplayIdSample.Grimm.value}?append_to_response=images" in fullPath ||
            "/${TmdbScreenplayIdSample.Grimm.value}/images" in fullPath -> TmdbScreenplayImagesJson.Grimm
        "/${TmdbScreenplayIdSample.Grimm.value}/videos" in fullPath -> TmdbScreenplayVideosJson.Grimm

        "/${TmdbScreenplayIdSample.Inception.value}?append_to_response=images" in fullPath ||
            "/${TmdbScreenplayIdSample.Inception.value}/images" in fullPath -> TmdbScreenplayImagesJson.Inception
        "/${TmdbScreenplayIdSample.Inception.value}/videos" in fullPath -> TmdbScreenplayVideosJson.Inception

        "/${TmdbScreenplayIdSample.TheWolfOfWallStreet.value}/images" in fullPath ->
            TmdbScreenplayImagesJson.TheWolfOfWallStreet
        "/${TmdbScreenplayIdSample.TheWolfOfWallStreet.value}/videos" in fullPath ->
            TmdbScreenplayVideosJson.TheWolfOfWallStreet

        "/${TmdbScreenplayIdSample.War.value}?append_to_response=images" in fullPath ||
            "/${TmdbScreenplayIdSample.War.value}/images" in fullPath -> TmdbScreenplayImagesJson.War
        "/${TmdbScreenplayIdSample.War.value}/videos" in fullPath -> TmdbScreenplayVideosJson.War

        else -> throw UnsupportedOperationException(fullPath)
    }
}
