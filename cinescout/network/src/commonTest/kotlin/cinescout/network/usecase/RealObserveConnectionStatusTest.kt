package cinescout.network.usecase

import app.cash.turbine.test
import arrow.core.left
import arrow.core.right
import cinescout.error.NetworkError
import cinescout.network.model.ConnectionStatus
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.testTimeSource
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds
import kotlin.time.measureTime

internal class RealObserveConnectionStatusTest {

    private val scheduler = TestCoroutineScheduler()
    private val appScope = TestScope(scheduler)
    private val ioDispatcher = StandardTestDispatcher(scheduler)
    private val observeNetworkStatusChanges: ObserveNetworkStatusChanges = mockk {
        every { this@mockk() } returns flowOf(Unit)
    }
    private val ping: Ping = mockk {
        coEvery { this@mockk(any()) } returns Unit.right()
    }
    private val observeConnectionStatus by lazy {
        RealObserveConnectionStatus(
            appScope = appScope,
            ioDispatcher = ioDispatcher,
            observeNetworkStatusChanges = observeNetworkStatusChanges,
            ping = ping
        )
    }

    @Test
    fun `emits right status, when all the hosts are online`() = runTest {
        // given
        val expected = ConnectionStatus.AllOnline
        coEvery { ping(Ping.Host.Google) } returns Unit.right()
        coEvery { ping(Ping.Host.Tmdb) } returns Unit.right()
        coEvery { ping(Ping.Host.Trakt) } returns Unit.right()

        // when
        observeConnectionStatus.flow.test {

            // then
            assertEquals(expected, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `pings are executed in parallel`() = runTest {
        // given
        val expected = ConnectionStatus.AllOnline
        val delay = 100.milliseconds
        coEvery { ping(Ping.Host.Google) } coAnswers {
            delay(delay)
            Unit.right()
        }
        coEvery { ping(Ping.Host.Tmdb) } coAnswers {
            delay(delay)
            Unit.right()
        }
        coEvery { ping(Ping.Host.Trakt) } coAnswers {
            delay(delay)
            Unit.right()
        }

        // when
        observeConnectionStatus.flow.test {

            // then
            val time = testTimeSource.measureTime {
                assertEquals(expected, awaitItem())
            }
            assertEquals(delay, time)
        }
    }

    @Test
    fun `when network status changes, pings are executed again`() = runTest {
        // given
        val networkStatusFlow = MutableSharedFlow<Unit>(replay = 1)
        networkStatusFlow.emit(Unit)
        every { observeNetworkStatusChanges() } returns networkStatusFlow
        var i = 0
        coEvery { ping(any()) } answers {
            listOf(
                Unit.right(),
                NetworkError.Unreachable.left()
            )[i++ % 2]
        }

        // when
        observeConnectionStatus.flow.test {
            val time = testTimeSource.measureTime {
                awaitItem()
                networkStatusFlow.emit(Unit)

                // then
                awaitItem()
                coVerify(exactly = 2) { ping(Ping.Host.Google) }
                coVerify(exactly = 2) { ping(Ping.Host.Tmdb) }
                coVerify(exactly = 2) { ping(Ping.Host.Trakt) }
            }
            assert(time < 30.seconds) { time }
        }
    }
}
