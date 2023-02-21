package cinescout.home.presentation.viewmodel

import app.cash.turbine.test
import cinescout.FakeGetAppVersion
import cinescout.account.domain.model.Account
import cinescout.account.tmdb.domain.sample.TmdbAccountSample
import cinescout.account.tmdb.domain.usecase.FakeGetTmdbAccount
import cinescout.account.trakt.domain.sample.TraktAccountSample
import cinescout.account.trakt.domain.usecase.FakeGetTraktAccount
import cinescout.design.FakeNetworkErrorToMessageMapper
import cinescout.design.model.ConnectionStatusUiModel
import cinescout.home.presentation.sample.HomeStateSample
import cinescout.home.presentation.state.HomeState
import cinescout.network.model.ConnectionStatus
import cinescout.network.sample.ConnectionStatusSample
import cinescout.network.usecase.FakeObserveConnectionStatus
import cinescout.test.android.ViewModelTestListener
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.core.test.testCoroutineScheduler
import io.kotest.matchers.shouldBe

class HomeViewModelTest : BehaviorSpec({
    extension(ViewModelTestListener())

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

        When("Tmdb account") {
            val tmdbAccount = TmdbAccountSample.Account
            val scenario = TestScenario(tmdbAccount = tmdbAccount)

            Then("account is emitted") {
                testCoroutineScheduler.advanceUntilIdle()
                scenario.sut.state.test {
                    awaitItem().accounts.tmdb shouldBe HomeStateSample.TmdbAccount
                    cancelAndIgnoreRemainingEvents()
                }
            }
        }

        When("Trakt account") {
            val traktAccount = TraktAccountSample.Account
            val scenario = TestScenario(traktAccount = traktAccount)

            Then("account is emitted") {
                testCoroutineScheduler.advanceUntilIdle()
                scenario.sut.state.test {
                    awaitItem().accounts.trakt shouldBe HomeStateSample.TraktAccount
                    cancelAndIgnoreRemainingEvents()
                }
            }
        }

        When("no Tmdb account") {
            val scenario = TestScenario(tmdbAccount = null)

            Then("no connected account is emitted") {
                testCoroutineScheduler.advanceUntilIdle()
                scenario.sut.state.test {
                    awaitItem().accounts.tmdb shouldBe HomeState.Accounts.Account.NoAccountConnected
                    cancelAndIgnoreRemainingEvents()
                }
            }
        }

        When("no Trakt account") {
            val scenario = TestScenario(traktAccount = null)

            Then("no connected account is emitted") {
                testCoroutineScheduler.advanceUntilIdle()
                scenario.sut.state.test {
                    awaitItem().accounts.trakt shouldBe HomeState.Accounts.Account.NoAccountConnected
                    cancelAndIgnoreRemainingEvents()
                }
            }
        }

        When("no account connected") {
            val scenario = TestScenario(tmdbAccount = null, traktAccount = null)

            Then("no connected account is emitted") {
                testCoroutineScheduler.advanceUntilIdle()
                scenario.sut.state.test {
                    awaitItem().accounts shouldBe HomeState.Accounts.NoAccountConnected
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
    tmdbAccount: Account.Tmdb? = null,
    traktAccount: Account.Trakt? = null
): HomeViewModelTestScenario {
    return HomeViewModelTestScenario(
        sut = HomeViewModel(
            getAppVersion = FakeGetAppVersion(),
            getTmdbAccount = FakeGetTmdbAccount(account = tmdbAccount),
            getTraktAccount = FakeGetTraktAccount(account = traktAccount),
            networkErrorMapper = FakeNetworkErrorToMessageMapper(),
            observeConnectionStatus = FakeObserveConnectionStatus(connectionStatus = connectionStatus)
        )
    )
}
