package cinescout.test.mock

import cinescout.CineScoutTestApi
import cinescout.account.trakt.data.remote.testutil.TraktAccountMockEngine
import cinescout.anticipated.data.remote.mock.TraktAnticipatedMockEngine
import cinescout.auth.trakt.data.remote.testutil.TraktAuthMockEngine
import cinescout.media.data.remote.mock.TmdbMediaMockEngine
import cinescout.network.testutil.plus
import cinescout.people.data.remote.mock.TmdbPeopleMockEngine
import cinescout.popular.data.remote.mock.TraktPopularMockEngine
import cinescout.rating.data.remote.mock.TraktRatingMockEngine
import cinescout.recommended.data.remote.mock.TraktRecommendedMockEngine
import cinescout.screenplay.data.remote.tmdb.mock.TmdbDetailsMockEngine
import cinescout.screenplay.data.remote.tmdb.mock.TmdbScreenplayMockEngine
import cinescout.search.data.remote.mock.TraktSearchMockEngine
import cinescout.seasons.data.remote.mock.TraktSeasonMockEngine
import cinescout.trending.data.remote.mock.TraktTrendingMockEngine
import cinescout.watchlist.data.remote.mock.TraktWatchlistMockEngine
import io.ktor.client.engine.mock.MockEngine
import screenplay.data.remote.trakt.mock.TraktRecommendationMockEngine
import screenplay.data.remote.trakt.mock.TraktScreenplayMockEngine
import screenplay.data.remote.trakt.mock.TraktUtilityMockEngine

@CineScoutTestApi
object MockEngines {
    
    fun tmdb() = MockEngines(
        TmdbDetailsMockEngine(),
        TmdbMediaMockEngine(),
        TmdbPeopleMockEngine(),
        TmdbScreenplayMockEngine()
    )
    fun trakt() = MockEngines(
        TraktAccountMockEngine(),
        TraktAnticipatedMockEngine(),
        TraktAuthMockEngine(),
        TraktPopularMockEngine(),
        TraktRatingMockEngine(),
        TraktRecommendationMockEngine(),
        TraktRecommendedMockEngine(),
        TraktScreenplayMockEngine(),
        TraktSearchMockEngine(),
        TraktSeasonMockEngine(),
        TraktTrendingMockEngine(),
        TraktUtilityMockEngine(),
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
