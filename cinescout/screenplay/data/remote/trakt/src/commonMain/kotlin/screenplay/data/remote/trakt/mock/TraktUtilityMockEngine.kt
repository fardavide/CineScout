package screenplay.data.remote.trakt.mock

import cinescout.CineScoutTestApi
import cinescout.network.testutil.respondJson
import cinescout.network.testutil.unhandled
import io.ktor.client.engine.mock.MockEngine
import io.ktor.http.Url
import io.ktor.http.fullPath
import screenplay.data.remote.trakt.res.TraktGenreJson

@CineScoutTestApi
fun TraktUtilityMockEngine() = MockEngine { requestData ->
    val json = getContent(requestData.url)
    respondJson(json)
}

@CineScoutTestApi
private fun getContent(url: Url): String {
    val fullPath = url.fullPath
    return when {
        "genres/movies" in fullPath -> TraktGenreJson.Movies
        "genres/shows" in fullPath -> TraktGenreJson.TvShows
        else -> unhandled(url)
    }
}
