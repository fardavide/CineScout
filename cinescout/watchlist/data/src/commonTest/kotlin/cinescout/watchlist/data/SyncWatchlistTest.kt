package cinescout.watchlist.data

import app.cash.turbine.test
import arrow.core.left
import arrow.core.right
import cinescout.error.NetworkError
import cinescout.screenplay.domain.model.Screenplay
import cinescout.screenplay.domain.model.ScreenplayType
import cinescout.screenplay.domain.sample.ScreenplaySample
import cinescout.watchlist.data.datasource.FakeLocalWatchlistDataSource
import cinescout.watchlist.data.datasource.FakeRemoteWatchlistDataSource
import cinescout.watchlist.data.mediator.SyncWatchlist
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.Flow

class SyncWatchlistTest : BehaviorSpec({

    Given("connected") {
        val isConnected = true

        And("type is all") {
            val listType = ScreenplayType.All

            When("call is success") {
                val scenario = TestScenario(
                    isConnected = isConnected,
                    watchlist = listOf(ScreenplaySample.Inception, ScreenplaySample.BreakingBad)
                )
                val result = scenario.sut(listType, page = 1)

                Then("unit is returned") {
                    result shouldBe Unit.right()
                }

                And("items are inserted") {
                    scenario.cachedWatchlist.test {
                        awaitItem() shouldContainExactly listOf(
                            ScreenplaySample.Inception,
                            ScreenplaySample.BreakingBad
                        )
                    }
                }
            }

            When("call response is not found") {
                val scenario = TestScenario(
                    isConnected = isConnected,
                    watchlist = listOf(ScreenplaySample.Inception, ScreenplaySample.BreakingBad)
                )
                val result = scenario.sut(listType, page = 2)

                Then("not found is returned") {
                    result shouldBe NetworkError.NotFound.left()
                }

                And("items are not inserted") {
                    scenario.cachedWatchlist.test {
                        awaitItem().shouldBeEmpty()
                    }
                }
            }
        }
    }

    Given("not connected") {
        val isConnected = false

        When("type is all") {
            val listType = ScreenplayType.All
            val scenario = TestScenario(
                isConnected = isConnected,
                watchlist = listOf(ScreenplaySample.Inception, ScreenplaySample.BreakingBad)
            )
            val result = scenario.sut(listType, page = 1)

            Then("unit is returned") {
                result shouldBe Unit.right()
            }

            And("items are not inserted") {
                scenario.cachedWatchlist.test {
                    awaitItem().shouldBeEmpty()
                }
            }
        }
    }
})

private class SyncWatchlistTestScenario(
    val sut: SyncWatchlist,
    val cachedWatchlist: Flow<List<Screenplay>>
)

private fun TestScenario(isConnected: Boolean, watchlist: List<Screenplay>): SyncWatchlistTestScenario {
    val localDataSource = FakeLocalWatchlistDataSource()
    return SyncWatchlistTestScenario(
        sut = SyncWatchlist(
            localDataSource = localDataSource,
            remoteDataSource = FakeRemoteWatchlistDataSource(
                isConnected = isConnected,
                pageSize = 2,
                watchlist = watchlist
            )
        ),
        cachedWatchlist = localDataSource.watchlist
    )
}
