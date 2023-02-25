package cinescout.account.domain.usecase

import app.cash.turbine.test
import arrow.core.Either
import arrow.core.left
import arrow.core.right
import cinescout.account.domain.model.Account
import cinescout.account.domain.model.GetAccountError
import cinescout.account.domain.sample.AccountSample
import cinescout.auth.domain.usecase.FakeIsTmdbLinked
import cinescout.auth.domain.usecase.FakeIsTraktLinked
import cinescout.error.NetworkError
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.first

class RealGetCurrentAccountTest : BehaviorSpec({

    Given("Tmdb is linked") {
        val isTmdbLinked = true

        And("Trakt is linked") {
            val isTraktLinked = true

            val scenario = TestScenario(
                isTmdbLinked = isTmdbLinked,
                isTraktLinked = isTraktLinked
            )

            Then("exception is thrown") {
                shouldThrow<IllegalStateException> { scenario.sut().first() }
            }
        }

        And("Trakt is not linked") {
            val isTraktLinked = false

            When("account is available") {
                val tmdbAccount = AccountSample.Tmdb.right()

                val scenario = TestScenario(
                    isTmdbLinked = isTmdbLinked,
                    isTraktLinked = isTraktLinked,
                    tmdbAccount = tmdbAccount
                )

                Then("account is emitted") {
                    scenario.sut().test {
                        awaitItem() shouldBe tmdbAccount
                        awaitComplete()
                    }
                }
            }

            When("account is not available") {
                val tmdbAccount = GetAccountError.NotConnected.left()

                val scenario = TestScenario(
                    isTmdbLinked = isTmdbLinked,
                    isTraktLinked = isTraktLinked,
                    tmdbAccount = tmdbAccount
                )

                Then("error is emitted") {
                    scenario.sut().test {
                        awaitItem() shouldBe tmdbAccount
                        awaitComplete()
                    }
                }
            }

            When("account is error") {
                val tmdbAccount = GetAccountError.Network(NetworkError.NoNetwork).left()

                val scenario = TestScenario(
                    isTmdbLinked = isTmdbLinked,
                    isTraktLinked = isTraktLinked,
                    tmdbAccount = tmdbAccount
                )

                Then("error is emitted") {
                    scenario.sut().test {
                        awaitItem() shouldBe tmdbAccount
                        awaitComplete()
                    }
                }
            }
        }
    }

    Given("Trakt is linked") {
        val isTraktLinked = true

        And("Tmdb is not linked") {
            val isTmdbLinked = false

            When("account is available") {
                val traktAccount = AccountSample.Trakt.right()

                val scenario = TestScenario(
                    isTmdbLinked = isTmdbLinked,
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
                    isTmdbLinked = isTmdbLinked,
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
                    isTmdbLinked = isTmdbLinked,
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
    }

    Given("no service is linked") {
        val isTmdbLinked = false
        val isTraktLinked = false

        val scenario = TestScenario(
            isTmdbLinked = isTmdbLinked,
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
    isTmdbLinked: Boolean,
    isTraktLinked: Boolean,
    tmdbAccount: Either<GetAccountError, Account.Tmdb> = GetAccountError.NotConnected.left(),
    traktAccount: Either<GetAccountError, Account.Trakt> = GetAccountError.NotConnected.left()
) = GetCurrentAccountTestScenario(
    sut = RealGetCurrentAccount(
        getTmdbAccount = FakeGetTmdbAccount(result = tmdbAccount),
        getTraktAccount = FakeGetTraktAccount(result = traktAccount),
        isTmdbLinked = FakeIsTmdbLinked(isLinked = isTmdbLinked),
        isTraktLinked = FakeIsTraktLinked(isLinked = isTraktLinked)
    )
)
