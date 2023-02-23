package cinescout.auth.tmdb.domain.usecase

import arrow.core.left
import arrow.core.right
import cinescout.auth.domain.usecase.FakeIsTmdbLinked
import cinescout.error.NetworkError
import cinescout.model.NetworkOperation
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

class CallWithTmdbAccountTest : BehaviorSpec({

    Given("Tmdb is not linked") {
        val isTmdbLinked = FakeIsTmdbLinked(isLinked = false)
        val callWithTmdbAccount = CallWithTmdbAccount(appScope = testScope, isTmdbLinked = isTmdbLinked)

        When("call returns left") {
            val call = { NetworkError.Unknown.left() }

            Then("it skips the call") {
                callWithTmdbAccount(call) shouldBe NetworkOperation.Skipped.left()
            }
        }

        When("call returns right") {
            val call = { 2.right() }

            Then("it skips the call") {
                callWithTmdbAccount(call) shouldBe NetworkOperation.Skipped.left()
            }
        }
    }

    Given("Tmdb is linked") {
        val isTmdbLinked = FakeIsTmdbLinked(isLinked = true)
        val callWithTmdbAccount = CallWithTmdbAccount(appScope = testScope, isTmdbLinked = isTmdbLinked)

        When("call returns left") {
            val error = NetworkError.Forbidden
            val call = { error.left() }

            Then("it returns a network operation error") {
                callWithTmdbAccount(call) shouldBe NetworkOperation.Error(error).left()
            }
        }

        When("call returns right") {
            val call = { 2.right() }

            Then("it returns the call result") {
                callWithTmdbAccount(call) shouldBe 2.right()
            }
        }
    }
})
