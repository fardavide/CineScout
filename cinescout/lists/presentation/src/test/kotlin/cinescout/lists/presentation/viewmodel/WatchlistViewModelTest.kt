package cinescout.lists.presentation.viewmodel

import app.cash.turbine.ReceiveTurbine
import app.cash.turbine.test
import cinescout.lists.presentation.model.WatchlistState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class WatchlistViewModelTest {

    private val dispatcher = StandardTestDispatcher()
    private val viewModel by lazy { WatchlistViewModel() }

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
    fun `emits empty watchlist`() = runTest(dispatcher) {
        // given
        val expected = WatchlistState.Data.Empty

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
