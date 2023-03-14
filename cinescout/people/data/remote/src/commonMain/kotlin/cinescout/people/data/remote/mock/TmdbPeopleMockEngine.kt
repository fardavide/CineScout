package cinescout.people.data.remote.mock

import cinescout.network.testutil.respondJson
import cinescout.people.data.remote.res.TmdbScreenplayCreditsJson
import cinescout.screenplay.domain.sample.TmdbScreenplayIdSample
import io.ktor.client.engine.mock.MockEngine
import io.ktor.http.Url
import io.ktor.http.fullPath

fun TmdbPeopleMockEngine() = MockEngine { requestData ->
    respondJson(getContent(requestData.url))
}

private fun getContent(url: Url): String {
    val fullPath = url.fullPath
    return when {
        "/${TmdbScreenplayIdSample.BreakingBad.value}/credits" in fullPath -> TmdbScreenplayCreditsJson.BreakingBad
        "/${TmdbScreenplayIdSample.Dexter.value}/credits" in fullPath -> TmdbScreenplayCreditsJson.Dexter
        "/${TmdbScreenplayIdSample.Grimm.value}/credits" in fullPath -> TmdbScreenplayCreditsJson.Grimm
        "/${TmdbScreenplayIdSample.Inception.value}/credits" in fullPath -> TmdbScreenplayCreditsJson.Inception
        "/${TmdbScreenplayIdSample.TheWolfOfWallStreet.value}/credits" in fullPath ->
            TmdbScreenplayCreditsJson.TheWolfOfWallStreet
        "/${TmdbScreenplayIdSample.War.value}/credits" in fullPath -> TmdbScreenplayCreditsJson.War

        else -> throw UnsupportedOperationException(fullPath)
    }
}
