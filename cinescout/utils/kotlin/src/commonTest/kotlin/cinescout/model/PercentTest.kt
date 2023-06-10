package cinescout.model

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

class PercentTest : BehaviorSpec({

    Given("valid int") {
        val int = 33

        When("percent is created") {
            val percent = int.percent

            Then("value is correct") {
                percent.value shouldBe int
            }
        }
    }

    Given("valid double") {
        val double = 33.33

        When("percent is created") {
            val percent = double.percent

            Then("value is correct") {
                percent.value shouldBe double
            }
        }
    }

    Given("valid double with 4 decimals") {
        val double = 33.3333

        When("percent is created") {
            val percent = double.percent

            Then("value is rounded to 2 decimals") {
                percent.value shouldBe 33.33
            }
        }
    }

    Given("valid float") {
        val float = 33.33f

        When("percent is created") {
            val percent = float.percent

            Then("value is correct") {
                percent.value shouldBe 33.33
            }
        }
    }
})
