package cinescout.account.domain.usecase

import app.cash.turbine.test
import arrow.core.Either
import arrow.core.left
import arrow.core.right
import cinescout.account.domain.model.Account
import cinescout.account.domain.model.GetAccountError
import cinescout.account.domain.sample.AccountSample
import cinescout.error.NetworkError
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.first

class RealGetCurrentAccountTest : BehaviorSpec({

    Given("Tmdb account is connected") {
        val tmdbAccount = AccountSample.Tmdb.right()

        And("Trakt account is not connected") {
            val traktAccount = GetAccountError.NotConnected.left()

            When("get current account") {
                val scenario = TestScenario(
                    tmdbAccount = tmdbAccount,
                    traktAccount = traktAccount
                )

                Then("Tmdb account is returned") {
                    scenario.sut().test {
                        awaitItem() shouldBe tmdbAccount
                        awaitComplete()
                    }
                }
            }
        }

        And("Trakt account is error") {
            val traktAccount = GetAccountError.Network(NetworkError.NoNetwork).left()

            When("get current account") {
                val scenario = TestScenario(
                    tmdbAccount = tmdbAccount,
                    traktAccount = traktAccount
                )

                Then("Tmdb account is returned") {
                    scenario.sut().test {
                        awaitItem() shouldBe tmdbAccount
                        awaitComplete()
                    }
                }
            }
        }
    }

    Given("Trakt account is connected") {
        val traktAccount = AccountSample.Trakt.right()

        And("Tmdb account is not connected") {
            val tmdbAccount = GetAccountError.NotConnected.left()

            When("get current account") {
                val scenario = TestScenario(
                    tmdbAccount = tmdbAccount,
                    traktAccount = traktAccount
                )

                Then("Trakt account is returned") {
                    scenario.sut().test {
                        awaitItem() shouldBe traktAccount
                        awaitComplete()
                    }
                }
            }
        }

        And("Tmdb account is error") {
            val tmdbAccount = GetAccountError.Network(NetworkError.NoNetwork).left()

            When("get current account") {
                val scenario = TestScenario(
                    tmdbAccount = tmdbAccount,
                    traktAccount = traktAccount
                )

                Then("Trakt account is returned") {
                    scenario.sut().test {
                        awaitItem() shouldBe traktAccount
                        awaitComplete()
                    }
                }
            }
        }
    }

    Given("Tmdb account is error") {
        val tmdbAccount = GetAccountError.Network(NetworkError.NoNetwork).left()

        And("Trakt account is not connected") {
            val traktAccount = GetAccountError.NotConnected.left()

            When("get current account") {
                val scenario = TestScenario(
                    tmdbAccount = tmdbAccount,
                    traktAccount = traktAccount
                )

                Then("error is returned") {
                    scenario.sut().test {
                        awaitItem() shouldBe tmdbAccount
                        awaitComplete()
                    }
                }
            }
        }
    }

    Given("Trakt account is error") {
        val traktAccount = GetAccountError.Network(NetworkError.NoNetwork).left()

        And("Tmdb account is not connected") {
            val tmdbAccount = GetAccountError.NotConnected.left()

            When("get current account") {
                val scenario = TestScenario(
                    tmdbAccount = tmdbAccount,
                    traktAccount = traktAccount
                )

                Then("error is returned") {
                    scenario.sut().test {
                        awaitItem() shouldBe traktAccount
                        awaitComplete()
                    }
                }
            }
        }
    }

    Given("Both accounts are connected") {
        val tmdbAccount = AccountSample.Tmdb.right()
        val traktAccount = AccountSample.Trakt.right()

        When("get current account") {
            val scenario = TestScenario(
                tmdbAccount = tmdbAccount,
                traktAccount = traktAccount
            )

            Then("exception is thrown") {
                shouldThrow<IllegalStateException> { scenario.sut().first() }
            }
        }
    }

    Given("No account is connected") {
        val tmdbAccount = GetAccountError.NotConnected.left()
        val traktAccount = GetAccountError.NotConnected.left()

        When("get current account") {
            val scenario = TestScenario(
                tmdbAccount = tmdbAccount,
                traktAccount = traktAccount
            )

            Then("not connected is returned") {
                scenario.sut().test {
                    awaitItem() shouldBe tmdbAccount
                    awaitComplete()
                }
            }
        }
    }

    Given("Both accounts are error") {
        val tmdbAccount = GetAccountError.Network(NetworkError.NoNetwork).left()
        val traktAccount = GetAccountError.Network(NetworkError.NoNetwork).left()

        When("get current account") {
            val scenario = TestScenario(
                tmdbAccount = tmdbAccount,
                traktAccount = traktAccount
            )

            Then("error is returned") {
                scenario.sut().test {
                    awaitItem() shouldBe tmdbAccount
                    awaitComplete()
                }
            }
        }
    }
})

private class GetCurrentAccountTestScenario(
    val sut: GetCurrentAccount
)

private fun TestScenario(
    tmdbAccount: Either<GetAccountError, Account.Tmdb>,
    traktAccount: Either<GetAccountError, Account.Trakt>
) = GetCurrentAccountTestScenario(
    sut = RealGetCurrentAccount(
        getTmdbAccount = FakeGetTmdbAccount(result = tmdbAccount),
        getTraktAccount = FakeGetTraktAccount(result = traktAccount)
    )
)
