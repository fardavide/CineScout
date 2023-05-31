package screenplay.data.remote.trakt.mock

import cinescout.network.testutil.addHandler
import cinescout.network.testutil.respondJson
import cinescout.network.testutil.unhandled
import cinescout.network.trakt.model.toTraktQueryString
import cinescout.screenplay.domain.model.ids.TraktScreenplayId
import cinescout.screenplay.domain.sample.TraktScreenplayIdSample
import io.ktor.client.engine.mock.MockEngine
import io.ktor.http.Url
import io.ktor.http.fullPath
import screenplay.data.remote.trakt.res.TraktExtendedScreenplayJson

fun TraktScreenplayMockEngine() = MockEngine { requestData ->
    respondJson(getContent(requestData.url))
}

private fun getContent(url: Url): String {
    val fullPath = url.fullPath
    return when {
        "related" in fullPath -> "[]"
        "movies/${TraktScreenplayIdSample.Avatar3.value}" in fullPath -> TraktExtendedScreenplayJson.Avatar3
        "movies/${TraktScreenplayIdSample.Inception.value}" in fullPath -> TraktExtendedScreenplayJson.Inception
        "movies/${TraktScreenplayIdSample.War.value}" in fullPath -> TraktExtendedScreenplayJson.War
        "shows/${TraktScreenplayIdSample.BreakingBad.value}" in fullPath -> TraktExtendedScreenplayJson.BreakingBad
        "shows/${TraktScreenplayIdSample.Dexter.value}" in fullPath -> TraktExtendedScreenplayJson.Dexter
        "shows/${TraktScreenplayIdSample.Grimm.value}" in fullPath -> TraktExtendedScreenplayJson.Grimm
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
