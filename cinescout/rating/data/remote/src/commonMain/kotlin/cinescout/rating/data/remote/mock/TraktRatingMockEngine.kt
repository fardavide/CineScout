package cinescout.rating.data.remote.mock

import cinescout.network.testutil.hasValidAccessToken
import cinescout.network.testutil.respondJson
import cinescout.network.testutil.respondUnauthorized
import cinescout.network.testutil.unhandled
import cinescout.network.trakt.model.TraktExtended
import cinescout.network.trakt.model.TraktQueryType
import cinescout.network.trakt.model.getExtendedParameter
import cinescout.rating.data.remote.res.TraktRatingExtendedJson
import cinescout.rating.data.remote.res.TraktRatingMetadataJson
import io.ktor.client.engine.mock.MockEngine
import io.ktor.http.Url
import io.ktor.http.fullPath

fun TraktRatingMockEngine(forceLoggedIn: Boolean = false) = MockEngine { requestData ->
    val json = getContent(requestData.url)
    when (forceLoggedIn || requestData.hasValidAccessToken()) {
        true -> respondJson(json)
        false -> respondUnauthorized()
    }
}

private fun getContent(url: Url): String {
    val extended = url.getExtendedParameter()
    val fullPath = url.fullPath
    return when {
        "sync/ratings/${TraktQueryType.All}" in fullPath -> when (extended) {
            TraktExtended.Full -> TraktRatingExtendedJson.OneMovieAndOneTvShow
            TraktExtended.Metadata -> TraktRatingMetadataJson.OneMovieAndOneTvShow
        }
        "sync/ratings/${TraktQueryType.Movies}" in fullPath -> when (extended) {
            TraktExtended.Full -> TraktRatingExtendedJson.OneMovie
            TraktExtended.Metadata -> TraktRatingMetadataJson.OneMovie
        }
        "sync/ratings/${TraktQueryType.TvShows}" in fullPath -> when (extended) {
            TraktExtended.Full -> TraktRatingExtendedJson.OneTvShow
            TraktExtended.Metadata -> TraktRatingMetadataJson.OneTvShow
        }
        else -> unhandled(url)
    }
}
