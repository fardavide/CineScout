package screenplay.data.remote.trakt.mock

import cinescout.network.testutil.addHandler
import cinescout.network.testutil.hasValidAccessToken
import cinescout.network.testutil.respondJson
import cinescout.network.testutil.respondUnauthorized
import cinescout.network.testutil.unhandled
import cinescout.network.trakt.model.toTraktQueryString
import cinescout.screenplay.domain.model.TraktScreenplayId
import cinescout.screenplay.domain.sample.TraktScreenplayIdSample
import io.ktor.client.engine.mock.MockEngine
import io.ktor.http.Url
import io.ktor.http.fullPath
import screenplay.data.remote.trakt.res.TraktExtendedScreenplayJson

fun TraktScreenplayMockEngine(forceLoggedIn: Boolean = false) = MockEngine { requestData ->
    val json = getContent(requestData.url)
    when (forceLoggedIn || requestData.hasValidAccessToken()) {
        true -> respondJson(json)
        false -> respondUnauthorized()
    }
}

private fun getContent(url: Url): String {
    val fullPath = url.fullPath
    return when {
        "movies/${TraktScreenplayIdSample.Avatar3.value}" in fullPath -> TraktExtendedScreenplayJson.Avatar3
        "movies/${TraktScreenplayIdSample.Inception.value}" in fullPath -> TraktExtendedScreenplayJson.Inception
        "shows/${TraktScreenplayIdSample.BreakingBad.value}/related" in fullPath -> "[]"
        "shows/${TraktScreenplayIdSample.BreakingBad.value}" in fullPath -> TraktExtendedScreenplayJson.BreakingBad
        "shows/${TraktScreenplayIdSample.Dexter.value}" in fullPath -> TraktExtendedScreenplayJson.Dexter
        "shows/${TraktScreenplayIdSample.TheWalkingDeadDeadCity.value}" in fullPath ->
            TraktExtendedScreenplayJson.TheWalkingDeadDeadCity
        else -> unhandled(url)
    }
}

fun MockEngine.addSimilarHandler(screenplayId: TraktScreenplayId, responseJson: String) {
    addHandler { requestData ->
        val fullPath = requestData.url.fullPath
        respondJson(
            content = when {
                "${screenplayId.toTraktQueryString()}/${screenplayId.value}/related" in fullPath -> responseJson
                else -> unhandled(requestData.url)
            }
        )
    }
}
