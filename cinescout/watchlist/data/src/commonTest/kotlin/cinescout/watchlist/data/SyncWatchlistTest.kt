package cinescout.watchlist.data

import app.cash.turbine.test
import arrow.core.right
import cinescout.fetchdata.domain.repository.FakeFetchDataRepository
import cinescout.screenplay.domain.model.Screenplay
import cinescout.screenplay.domain.model.ScreenplayTypeFilter
import cinescout.screenplay.domain.sample.ScreenplaySample
import cinescout.sync.domain.model.RequiredSync
import cinescout.watchlist.data.datasource.FakeLocalWatchlistDataSource
import cinescout.watchlist.data.datasource.FakeRemoteWatchlistDataSource
import cinescout.watchlist.data.mediator.RealSyncWatchlist
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.Flow

class SyncWatchlistTest : BehaviorSpec({

    Given("connected") {
        val isConnected = true

        And("type is all") {
            val type = ScreenplayTypeFilter.All

            When("call is success") {
                val scenario = TestScenario(
                    isConnected = isConnected,
                    watchlist = listOf(ScreenplaySample.Inception, ScreenplaySample.BreakingBad)
                )
                val result = scenario.sut(type, RequiredSync.Initial)

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
        }
    }

    Given("not connected") {
        val isConnected = false

        When("type is all") {
            val listType = ScreenplayTypeFilter.All
            val scenario = TestScenario(
                isConnected = isConnected,
                watchlist = listOf(ScreenplaySample.Inception, ScreenplaySample.BreakingBad)
            )
            val result = scenario.sut(listType, RequiredSync.Initial)

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
    val sut: RealSyncWatchlist,
    val cachedWatchlist: Flow<List<Screenplay>>
)

private fun TestScenario(isConnected: Boolean, watchlist: List<Screenplay>): SyncWatchlistTestScenario {
    val localDataSource = FakeLocalWatchlistDataSource()
    return SyncWatchlistTestScenario(
        sut = RealSyncWatchlist(
            fetchDataRepository = FakeFetchDataRepository(),
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
