package cinescout.home.presentation.viewmodel

import app.cash.turbine.test
import arrow.core.Either
import arrow.core.left
import arrow.core.right
import cinescout.FakeGetAppVersion
import cinescout.account.tmdb.domain.model.TmdbAccount
import cinescout.account.tmdb.domain.sample.TmdbAccountSample
import cinescout.account.tmdb.domain.usecase.FakeGetTmdbAccount
import cinescout.account.trakt.domain.model.TraktAccount
import cinescout.account.trakt.domain.sample.TraktAccountSample
import cinescout.account.trakt.domain.usecase.FakeGetTraktAccount
import cinescout.auth.tmdb.domain.usecase.FakeLinkToTmdb
import cinescout.auth.tmdb.domain.usecase.FakeNotifyTmdbAppAuthorized
import cinescout.auth.tmdb.domain.usecase.FakeUnlinkFromTmdb
import cinescout.auth.tmdb.domain.usecase.LinkToTmdb
import cinescout.auth.trakt.domain.sample.TraktAuthorizationCodeSample
import cinescout.auth.trakt.domain.usecase.FakeLinkToTrakt
import cinescout.auth.trakt.domain.usecase.FakeNotifyTraktAppAuthorized
import cinescout.auth.trakt.domain.usecase.LinkToTrakt
import cinescout.design.FakeNetworkErrorToMessageMapper
import cinescout.design.R.string
import cinescout.design.TextRes
import cinescout.design.model.ConnectionStatusUiModel
import cinescout.home.presentation.model.HomeAction
import cinescout.home.presentation.model.HomeState
import cinescout.home.presentation.sample.HomeStateSample
import cinescout.network.model.ConnectionStatus
import cinescout.network.sample.ConnectionStatusSample
import cinescout.network.usecase.FakeObserveConnectionStatus
import cinescout.suggestions.domain.usecase.FakeStartUpdateSuggestions
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

    Given("logging in to Tmdb") {

        When("started") {
            val scenario = TestScenario()
            scenario.sut.submit(HomeAction.LoginToTmdb)

            Then("link is called") {
                testCoroutineScheduler.advanceUntilIdle()
                scenario.linkToTmdb.invoked shouldBe true
            }
        }

        When("token authorized") {
            val scenario = TestScenario()
            scenario.sut.submit(HomeAction.NotifyTmdbAppAuthorized)
            testCoroutineScheduler.advanceUntilIdle()

            Then("notify is called") {
                scenario.notifyTmdbAppAuthorized.invoked shouldBe true
            }
        }

        When("token not authorized") {
            val expectedMessage = TextRes(string.home_login_app_not_authorized)
            val linkResult = LinkToTmdb.Error.UserDidNotAuthorizeToken.left()
            val scenario = TestScenario(linkToTmdbResult = linkResult)
            scenario.sut.submit(HomeAction.LoginToTmdb)
            testCoroutineScheduler.advanceUntilIdle()

            Then("error is emitted") {
                scenario.sut.state.test {
                    awaitItem().loginEffect.consume() shouldBe HomeState.Login.Error(expectedMessage)
                }
            }
        }

        When("completed") {
            val scenario = TestScenario()
            scenario.sut.submit(HomeAction.LoginToTmdb)

            Then("suggestions are updated") {
                testCoroutineScheduler.advanceUntilIdle()
                scenario.startUpdateSuggestions.invoked shouldBe true
            }
        }
    }

    Given("logging in to Trakt") {

        When("started") {
            val scenario = TestScenario()
            scenario.sut.submit(HomeAction.LoginToTrakt)

            Then("link is called") {
                testCoroutineScheduler.advanceUntilIdle()
                scenario.linkToTrakt.invoked shouldBe true
            }
        }

        When("app authorized") {
            val authorizationCode = TraktAuthorizationCodeSample.AuthorizationCode
            val scenario = TestScenario()
            scenario.sut.submit(HomeAction.NotifyTraktAppAuthorized(authorizationCode))
            testCoroutineScheduler.advanceUntilIdle()

            Then("notify is called") {
                scenario.notifyTraktAppAuthorized.invokedWithAuthorizationCode shouldBe authorizationCode
            }
        }

        When("app not authorized") {
            val expectedMessage = TextRes(string.home_login_app_not_authorized)
            val linkResult = LinkToTrakt.Error.UserDidNotAuthorizeApp.left()
            val scenario = TestScenario(linkToTraktResult = linkResult)
            scenario.sut.submit(HomeAction.LoginToTrakt)
            testCoroutineScheduler.advanceUntilIdle()

            Then("error is emitted") {
                scenario.sut.state.test {
                    awaitItem().loginEffect.consume() shouldBe HomeState.Login.Error(expectedMessage)
                }
            }
        }

        When("completed") {
            val scenario = TestScenario()
            scenario.sut.submit(HomeAction.LoginToTrakt)

            Then("suggestions are updated") {
                testCoroutineScheduler.advanceUntilIdle()
                scenario.startUpdateSuggestions.invoked shouldBe true
            }
        }
    }

    Given("logging out from Tmdb") {

        When("started") {
            val scenario = TestScenario()
            scenario.sut.submit(HomeAction.LogoutFromTmdb)

            Then("unlink is called") {
                testCoroutineScheduler.advanceUntilIdle()
                scenario.unlinkFromTmdb.invoked shouldBe true
            }
        }
    }
})

private class TestScenario(
    val sut: HomeViewModel,
    val linkToTmdb: FakeLinkToTmdb,
    val linkToTrakt: FakeLinkToTrakt,
    val notifyTmdbAppAuthorized: FakeNotifyTmdbAppAuthorized,
    val notifyTraktAppAuthorized: FakeNotifyTraktAppAuthorized,
    val startUpdateSuggestions: FakeStartUpdateSuggestions,
    val unlinkFromTmdb: FakeUnlinkFromTmdb
)

private fun TestScenario(
    connectionStatus: ConnectionStatus = ConnectionStatus.AllOnline,
    linkToTmdbResult: Either<LinkToTmdb.Error, LinkToTmdb.State> = LinkToTmdb.State.Success.right(),
    linkToTraktResult: Either<LinkToTrakt.Error, LinkToTrakt.State> = LinkToTrakt.State.Success.right(),
    tmdbAccount: TmdbAccount? = null,
    traktAccount: TraktAccount? = null
): TestScenario {
    val fakeLinkToTmdb = FakeLinkToTmdb(result = linkToTmdbResult)
    val fakeLinkToTrakt = FakeLinkToTrakt(result = linkToTraktResult)
    val fakeNotifyTmdbAppAuthorized = FakeNotifyTmdbAppAuthorized()
    val fakeNotifyTraktAppAuthorized = FakeNotifyTraktAppAuthorized()
    val startUpdateSuggestions = FakeStartUpdateSuggestions()
    val unlinkFromTmdb = FakeUnlinkFromTmdb()
    return TestScenario(
        sut = HomeViewModel(
            getAppVersion = FakeGetAppVersion(),
            getTmdbAccount = FakeGetTmdbAccount(account = tmdbAccount),
            getTraktAccount = FakeGetTraktAccount(account = traktAccount),
            linkToTmdb = fakeLinkToTmdb,
            linkToTrakt = fakeLinkToTrakt,
            networkErrorMapper = FakeNetworkErrorToMessageMapper(),
            notifyTmdbAppAuthorized = fakeNotifyTmdbAppAuthorized,
            notifyTraktAppAuthorized = fakeNotifyTraktAppAuthorized,
            observeConnectionStatus = FakeObserveConnectionStatus(connectionStatus = connectionStatus),
            startUpdateSuggestions = startUpdateSuggestions,
            unlinkFromTmdb = unlinkFromTmdb
        ),
        linkToTmdb = fakeLinkToTmdb,
        linkToTrakt = fakeLinkToTrakt,
        notifyTmdbAppAuthorized = fakeNotifyTmdbAppAuthorized,
        notifyTraktAppAuthorized = fakeNotifyTraktAppAuthorized,
        startUpdateSuggestions = startUpdateSuggestions,
        unlinkFromTmdb = unlinkFromTmdb
    )
}
