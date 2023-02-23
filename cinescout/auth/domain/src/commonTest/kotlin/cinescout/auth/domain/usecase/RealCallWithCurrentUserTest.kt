package cinescout.auth.domain.usecase

import arrow.core.left
import arrow.core.right
import cinescout.error.NetworkError
import cinescout.model.NetworkOperation
import io.kotest.assertions.throwables.shouldThrowWithMessage
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

class RealCallWithCurrentUserTest : BehaviorSpec({

    val unitSuccessCall = { Unit.right() }
    val intSuccessCall = { 1.right() }
    val networkError = NetworkError.Forbidden
    val errorCall = { networkError.left() }

    Given("no account is linked") {
        val scenario = TestScenario(isTmdbLinked = false, isTraktLinked = false)

        When("return type is Unit") {
            val result = scenario.sut.forUnit(
                tmdbCall = unitSuccessCall,
                traktCall = unitSuccessCall
            )

            Then("success is returned") {
                result shouldBe unitSuccessCall()
            }
        }

        When("return type is not Unit") {
            val result = scenario.sut.forResult(
                tmdbCall = intSuccessCall,
                traktCall = intSuccessCall
            )

            Then("skipped is returned") {
                result shouldBe NetworkOperation.Skipped.left()
            }
        }
    }

    Given("both accounts are linked") {
        val scenario = TestScenario(isTmdbLinked = true, isTraktLinked = true)

        When("return type is Unit") {
            Then("exception is thrown") {
                shouldThrowWithMessage<IllegalStateException>(CallWithCurrentUser.BothLinkedErrorMessage) {
                    scenario.sut.forUnit(
                        tmdbCall = unitSuccessCall,
                        traktCall = unitSuccessCall
                    )
                }
            }
        }

        When("return type is not Unit") {
            Then("exception is thrown") {
                shouldThrowWithMessage<IllegalStateException>(CallWithCurrentUser.BothLinkedErrorMessage) {
                    scenario.sut.forResult(
                        tmdbCall = intSuccessCall,
                        traktCall = intSuccessCall
                    )
                }
            }
        }
    }

    Given("one account is linked") {
        val scenario = TestScenario(isTmdbLinked = true, isTraktLinked = false)

        When("return type is Unit") {

            And("connected source's operation succeed") {
                val result = scenario.sut.forUnit(
                    tmdbCall = unitSuccessCall,
                    traktCall = unitSuccessCall
                )

                Then("success is returned") {
                    result shouldBe unitSuccessCall()
                }
            }

            And("connected source's operation fails") {
                val result = scenario.sut.forUnit(
                    tmdbCall = errorCall,
                    traktCall = unitSuccessCall
                )

                Then("error is returned") {
                    result shouldBe errorCall()
                }
            }
        }

        When("return type is not Unit") {

            And("connected source's operation succeed") {
                val result = scenario.sut.forResult(
                    tmdbCall = intSuccessCall,
                    traktCall = intSuccessCall
                )

                Then("success is returned") {
                    result shouldBe intSuccessCall()
                }
            }

            And("connected source's operation fails") {
                val result = scenario.sut.forResult(
                    tmdbCall = errorCall,
                    traktCall = intSuccessCall
                )

                Then("error is returned") {
                    result shouldBe NetworkOperation.Error(networkError).left()
                }
            }
        }
    }
})

private class RealCallWithCurrentUserTestScenario(
    val sut: CallWithCurrentUser
)

private fun TestScenario(isTmdbLinked: Boolean, isTraktLinked: Boolean) = RealCallWithCurrentUserTestScenario(
    sut = RealCallWithCurrentUser(
        isTmdbLinked = FakeIsTmdbLinked(isLinked = isTmdbLinked),
        isTraktLinked = FakeIsTraktLinked(isLinked = isTraktLinked)
    )
)
