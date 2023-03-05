package cinescout.account.domain.usecase

import app.cash.turbine.test
import arrow.core.Either
import arrow.core.left
import arrow.core.right
import cinescout.account.domain.model.Account
import cinescout.account.domain.model.GetAccountError
import cinescout.account.domain.sample.AccountSample
import cinescout.auth.domain.usecase.FakeIsTraktLinked
import cinescout.error.NetworkError
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

class RealGetCurrentAccountTest : BehaviorSpec({

    Given("Trakt is linked") {
        val isTraktLinked = true

        When("account is available") {
            val traktAccount = AccountSample.Trakt.right()

            val scenario = TestScenario(
                isTraktLinked = isTraktLinked,
                traktAccount = traktAccount
            )

            Then("account is emitted") {
                scenario.sut().test {
                    awaitItem() shouldBe traktAccount
                    awaitComplete()
                }
            }
        }

        When("account is not available") {
            val traktAccount = GetAccountError.NotConnected.left()

            val scenario = TestScenario(
                isTraktLinked = isTraktLinked,
                traktAccount = traktAccount
            )

            Then("error is emitted") {
                scenario.sut().test {
                    awaitItem() shouldBe traktAccount
                    awaitComplete()
                }
            }
        }

        When("account is error") {
            val traktAccount = GetAccountError.Network(NetworkError.NoNetwork).left()

            val scenario = TestScenario(
                isTraktLinked = isTraktLinked,
                traktAccount = traktAccount
            )

            Then("error is emitted") {
                scenario.sut().test {
                    awaitItem() shouldBe traktAccount
                    awaitComplete()
                }
            }
        }
    }

    Given("Trakt is not linked") {
        val isTraktLinked = false

        val scenario = TestScenario(
            isTraktLinked = isTraktLinked
        )

        Then("not connected is emitted") {
            scenario.sut().test {
                awaitItem() shouldBe GetAccountError.NotConnected.left()
                awaitComplete()
            }
        }
    }
})

private class GetCurrentAccountTestScenario(
    val sut: GetCurrentAccount
)

private fun TestScenario(
    isTraktLinked: Boolean,
    traktAccount: Either<GetAccountError, Account> = GetAccountError.NotConnected.left()
) = GetCurrentAccountTestScenario(
    sut = RealGetCurrentAccount(
        getTraktAccount = FakeGetTraktAccount(result = traktAccount),
        isTraktLinked = FakeIsTraktLinked(isLinked = isTraktLinked)
    )
)
