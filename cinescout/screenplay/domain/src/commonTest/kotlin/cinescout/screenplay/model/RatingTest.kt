package cinescout.screenplay.model

import cinescout.screenplay.domain.model.Rating
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.property.Arb
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.negativeInt
import io.kotest.property.forAll

class RatingTest : BehaviorSpec({
    
    Given("input") {

        When("below zero") {

            Then("it should be invalid") {
                forAll(Arb.negativeInt()) { input ->
                    Rating.of(input).isLeft()
                }
            }
        }

        When("between zero and ten") {

            Then("it should be valid") {
                forAll(Arb.int(0..10)) { input ->
                    Rating.of(input).isRight()
                }
            }
        }

        When("above ten") {

            Then("it should be invalid") {
                forAll(Arb.int(11..Int.MAX_VALUE)) { input ->
                    Rating.of(input).isLeft()
                }
            }
        }
    }
})
