package cinescout.home.presentation.viewmodel

import app.cash.turbine.test
import cinescout.FakeGetAppVersion
import cinescout.account.domain.model.Account
import cinescout.account.domain.sample.AccountSample
import cinescout.account.domain.usecase.FakeGetCurrentAccount
import cinescout.design.FakeNetworkErrorToMessageMapper
import cinescout.design.model.ConnectionStatusUiModel
import cinescout.home.presentation.sample.HomeStateSample
import cinescout.home.presentation.state.HomeState
import cinescout.network.model.ConnectionStatus
import cinescout.network.sample.ConnectionStatusSample
import cinescout.network.usecase.FakeObserveConnectionStatus
import cinescout.test.android.ViewModelExtension
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.core.test.testCoroutineScheduler
import io.kotest.matchers.shouldBe

class HomeViewModelTest : BehaviorSpec({
    extension(ViewModelExtension())

    Given("view model") {

        When("started") {
            val scenario = TestScenario()

            Then("is loading") {
                scenario.sut.state.test {
                    awaitItem() shouldBe HomeState.Loading
                    cancelAndIgnoreRemainingEvents()
                }
            }
        }

        When("account connected") {
            val tmdbAccount = AccountSample.Tmdb
            val scenario = TestScenario(account = tmdbAccount)

            Then("account is emitted") {
                testCoroutineScheduler.advanceUntilIdle()
                scenario.sut.state.test {
                    awaitItem().account shouldBe HomeStateSample.TmdbAccount
                    cancelAndIgnoreRemainingEvents()
                }
            }
        }

        When("no account connected") {
            val scenario = TestScenario(account = null)

            Then("no connected account is emitted") {
                testCoroutineScheduler.advanceUntilIdle()
                scenario.sut.state.test {
                    awaitItem().account shouldBe HomeState.Account.NotConnected
                    cancelAndIgnoreRemainingEvents()
                }
            }
        }

        When("device offline") {
            val scenario = TestScenario(connectionStatus = ConnectionStatus.AllOffline)

            Then("connection status is emitted") {
                testCoroutineScheduler.advanceUntilIdle()
                scenario.sut.state.test {
                    awaitItem().connectionStatus shouldBe ConnectionStatusUiModel.DeviceOffline
                    cancelAndIgnoreRemainingEvents()
                }
            }
        }

        When("Tmdb offline") {
            val scenario = TestScenario(connectionStatus = ConnectionStatusSample.TmdbOffline)

            Then("connection status is emitted") {
                testCoroutineScheduler.advanceUntilIdle()
                scenario.sut.state.test {
                    awaitItem().connectionStatus shouldBe ConnectionStatusUiModel.TmdbOffline
                    cancelAndIgnoreRemainingEvents()
                }
            }
        }

        When("Trakt offline") {
            val scenario = TestScenario(connectionStatus = ConnectionStatusSample.TraktOffline)

            Then("connection status is emitted") {
                testCoroutineScheduler.advanceUntilIdle()
                scenario.sut.state.test {
                    awaitItem().connectionStatus shouldBe ConnectionStatusUiModel.TraktOffline
                    cancelAndIgnoreRemainingEvents()
                }
            }
        }
    }
})

private class HomeViewModelTestScenario(
    val sut: HomeViewModel
)

private fun TestScenario(
    connectionStatus: ConnectionStatus = ConnectionStatus.AllOnline,
    account: Account.Tmdb? = null
): HomeViewModelTestScenario {
    return HomeViewModelTestScenario(
        sut = HomeViewModel(
            getAppVersion = FakeGetAppVersion(),
            getCurrentAccount = FakeGetCurrentAccount(account = account),
            networkErrorMapper = FakeNetworkErrorToMessageMapper(),
            observeConnectionStatus = FakeObserveConnectionStatus(connectionStatus = connectionStatus)
        )
    )
}
