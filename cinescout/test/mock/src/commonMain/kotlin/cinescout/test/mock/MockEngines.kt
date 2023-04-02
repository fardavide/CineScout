package cinescout.test.mock

import cinescout.account.trakt.data.remote.testutil.TraktAccountMockEngine
import cinescout.auth.trakt.data.remote.testutil.TraktAuthMockEngine
import cinescout.media.data.remote.mock.TmdbMediaMockEngine
import cinescout.network.testutil.plus
import cinescout.people.data.remote.mock.TmdbPeopleMockEngine
import cinescout.rating.data.remote.mock.TraktRatingMockEngine
import cinescout.screenplay.data.remote.tmdb.mock.TmdbDetailsMockEngine
import cinescout.screenplay.data.remote.tmdb.mock.TmdbScreenplayMockEngine
import cinescout.watchlist.data.remote.mock.TraktWatchlistMockEngine
import io.ktor.client.engine.mock.MockEngine
import screenplay.data.remote.trakt.mock.TraktRecommendationMockEngine

object MockEngines {
    
    fun tmdb() = MockEngines(
        TmdbDetailsMockEngine(),
        TmdbMediaMockEngine(),
        TmdbPeopleMockEngine(),
        TmdbScreenplayMockEngine()
    )
    fun trakt() = MockEngines(
        TraktAccountMockEngine(),
        TraktAuthMockEngine(),
        TraktRatingMockEngine(),
        TraktRecommendationMockEngine(),
        TraktWatchlistMockEngine()
    )
}

private fun MockEngines(first: MockEngine, vararg others: MockEngine): MockEngine =
    others.fold(first) { acc, mockEngine ->
        acc + mockEngine
    }

object MockEngineQualifier {

    const val Tmdb = "Tmdb Mock Engine"
    const val Trakt = "Trakt Mock Engine"
}
