package cinescout.watchlist.data.remote.mock

import cinescout.network.testutil.hasValidAccessToken
import cinescout.network.testutil.respondJson
import cinescout.network.testutil.respondUnauthorized
import cinescout.network.testutil.unhandled
import cinescout.network.trakt.model.TraktExtended
import cinescout.network.trakt.model.TraktQueryType
import cinescout.network.trakt.model.getExtendedParameter
import cinescout.watchlist.data.remote.res.TraktWatchlistExtendedJson
import cinescout.watchlist.data.remote.res.TraktWatchlistMetadataJson
import io.ktor.client.engine.mock.MockEngine
import io.ktor.http.Url
import io.ktor.http.fullPath

fun TraktWatchlistMockEngine(forceLoggedIn: Boolean = false) = MockEngine { requestData ->
    when (forceLoggedIn || requestData.hasValidAccessToken()) {
        true -> respondJson(getContent(requestData.url))
        false -> respondUnauthorized()
    }
}

private fun getContent(url: Url): String {
    val extended = url.getExtendedParameter()
    val fullPath = url.fullPath
    return when {
        "sync/watchlist/${TraktQueryType.All}" in fullPath -> when (extended) {
            TraktExtended.Full -> TraktWatchlistExtendedJson.OneMovieAndOneTvShow
            TraktExtended.Metadata -> TraktWatchlistMetadataJson.OneMovieAndOneTvShow
        }
        "sync/watchlist/${TraktQueryType.Movies}" in fullPath -> when (extended) {
            TraktExtended.Full -> TraktWatchlistExtendedJson.OneMovie
            TraktExtended.Metadata -> TraktWatchlistMetadataJson.OneMovie
        }
        "sync/watchlist/${TraktQueryType.TvShows}" in fullPath -> when (extended) {
            TraktExtended.Full -> TraktWatchlistExtendedJson.OneTvShow
            TraktExtended.Metadata -> TraktWatchlistMetadataJson.OneTvShow
        }
        else -> unhandled(url)
    }
}
