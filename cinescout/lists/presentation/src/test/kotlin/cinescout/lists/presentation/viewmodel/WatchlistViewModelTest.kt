package cinescout.lists.presentation.viewmodel

import app.cash.turbine.ReceiveTurbine
import app.cash.turbine.test
import arrow.core.nonEmptyListOf
import cinescout.design.NetworkErrorToMessageMapper
import cinescout.design.testdata.MessageTextResTestData
import cinescout.lists.presentation.model.WatchlistItemUiModel
import cinescout.lists.presentation.model.WatchlistState
import cinescout.movies.domain.testdata.MovieTestData
import cinescout.movies.domain.testdata.TmdbMovieIdTestData
import cinescout.movies.domain.usecase.GetAllWatchlistMovies
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import store.builder.emptyPagedStore
import store.builder.pagedStoreOf
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class WatchlistViewModelTest {

    private val dispatcher = StandardTestDispatcher()
    private val errorToMessageMapper: NetworkErrorToMessageMapper = mockk {
        every { toMessage(any()) } returns MessageTextResTestData.NoNetworkError
    }
    private val getAllWatchlistMovies: GetAllWatchlistMovies = mockk {
        every { this@mockk(refresh = any()) } returns emptyPagedStore()
    }
    private val viewModel by lazy {
        WatchlistViewModel(errorToMessageMapper = errorToMessageMapper, getAllWatchlistMovies = getAllWatchlistMovies)
    }

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(dispatcher)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `emits loading at start`() = runTest(dispatcher) {
        // given
        val expected = WatchlistState.Loading

        // when
        viewModel.state.test {

            // then
            assertEquals(expected, awaitItem())
        }
    }

    @Test
    fun `emits empty watchlist when no movies in watchlist`() = runTest(dispatcher) {
        // given
        val expected = WatchlistState.Data.Empty
        every { getAllWatchlistMovies() } returns pagedStoreOf(emptyList())

        // when
        viewModel.state.test {
            givenLoadingEmitted()

            // then
            assertEquals(expected, awaitItem())
        }
    }

    @Test
    fun `emits movies from watchlist`() = runTest(dispatcher) {
        // given
        val models = nonEmptyListOf(
            WatchlistItemUiModel(
                tmdbId = TmdbMovieIdTestData.Inception,
                title = MovieTestData.Inception.title
            )
        )
        val expected = WatchlistState.Data.NotEmpty(models)
        every { getAllWatchlistMovies(refresh = any()) } returns pagedStoreOf(MovieTestData.Inception)

        // when
        viewModel.state.test {
            givenLoadingEmitted()

            // then
            assertEquals(expected, awaitItem())
        }
    }

    private suspend fun ReceiveTurbine<WatchlistState>.givenLoadingEmitted() {
        assertEquals(WatchlistState.Loading, awaitItem())
    }
}
