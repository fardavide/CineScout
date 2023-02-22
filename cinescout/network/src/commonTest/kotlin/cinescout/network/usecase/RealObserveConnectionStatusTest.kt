package cinescout.network.usecase

import app.cash.turbine.test
import cinescout.network.model.ConnectionStatus
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.core.test.testCoroutineScheduler
import io.kotest.matchers.comparables.shouldBeLessThan
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.measureTime

class RealObserveConnectionStatusTest : BehaviorSpec({
    coroutineTestScope = true

    Given("all hosts are online") {

        When("observe connection status") {

            Then("emits all online status") {
                val scenario = TestScenario()
                scenario.sut().first() shouldBe ConnectionStatus.AllOnline
            }

            Then("pings are executed in parallel") {
                val duration = 100.milliseconds
                val scenario = TestScenario(pingDelay = duration)
                val time = testCoroutineScheduler.timeSource.measureTime {
                    scenario.sut().first() shouldBe ConnectionStatus.AllOnline
                }
                time shouldBe duration
            }
        }

        When("network status changes") {

            Then("pings are executed again") {
                val scenario = TestScenario(alternatePingFailures = true)
                scenario.sut().test {
                    val time = testCoroutineScheduler.timeSource.measureTime {
                        awaitItem()
                        scenario.networkStatusChanged()

                        // then
                        awaitItem()
                        scenario.ping.invocationCount(Ping.Host.Google) shouldBe 2
                        scenario.ping.invocationCount(Ping.Host.Tmdb) shouldBe 2
                        scenario.ping.invocationCount(Ping.Host.Trakt) shouldBe 2
                    }
                    time shouldBeLessThan ObserveConnectionStatus.DefaultInterval
                }
            }
        }
    }
})

private class RealObserveConnectionStatusTestScenario(
    val sut: ObserveConnectionStatus,
    val ping: FakePing,
    private val networkStatusFlow: MutableSharedFlow<Unit>
) {

    init {
        networkStatusFlow.tryEmit(Unit)
    }

    suspend fun networkStatusChanged() {
        networkStatusFlow.emit(Unit)
    }
}

private fun io.kotest.core.test.TestScope.TestScenario(
    pingDelay: Duration = Duration.ZERO,
    alternatePingFailures: Boolean = false
): RealObserveConnectionStatusTestScenario {
    val scheduler = testCoroutineScheduler
    val fakePing = FakePing(delay = pingDelay, alternateFailures = alternatePingFailures)
    val networkStatusFlow = MutableSharedFlow<Unit>(replay = 1)
    return RealObserveConnectionStatusTestScenario(
        sut = RealObserveConnectionStatus(
            appScope = TestScope(scheduler),
            ioDispatcher = StandardTestDispatcher(scheduler),
            observeNetworkStatusChanges = FakeObserveNetworkStatusChanges(flow = networkStatusFlow),
            ping = fakePing
        ),
        ping = fakePing,
        networkStatusFlow = networkStatusFlow
    )
}
