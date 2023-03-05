package cinescout.account.presentation.viewmodel

import app.cash.turbine.test
import arrow.core.Either
import arrow.core.left
import arrow.core.right
import cinescout.account.domain.model.Account
import cinescout.account.domain.model.GetAccountError
import cinescout.account.domain.sample.AccountSample
import cinescout.account.domain.usecase.FakeGetCurrentAccount
import cinescout.account.presentation.action.ManageAccountAction
import cinescout.account.presentation.mapper.AccountUiModelMapper
import cinescout.account.presentation.sample.ManageAccountStateSample
import cinescout.account.presentation.state.ManageAccountState
import cinescout.auth.trakt.domain.sample.TraktAuthorizationCodeSample
import cinescout.auth.trakt.domain.usecase.FakeLinkToTrakt
import cinescout.auth.trakt.domain.usecase.FakeNotifyTraktAppAuthorized
import cinescout.auth.trakt.domain.usecase.FakeUnlinkFromTrakt
import cinescout.auth.trakt.domain.usecase.LinkToTrakt
import cinescout.design.FakeNetworkErrorToMessageMapper
import cinescout.design.R.string
import cinescout.design.TextRes
import cinescout.error.NetworkError
import cinescout.suggestions.domain.usecase.FakeStartUpdateSuggestions
import cinescout.test.android.ViewModelExtension
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.core.test.testCoroutineScheduler
import io.kotest.matchers.shouldBe

class ManageAccountViewModelTest : BehaviorSpec({
    extension(ViewModelExtension())

    Given("view model") {

        When("started") {
            val scenario = TestScenario()

            Then("should emit loading state") {
                scenario.sut.state.test {
                    awaitItem() shouldBe ManageAccountState.Loading
                }
            }
        }

        When("Trakt account connected") {
            val account = AccountSample.Trakt
            val scenario = TestScenario(account = account)

            Then("account is emitted") {
                testCoroutineScheduler.advanceUntilIdle()
                scenario.sut.state.test {
                    awaitItem().account shouldBe ManageAccountStateSample.Account.Connected
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

        When("account is error") {
            val account = GetAccountError.Network(NetworkError.Forbidden).left()
            val scenario = TestScenario(accountResult = account)

            Then("error is emitted") {
                testCoroutineScheduler.advanceUntilIdle()
                scenario.sut.state.test {
                    awaitItem().account shouldBe ManageAccountStateSample.Account.Error
                    cancelAndIgnoreRemainingEvents()
                }
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
    val linkToTrakt: FakeLinkToTrakt,
    val notifyTraktAppAuthorized: FakeNotifyTraktAppAuthorized,
    val startUpdateSuggestions: FakeStartUpdateSuggestions,
    val unlinkFromTrakt: FakeUnlinkFromTrakt
)

private fun TestScenario(
    linkToTraktResult: Either<LinkToTrakt.Error, LinkToTrakt.State> = LinkToTrakt.State.Success.right(),
    account: Account? = null,
    accountResult: Either<GetAccountError, Account> = account?.right() ?: GetAccountError.NotConnected.left()
): ManageAccountViewModelTestScenario {
    val linkToTrakt = FakeLinkToTrakt(result = linkToTraktResult)
    val notifyTraktAppAuthorized = FakeNotifyTraktAppAuthorized()
    val startUpdateSuggestions = FakeStartUpdateSuggestions()
    val unlinkFromTrakt = FakeUnlinkFromTrakt()
    return ManageAccountViewModelTestScenario(
        sut = ManageAccountViewModel(
            accountUiModelMapper = AccountUiModelMapper(),
            getCurrentAccount = FakeGetCurrentAccount(result = accountResult),
            linkToTrakt = linkToTrakt,
            notifyTraktAppAuthorized = notifyTraktAppAuthorized,
            networkErrorMapper = FakeNetworkErrorToMessageMapper(),
            startUpdateSuggestions = startUpdateSuggestions,
            unlinkFromTrakt = unlinkFromTrakt
        ),
        linkToTrakt = linkToTrakt,
        notifyTraktAppAuthorized = notifyTraktAppAuthorized,
        startUpdateSuggestions = startUpdateSuggestions,
        unlinkFromTrakt = unlinkFromTrakt
    )
}
