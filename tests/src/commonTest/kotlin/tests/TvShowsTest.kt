package tests

import app.cash.turbine.test
import arrow.core.right
import cinescout.account.tmdb.data.remote.testutil.MockTmdbAccountEngine
import cinescout.account.trakt.data.remote.testutil.MockTraktAccountEngine
import cinescout.auth.tmdb.data.remote.testutil.MockTmdbAuthEngine
import cinescout.auth.trakt.data.remote.testutil.MockTraktAuthEngine
import cinescout.network.testutil.plus
import cinescout.network.tmdb.CineScoutTmdbV3Client
import cinescout.network.tmdb.CineScoutTmdbV4Client
import cinescout.network.tmdb.TmdbNetworkQualifier
import cinescout.network.trakt.CineScoutTraktClient
import cinescout.network.trakt.TraktNetworkQualifier
import cinescout.tvshows.data.remote.tmdb.testutil.MockTmdbTvShowEngine
import cinescout.tvshows.data.remote.trakt.testutil.MockTraktTvShowEngine
import cinescout.tvshows.domain.testdata.TvShowTestData
import cinescout.tvshows.domain.usecase.GetAllWatchlistTvShows
import kotlinx.coroutines.test.runTest
import org.koin.dsl.module
import org.koin.test.inject
import store.builder.dualSourcesPagedDataOf
import util.BaseAppTest
import util.BaseTmdbTest
import util.BaseTraktTest
import kotlin.test.Test
import kotlin.test.assertEquals

class TvShowsTest : BaseAppTest(), BaseTmdbTest, BaseTraktTest {

    // private val getAllRatedTvShows: GetAllRatedTvShows by inject()
    private val getAllWatchlistTvShows: GetAllWatchlistTvShows by inject()
    // private val getTvShowDetails: GetTvShowDetails by inject()
    // private val generateSuggestedTvShows: GenerateSuggestedTvShows by inject()
    // private val rateTvShow: RateTvShow by inject()

    private val tmdbTvShowEngine = MockTmdbTvShowEngine()

    override val extraModule = module {
        factory(TmdbNetworkQualifier.V3.Client) {
            CineScoutTmdbV3Client(
                engine = MockTmdbAccountEngine() + MockTmdbAuthEngine() + tmdbTvShowEngine,
                authProvider = get()
            )
        }
        factory(TmdbNetworkQualifier.V4.Client) {
            CineScoutTmdbV4Client(
                engine = MockTmdbAuthEngine(),
                authProvider = get()
            )
        }
        factory(TraktNetworkQualifier.Client) {
            CineScoutTraktClient(
                engine = MockTraktAccountEngine() + MockTraktAuthEngine() + MockTraktTvShowEngine(),
                authProvider = get()
            )
        }
        // factory { StartUpdateSuggestedTvShows {} }
    }

//    @Test
//    fun `get all rated movies`() = runTest {
//        // given
//        val expected = dualSourcesPagedDataOf(TvShowWithPersonalRatingTestData.Grimm).right()
//        givenSuccessfullyLinkedToTmdb()
//        givenSuccessfullyLinkedToTrakt()
//
//        // when
//        getAllRatedTvShows().test {
//
//            // then
//            assertEquals(expected.map { it.data }, awaitItem().map { it.data })
//        }
//    }

    @Test
    fun `get all watchlist tv shows`() = runTest {
        // given
        val expected = dualSourcesPagedDataOf(TvShowTestData.Grimm).right()
        givenSuccessfullyLinkedToTmdb()
        givenSuccessfullyLinkedToTrakt()

        // when
        getAllWatchlistTvShows().test {

            // then
            assertEquals(expected.map { it.data }, awaitItem().map { it.data })
        }
    }

//    @Test
//    fun `get movie`() = runTest {
//        // given
//        val movie = TvShowWithDetailsTestData.Grimm
//
//        // when
//        val result = getTvShowDetails(TmdbTvShowIdTestData.Grimm).first()
//
//        // then
//        assertEquals(movie.right(), result)
//    }

//    @Test
//    fun `generate suggested movies`() = runTest(dispatchTimeoutMs = TestTimeout) {
//        // given
//        val expected = nonEmptyListOf(TvShowTestData.TheWolfOfWallStreet).right()
//        givenSuccessfullyLinkedToTmdb()
//        givenSuccessfullyLinkedToTrakt()
//
//        // when
//        val result = generateSuggestedTvShows(SuggestionsMode.Quick).first()
//
//        // then
//        assertEquals(expected, result)
//    }

//    @Test
//    fun `generate suggested movies completes with movie details with empty genres`() = runTest(
//        dispatchTimeoutMs = TestTimeout
//    ) {
//        // given
//        val expected = nonEmptyListOf(TvShowTestData.TheWolfOfWallStreet).right()
//        tmdbTvShowEngine.addTvShowDetailsHandler(
//            TmdbTvShowIdTestData.Inception,
//            TmdbTvShowDetailsJson.InceptionWithEmptyGenres
//        )
//        givenSuccessfullyLinkedToTmdb()
//        givenSuccessfullyLinkedToTrakt()
//
//        // when
//        val result = generateSuggestedTvShows(SuggestionsMode.Quick).first()
//
//        // then
//        assertEquals(expected, result)
//    }

//    @Test
//    fun `generate suggested movies completes with movie details without genres`() = runTest(
//        dispatchTimeoutMs = TestTimeout
//    ) {
//        // given
//        val expected = nonEmptyListOf(TvShowTestData.TheWolfOfWallStreet).right()
//        tmdbTvShowEngine.addTvShowDetailsHandler(
//            TmdbTvShowIdTestData.Inception,
//            TmdbTvShowDetailsJson.InceptionWithoutGenres
//        )
//        givenSuccessfullyLinkedToTmdb()
//        givenSuccessfullyLinkedToTrakt()
//
//        // when
//        val result = generateSuggestedTvShows(SuggestionsMode.Quick).first()
//
//        // then
//        assertEquals(expected, result)
//    }

//    @Test
//    fun `generate suggested movies completes with movie details without release date`() = runTest(
//        dispatchTimeoutMs = TestTimeout
//    ) {
//        // given
//        val expected = nonEmptyListOf(TvShowTestData.TheWolfOfWallStreet).right()
//        tmdbTvShowEngine.addTvShowDetailsHandler(
//            TmdbTvShowIdTestData.Inception,
//            TmdbTvShowDetailsJson.InceptionWithoutReleaseDate
//        )
//        givenSuccessfullyLinkedToTmdb()
//        givenSuccessfullyLinkedToTrakt()
//
//        // when
//        val result = generateSuggestedTvShows(SuggestionsMode.Quick).first()
//
//        // then
//        assertEquals(expected, result)
//    }

//    @Test
//    fun `rate movie`() = runTest {
//        // given
//        val expected = Unit.right()
//        val movieId = TvShowTestData.TheWolfOfWallStreet.tmdbId
//        givenSuccessfullyLinkedToTmdb()
//        givenSuccessfullyLinkedToTrakt()
//        Rating.of(8).tap { rating ->
//
//            // when
//            val result = rateTvShow(movieId, rating)
//
//            // then
//            assertEquals(expected, result)
//        }
//    }
}
