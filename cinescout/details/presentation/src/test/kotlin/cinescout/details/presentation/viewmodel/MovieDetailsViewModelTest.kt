package cinescout.details.presentation.viewmodel

import app.cash.turbine.test
import cinescout.design.model.ConnectionStatusUiModel
import cinescout.details.presentation.state.MovieDetailsState
import cinescout.movies.domain.sample.TmdbMovieIdSample
import cinescout.network.model.ConnectionStatus
import cinescout.network.usecase.ObserveConnectionStatus
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class MovieDetailsViewModelTest {

    private val movieId = TmdbMovieIdSample.Inception
    private val observeConnectionStatus: ObserveConnectionStatus = mockk {
        every { this@mockk() } returns flowOf(ConnectionStatus.AllOnline)
    }
    private val viewModel by lazy {
        MovieDetailsViewModel(
            addMovieToWatchlist = mockk(),
            movieDetailsUiModelMapper = mockk(),
            movieId = movieId,
            networkErrorToMessageMapper = mockk(),
            getMovieExtras = mockk(),
            getMovieMedia = mockk(),
            observeConnectionStatus = observeConnectionStatus,
            rateMovie = mockk(),
            removeMovieFromWatchlist = mockk()
        )
    }

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is loading`() = runTest {
        // given
        val expected = MovieDetailsState.Loading

        // when
        viewModel.state.test {

            // then
            assertEquals(expected, awaitItem())
        }
    }

    @Test
    fun `when network status change, status is emitted`() = runTest {
        // given
        val expected = MovieDetailsState.Loading.copy(
            connectionStatus = ConnectionStatusUiModel.DeviceOffline
        )
        every { observeConnectionStatus() } returns flowOf(ConnectionStatus.AllOffline)

        // when
        viewModel.also { advanceUntilIdle() }.state.test {

            // then
            assertEquals(expected, awaitItem())
        }
    }
}
