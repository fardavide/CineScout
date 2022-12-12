package tests

import app.cash.turbine.test
import arrow.core.nonEmptyListOf
import arrow.core.right
import cinescout.account.tmdb.data.remote.testutil.MockTmdbAccountEngine
import cinescout.account.trakt.data.remote.testutil.MockTraktAccountEngine
import cinescout.auth.tmdb.data.remote.testutil.MockTmdbAuthEngine
import cinescout.auth.trakt.data.remote.testutil.MockTraktAuthEngine
import cinescout.common.model.Rating
import cinescout.network.testutil.plus
import cinescout.network.tmdb.CineScoutTmdbV3Client
import cinescout.network.tmdb.CineScoutTmdbV4Client
import cinescout.network.tmdb.TmdbNetworkQualifier
import cinescout.network.trakt.CineScoutTraktClient
import cinescout.network.trakt.TraktNetworkQualifier
import cinescout.suggestions.domain.model.SuggestionsMode
import cinescout.suggestions.domain.usecase.GenerateSuggestedTvShows
import cinescout.test.kotlin.TestTimeout
import cinescout.test.mock.TestSqlDriverModule
import cinescout.tvshows.data.remote.tmdb.testutil.MockTmdbTvShowEngine
import cinescout.tvshows.data.remote.trakt.testutil.MockTraktTvShowEngine
import cinescout.tvshows.domain.testdata.TmdbTvShowIdTestData
import cinescout.tvshows.domain.testdata.TvShowTestData
import cinescout.tvshows.domain.testdata.TvShowWithDetailsTestData
import cinescout.tvshows.domain.testdata.TvShowWithPersonalRatingTestData
import cinescout.tvshows.domain.usecase.GetAllRatedTvShows
import cinescout.tvshows.domain.usecase.GetAllWatchlistTvShows
import cinescout.tvshows.domain.usecase.GetTvShowDetails
import cinescout.tvshows.domain.usecase.RateTvShow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.koin.test.inject
import store.builder.dualSourcesPagedDataOf
import util.BaseAppTest
import util.BaseTmdbTest
import util.BaseTraktTest
import kotlin.test.Test
import kotlin.test.assertEquals

class TvShowsTest : BaseAppTest(), BaseTmdbTest, BaseTraktTest {

    private val getAllRatedTvShows: GetAllRatedTvShows by inject()
    private val getAllWatchlistTvShows: GetAllWatchlistTvShows by inject()
    private val getTvShowDetails: GetTvShowDetails by inject()
    private val generateSuggestedTvShows: GenerateSuggestedTvShows by inject()
    private val rateTvShow: RateTvShow by inject()

    private val tmdbTvShowEngine = MockTmdbTvShowEngine()

    override val extraModule = module {
        includes(TestSqlDriverModule)

        single<CoroutineScope> { TestScope(context = UnconfinedTestDispatcher()) }
        factory(named(TmdbNetworkQualifier.V3.Client)) {
            CineScoutTmdbV3Client(
                engine = MockTmdbAccountEngine() + MockTmdbAuthEngine() + tmdbTvShowEngine,
                authProvider = get()
            )
        }
        factory(named(TmdbNetworkQualifier.V4.Client)) {
            CineScoutTmdbV4Client(
                engine = MockTmdbAuthEngine(),
                authProvider = get()
            )
        }
        factory(named(TraktNetworkQualifier.Client)) {
            CineScoutTraktClient(
                engine = MockTraktAccountEngine() + MockTraktAuthEngine() + MockTraktTvShowEngine(),
                authProvider = get()
            )
        }
        // factory { StartUpdateSuggestedTvShows {} }
    }

    @Test
    fun `get all rated tv shows`() = runTest {
        // given
        val expected = dualSourcesPagedDataOf(TvShowWithPersonalRatingTestData.Grimm).right()
        givenSuccessfullyLinkedToTmdb()
        givenSuccessfullyLinkedToTrakt()

        // when
        getAllRatedTvShows().test {

            // then
            assertEquals(expected.map { it.data }, awaitItem().map { it.data })
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `get all watchlist tv shows`() = runTest(dispatchTimeoutMs = TestTimeout) {
        // given
        val expected = dualSourcesPagedDataOf(TvShowTestData.Grimm).right()
        givenSuccessfullyLinkedToTmdb()
        givenSuccessfullyLinkedToTrakt()

        // when
        getAllWatchlistTvShows().test {

            // then
            assertEquals(expected.map { it.data }, awaitItem().map { it.data })
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `get tv show`() = runTest {
        // given
        val tvShow = TvShowWithDetailsTestData.Grimm

        // when
        val result = getTvShowDetails(TmdbTvShowIdTestData.Grimm).first()

        // then
        assertEquals(tvShow.right(), result)
    }

    @Test
    fun `generate suggested tv shows`() = runTest(dispatchTimeoutMs = TestTimeout) {
        // given
        val expected = nonEmptyListOf(TvShowTestData.BreakingBad).right()
        givenSuccessfullyLinkedToTmdb()
        givenSuccessfullyLinkedToTrakt()

        // when
        val result = generateSuggestedTvShows(SuggestionsMode.Quick).first()

        // then
        assertEquals(expected, result)
    }

    @Test
    fun `rate tv show`() = runTest {
        // given
        val expected = Unit.right()
        val tvShowId = TvShowTestData.Dexter.tmdbId
        givenSuccessfullyLinkedToTmdb()
        givenSuccessfullyLinkedToTrakt()
        Rating.of(8).tap { rating ->

            // when
            val result = rateTvShow(tvShowId, rating)

            // then
            assertEquals(expected, result)
        }
    }
}
