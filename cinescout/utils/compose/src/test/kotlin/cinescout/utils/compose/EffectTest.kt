package cinescout.utils.compose

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe

class EffectTest : BehaviorSpec({

    Given("a non empty effect") {
        val effect = Effect.of("hello")

        When("effect is consumed") {
            val event = effect.consume()

            Then("event is returned") {
                event shouldBe "hello"
            }

            And("event is cleared") {
                effect.consume().shouldBeNull()
            }
        }
    }
})
