package cinescout.details.presentation.viewmodel

import app.cash.turbine.test
import cinescout.design.model.ConnectionStatusUiModel
import cinescout.details.presentation.state.TvShowDetailsState
import cinescout.network.model.ConnectionStatus
import cinescout.network.usecase.ObserveConnectionStatus
import cinescout.tvshows.domain.sample.TmdbTvShowIdSample
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

class TvShowDetailsViewModelTest {

    private val observeConnectionStatus: ObserveConnectionStatus = mockk {
        every { this@mockk() } returns flowOf(ConnectionStatus.AllOnline)
    }
    private val tvShowId = TmdbTvShowIdSample.Dexter
    private val viewModel by lazy {
        TvShowDetailsViewModel(
            addTvShowToWatchlist = mockk(),
            getTvShowExtras = mockk(),
            getTvShowMedia = mockk(),
            networkErrorToMessageMapper = mockk(),
            observeConnectionStatus = observeConnectionStatus,
            rateTvShow = mockk(),
            removeTvShowFromWatchlist = mockk(),
            tvShowDetailsUiModelMapper = mockk(),
            tvShowId = tvShowId
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
        val expected = TvShowDetailsState.Loading

        // when
        viewModel.state.test {

            // then
            assertEquals(expected, awaitItem())
        }
    }

    @Test
    fun `when network status change, status is emitted`() = runTest {
        // given
        val expected = TvShowDetailsState.Loading.copy(
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
