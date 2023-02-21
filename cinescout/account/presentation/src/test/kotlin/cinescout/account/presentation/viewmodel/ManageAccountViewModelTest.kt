package cinescout.account.presentation.viewmodel

import app.cash.turbine.test
import arrow.core.Either
import arrow.core.left
import arrow.core.right
import cinescout.account.domain.model.Account
import cinescout.account.domain.sample.AccountSample
import cinescout.account.domain.usecase.FakeGetCurrentAccount
import cinescout.account.presentation.action.ManageAccountAction
import cinescout.account.presentation.sample.ManageAccountStateSample
import cinescout.account.presentation.state.ManageAccountState
import cinescout.auth.tmdb.domain.usecase.FakeLinkToTmdb
import cinescout.auth.tmdb.domain.usecase.FakeNotifyTmdbAppAuthorized
import cinescout.auth.tmdb.domain.usecase.FakeUnlinkFromTmdb
import cinescout.auth.tmdb.domain.usecase.LinkToTmdb
import cinescout.auth.trakt.domain.sample.TraktAuthorizationCodeSample
import cinescout.auth.trakt.domain.usecase.FakeLinkToTrakt
import cinescout.auth.trakt.domain.usecase.FakeNotifyTraktAppAuthorized
import cinescout.auth.trakt.domain.usecase.FakeUnlinkFromTrakt
import cinescout.auth.trakt.domain.usecase.LinkToTrakt
import cinescout.design.FakeNetworkErrorToMessageMapper
import cinescout.design.R.string
import cinescout.design.TextRes
import cinescout.suggestions.domain.usecase.FakeStartUpdateSuggestions
import cinescout.test.android.ViewModelTestListener
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.core.test.testCoroutineScheduler
import io.kotest.matchers.shouldBe

class ManageAccountViewModelTest : BehaviorSpec({
    extension(ViewModelTestListener())

    Given("view model") {

        When("started") {
            val scenario = TestScenario()

            Then("should emit loading state") {
                scenario.sut.state.test {
                    awaitItem() shouldBe ManageAccountState.Loading
                }
            }
        }

        When("Tmdb account connected") {
            val tmdbAccount = AccountSample.Tmdb
            val scenario = TestScenario(account = tmdbAccount)

            Then("account is emitted") {
                testCoroutineScheduler.advanceUntilIdle()
                scenario.sut.state.test {
                    awaitItem().account shouldBe ManageAccountStateSample.Account.TmdbConnected
                    cancelAndIgnoreRemainingEvents()
                }
            }
        }

        When("Trakt account connected") {
            val tmdbAccount = AccountSample.Trakt
            val scenario = TestScenario(account = tmdbAccount)

            Then("account is emitted") {
                testCoroutineScheduler.advanceUntilIdle()
                scenario.sut.state.test {
                    awaitItem().account shouldBe ManageAccountStateSample.Account.TraktConnected
                    cancelAndIgnoreRemainingEvents()
                }
            }
        }

        When("no account connected") {
            val scenario = TestScenario(account = null)

            Then("no connected account is emitted") {
                testCoroutineScheduler.advanceUntilIdle()
                scenario.sut.state.test {
                    awaitItem().account shouldBe ManageAccountStateSample.Account.NotConnected
                    cancelAndIgnoreRemainingEvents()
                }
            }
        }
    }

    Given("logging in to Tmdb") {

        When("started") {
            val scenario = TestScenario()
            scenario.sut.submit(ManageAccountAction.LinkToTmdb)

            Then("link is called") {
                testCoroutineScheduler.advanceUntilIdle()
                scenario.linkToTmdb.invoked shouldBe true
            }
        }

        When("token authorized") {
            val scenario = TestScenario()
            scenario.sut.submit(ManageAccountAction.NotifyTmdbAppAuthorized)
            testCoroutineScheduler.advanceUntilIdle()

            Then("notify is called") {
                scenario.notifyTmdbAppAuthorized.invoked shouldBe true
            }
        }

        When("token not authorized") {
            val expectedMessage = TextRes(string.home_login_app_not_authorized)
            val linkResult = LinkToTmdb.Error.UserDidNotAuthorizeToken.left()
            val scenario = TestScenario(linkToTmdbResult = linkResult)
            scenario.sut.submit(ManageAccountAction.LinkToTmdb)
            testCoroutineScheduler.advanceUntilIdle()

            Then("error is emitted") {
                scenario.sut.state.test {
                    awaitItem().loginEffect.consume() shouldBe ManageAccountState.Login.Error(expectedMessage)
                }
            }
        }

        When("completed") {
            val scenario = TestScenario()
            scenario.sut.submit(ManageAccountAction.LinkToTmdb)

            Then("suggestions are updated") {
                testCoroutineScheduler.advanceUntilIdle()
                scenario.startUpdateSuggestions.invoked shouldBe true
            }
        }
    }

    Given("logging in to Trakt") {

        When("started") {
            val scenario = TestScenario()
            scenario.sut.submit(ManageAccountAction.LinkToTrakt)

            Then("link is called") {
                testCoroutineScheduler.advanceUntilIdle()
                scenario.linkToTrakt.invoked shouldBe true
            }
        }

        When("app authorized") {
            val authorizationCode = TraktAuthorizationCodeSample.AuthorizationCode
            val scenario = TestScenario()
            scenario.sut.submit(ManageAccountAction.NotifyTraktAppAuthorized(authorizationCode))
            testCoroutineScheduler.advanceUntilIdle()

            Then("notify is called") {
                scenario.notifyTraktAppAuthorized.invokedWithAuthorizationCode shouldBe authorizationCode
            }
        }

        When("app not authorized") {
            val expectedMessage = TextRes(string.home_login_app_not_authorized)
            val linkResult = LinkToTrakt.Error.UserDidNotAuthorizeApp.left()
            val scenario = TestScenario(linkToTraktResult = linkResult)
            scenario.sut.submit(ManageAccountAction.LinkToTrakt)
            testCoroutineScheduler.advanceUntilIdle()

            Then("error is emitted") {
                scenario.sut.state.test {
                    awaitItem().loginEffect.consume() shouldBe ManageAccountState.Login.Error(expectedMessage)
                }
            }
        }

        When("completed") {
            val scenario = TestScenario()
            scenario.sut.submit(ManageAccountAction.LinkToTrakt)

            Then("suggestions are updated") {
                testCoroutineScheduler.advanceUntilIdle()
                scenario.startUpdateSuggestions.invoked shouldBe true
            }
        }
    }

    Given("logging out from Tmdb") {

        When("started") {
            val scenario = TestScenario()
            scenario.sut.submit(ManageAccountAction.UnlinkFromTmdb)

            Then("unlink is called") {
                testCoroutineScheduler.advanceUntilIdle()
                scenario.unlinkFromTmdb.invoked shouldBe true
            }
        }
    }

    Given("logging out from Trakt") {

        When("started") {
            val scenario = TestScenario()
            scenario.sut.submit(ManageAccountAction.UnlinkFromTrakt)

            Then("unlink is called") {
                testCoroutineScheduler.advanceUntilIdle()
                scenario.unlinkFromTrakt.invoked shouldBe true
            }
        }
    }
})

private class ManageAccountViewModelTestScenario(
    val sut: ManageAccountViewModel,
    val linkToTmdb: FakeLinkToTmdb,
    val linkToTrakt: FakeLinkToTrakt,
    val notifyTmdbAppAuthorized: FakeNotifyTmdbAppAuthorized,
    val notifyTraktAppAuthorized: FakeNotifyTraktAppAuthorized,
    val startUpdateSuggestions: FakeStartUpdateSuggestions,
    val unlinkFromTmdb: FakeUnlinkFromTmdb,
    val unlinkFromTrakt: FakeUnlinkFromTrakt
)

private fun TestScenario(
    linkToTmdbResult: Either<LinkToTmdb.Error, LinkToTmdb.State> = LinkToTmdb.State.Success.right(),
    linkToTraktResult: Either<LinkToTrakt.Error, LinkToTrakt.State> = LinkToTrakt.State.Success.right(),
    account: Account? = null
): ManageAccountViewModelTestScenario {
    val linkToTmdb = FakeLinkToTmdb(result = linkToTmdbResult)
    val linkToTrakt = FakeLinkToTrakt(result = linkToTraktResult)
    val notifyTmdbAppAuthorized = FakeNotifyTmdbAppAuthorized()
    val notifyTraktAppAuthorized = FakeNotifyTraktAppAuthorized()
    val startUpdateSuggestions = FakeStartUpdateSuggestions()
    val unlinkFromTmdb = FakeUnlinkFromTmdb()
    val unlinkFromTrakt = FakeUnlinkFromTrakt()
    return ManageAccountViewModelTestScenario(
        sut = ManageAccountViewModel(
            getCurrentAccount = FakeGetCurrentAccount(account = account),
            linkToTmdb = linkToTmdb,
            linkToTrakt = linkToTrakt,
            notifyTmdbAppAuthorized = notifyTmdbAppAuthorized,
            notifyTraktAppAuthorized = notifyTraktAppAuthorized,
            networkErrorMapper = FakeNetworkErrorToMessageMapper(),
            startUpdateSuggestions = startUpdateSuggestions,
            unlinkFromTmdb = unlinkFromTmdb,
            unlinkFromTrakt = unlinkFromTrakt
        ),
        linkToTmdb = linkToTmdb,
        linkToTrakt = linkToTrakt,
        notifyTmdbAppAuthorized = notifyTmdbAppAuthorized,
        notifyTraktAppAuthorized = notifyTraktAppAuthorized,
        startUpdateSuggestions = startUpdateSuggestions,
        unlinkFromTmdb = unlinkFromTmdb,
        unlinkFromTrakt = unlinkFromTrakt
    )
}
