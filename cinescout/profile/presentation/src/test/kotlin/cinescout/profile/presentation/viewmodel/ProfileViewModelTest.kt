package cinescout.profile.presentation.viewmodel

import app.cash.turbine.test
import arrow.core.left
import arrow.core.right
import cinescout.account.domain.model.Account
import cinescout.account.domain.model.GetAccountError
import cinescout.account.domain.sample.AccountSample
import cinescout.account.domain.usecase.FakeGetCurrentAccount
import cinescout.error.NetworkError
import cinescout.profile.presentation.sample.ProfileStateSample
import cinescout.profile.presentation.state.ProfileState
import cinescout.test.android.ViewModelExtension
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.core.test.testCoroutineScheduler
import io.kotest.matchers.shouldBe

class ProfileViewModelTest : BehaviorSpec({
    extension(ViewModelExtension())

    Given("view model") {

        When("started") {
            val scenario = TestScenario()

            Then("state is loading") {
                scenario.sut.state.test {
                    awaitItem() shouldBe ProfileState.Loading
                }
            }
        }

        When("account connected") {
            val account = AccountSample.Tmdb
            val scenario = TestScenario(account = account)

            Then("account is connected") {
                testCoroutineScheduler.advanceUntilIdle()
                scenario.sut.state.test {
                    awaitItem().account shouldBe ProfileStateSample.AccountConnected.account
                }
            }
        }

        When("account error") {
            val accountError = NetworkError.NoNetwork
            val scenario = TestScenario(accountError = accountError)

            Then("account is error") {
                testCoroutineScheduler.advanceUntilIdle()
                scenario.sut.state.test {
                    awaitItem().account shouldBe ProfileStateSample.AccountError.account
                }
            }
        }

        When("account not connected") {
            val scenario = TestScenario(account = null)

            Then("account is not connected") {
                testCoroutineScheduler.advanceUntilIdle()
                scenario.sut.state.test {
                    awaitItem().account shouldBe ProfileStateSample.AccountNotConnected.account
                }
            }
        }
    }
})

private class ProfileViewModelTestScenario(
    val sut: ProfileViewModel
)

private fun TestScenario(
    account: Account? = null,
    accountError: NetworkError? = null
): ProfileViewModelTestScenario {
    val accountResult = account?.right()
        ?: accountError?.let(GetAccountError::Network)?.left()
        ?: GetAccountError.NotConnected.left()
    return ProfileViewModelTestScenario(
        sut = ProfileViewModel(
            getCurrentAccount = FakeGetCurrentAccount(result = accountResult)
        )
    )
}
