package cinescout.auth.domain.usecase

import arrow.core.left
import arrow.core.right
import cinescout.error.NetworkError
import cinescout.model.NetworkOperation
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

class RealCallWithTraktAccountTest : BehaviorSpec({

    Given("Trakt is not linked") {
        val isTraktLinked = FakeIsTraktLinked(isLinked = false)
        val callWithTraktAccount = RealCallWithTraktAccount(appScope = testScope, isTraktLinked = isTraktLinked)

        When("call returns left") {
            val call = { NetworkError.Unknown.left() }

            Then("it skips the call") {
                callWithTraktAccount(call) shouldBe NetworkOperation.Skipped.left()
            }
        }

        When("call returns right") {
            val call = { 2.right() }

            Then("it skips the call") {
                callWithTraktAccount(call) shouldBe NetworkOperation.Skipped.left()
            }
        }
    }

    Given("Trakt is linked") {
        val isTraktLinked = FakeIsTraktLinked(isLinked = true)
        val callWithTraktAccount = RealCallWithTraktAccount(appScope = testScope, isTraktLinked = isTraktLinked)

        When("call returns left") {
            val error = NetworkError.Forbidden
            val call = { error.left() }

            Then("it returns a network operation error") {
                callWithTraktAccount(call) shouldBe NetworkOperation.Error(error).left()
            }
        }

        When("call returns right") {
            val call = { 2.right() }

            Then("it returns the call result") {
                callWithTraktAccount(call) shouldBe 2.right()
            }
        }
    }
})
