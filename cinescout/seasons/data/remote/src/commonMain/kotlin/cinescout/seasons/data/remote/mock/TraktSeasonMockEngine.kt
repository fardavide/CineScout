package cinescout.seasons.data.remote.mock

import cinescout.CineScoutTestApi
import cinescout.network.testutil.respondJson
import cinescout.network.testutil.unhandled
import cinescout.notImplementedFake
import cinescout.screenplay.domain.sample.TraktScreenplayIdSample
import cinescout.seasons.data.remote.res.TraktExtendedSeasonsWithEpisodesJson
import io.ktor.client.engine.mock.MockEngine
import io.ktor.http.Url
import io.ktor.http.fullPath

@CineScoutTestApi
fun TraktSeasonMockEngine() = MockEngine { requestData ->
    respondJson(getContent(requestData.url))
}

@CineScoutTestApi
private fun getContent(url: Url): String {
    val fullPath = url.fullPath
    return when {
        "shows/${TraktScreenplayIdSample.BreakingBad.value}/seasons" in fullPath ->
            TraktExtendedSeasonsWithEpisodesJson.BreakingBad
        "shows/${TraktScreenplayIdSample.Dexter.value}/seasons" in fullPath -> notImplementedFake()
        "shows/${TraktScreenplayIdSample.Grimm.value}/seasons" in fullPath -> notImplementedFake()
        "shows/${TraktScreenplayIdSample.TheWalkingDeadDeadCity.value}/seasons" in fullPath -> notImplementedFake()
        else -> unhandled(url)
    }
}
