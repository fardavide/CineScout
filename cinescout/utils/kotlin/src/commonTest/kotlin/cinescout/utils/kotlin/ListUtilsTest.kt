package cinescout.utils.kotlin

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import cinescout.error.NetworkError
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

class ListUtilsTest : BehaviorSpec({

    Given("shiftWithAnyRight") {

        When("the list is empty") {
            val eitherList = emptyList<Either<NetworkError, Int>>()
            val result = eitherList.shiftWithAnyRight()

            Then("it returns right of empty list") {
                result shouldBe emptyList<Int>().right()
            }
        }

        When("the list has one right and one left") {
            val eitherList = listOf(
                Either.Left(NetworkError.NoNetwork),
                Either.Right(1)
            )
            val result = eitherList.shiftWithAnyRight()

            Then("it returns a list of right") {
                result shouldBe listOf(1).right()
            }
        }

        When("the list has one left") {
            val eitherList = listOf(
                Either.Left(NetworkError.NoNetwork)
            )
            val result = eitherList.shiftWithAnyRight()

            Then("it returns the first left") {
                result shouldBe NetworkError.NoNetwork.left()
            }
        }
    }
})
