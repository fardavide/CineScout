package cinescout.network.usecase

import app.cash.turbine.test
import arrow.core.right
import cinescout.network.model.ConnectionStatus
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.testTimeSource
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.measureTime

internal class RealObserveConnectionStatusTest {

    private val scheduler = TestCoroutineScheduler()
    private val appScope = TestScope(scheduler)
    private val ioDispatcher = StandardTestDispatcher(scheduler)
    private val ping: Ping = mockk()
    private val observeConnectionStatus = RealObserveConnectionStatus(
        appScope = appScope,
        ioDispatcher = ioDispatcher,
        ping = ping
    )

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
}
