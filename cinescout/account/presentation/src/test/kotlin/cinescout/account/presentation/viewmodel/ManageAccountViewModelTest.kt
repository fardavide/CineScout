package cinescout.account.presentation.viewmodel

import app.cash.turbine.test
import arrow.core.Either
import arrow.core.left
import arrow.core.right
import cinescout.account.domain.model.Account
import cinescout.account.domain.model.GetAccountError
import cinescout.account.domain.sample.AccountSample
import cinescout.account.domain.usecase.FakeGetCurrentAccount
import cinescout.account.domain.usecase.FakeLinkTraktAccount
import cinescout.account.domain.usecase.FakeUnlinkTraktAccount
import cinescout.account.presentation.action.ManageAccountAction
import cinescout.account.presentation.mapper.AccountUiModelMapper
import cinescout.account.presentation.sample.ManageAccountStateSample
import cinescout.account.presentation.state.ManageAccountState
import cinescout.auth.domain.sample.TraktAuthorizationCodeSample
import cinescout.auth.domain.usecase.FakeNotifyTraktAppAuthorized
import cinescout.auth.domain.usecase.LinkToTrakt
import cinescout.error.NetworkError
import cinescout.resources.R.string
import cinescout.resources.TextRes
import cinescout.suggestions.domain.usecase.FakeStartUpdateSuggestions
import cinescout.test.android.ViewModelExtension
import cinescout.utils.compose.FakeNetworkErrorToMessageMapper
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
                scenario.linkTraktAccount.invoked shouldBe true
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
                scenario.unlinkTraktAccount.invoked shouldBe true
            }
        }
    }
})

private class ManageAccountViewModelTestScenario(
    val sut: ManageAccountViewModel,
    val linkTraktAccount: FakeLinkTraktAccount,
    val notifyTraktAppAuthorized: FakeNotifyTraktAppAuthorized,
    val startUpdateSuggestions: FakeStartUpdateSuggestions,
    val unlinkTraktAccount: FakeUnlinkTraktAccount
)

private fun TestScenario(
    linkToTraktResult: Either<LinkToTrakt.Error, LinkToTrakt.State> = LinkToTrakt.State.Success.right(),
    account: Account? = null,
    accountResult: Either<GetAccountError, Account> = account?.right() ?: GetAccountError.NotConnected.left()
): ManageAccountViewModelTestScenario {
    val linkTraktAccount = FakeLinkTraktAccount(result = linkToTraktResult)
    val notifyTraktAppAuthorized = FakeNotifyTraktAppAuthorized()
    val startUpdateSuggestions = FakeStartUpdateSuggestions()
    val unlinkTraktAccount = FakeUnlinkTraktAccount()
    return ManageAccountViewModelTestScenario(
        sut = ManageAccountViewModel(
            accountUiModelMapper = AccountUiModelMapper(),
            getCurrentAccount = FakeGetCurrentAccount(result = accountResult),
            linkTraktAccount = linkTraktAccount,
            notifyTraktAppAuthorized = notifyTraktAppAuthorized,
            networkErrorMapper = FakeNetworkErrorToMessageMapper(),
            startUpdateSuggestions = startUpdateSuggestions,
            unlinkTraktAccount = unlinkTraktAccount
        ),
        linkTraktAccount = linkTraktAccount,
        notifyTraktAppAuthorized = notifyTraktAppAuthorized,
        startUpdateSuggestions = startUpdateSuggestions,
        unlinkTraktAccount = unlinkTraktAccount
    )
}
