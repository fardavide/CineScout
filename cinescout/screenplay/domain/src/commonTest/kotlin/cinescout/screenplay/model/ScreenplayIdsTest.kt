package cinescout.screenplay.model

import cinescout.screenplay.domain.model.ScreenplayIds
import cinescout.screenplay.domain.sample.ScreenplayIdsSample
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class ScreenplayIdsTest : BehaviorSpec({

    Given("a Movie Id") {
        val ids = ScreenplayIdsSample.Inception

        When("unique id is called") {
            val string = ids.uniqueId()

            Then("it should be the id") {
                string shouldBe "\"movie:27205:16662\""
            }
        }

        When("encode to string") {
            val string = Json.encodeToString<ScreenplayIds>(ids)

            And("decode from string") {
                val decoded = Json.decodeFromString<ScreenplayIds>(string)

                Then("it should be the same") {
                    decoded shouldBe ids
                }
            }
        }
    }

    Given("a TvShow Id") {
        val ids = ScreenplayIdsSample.Dexter

        When("unique id is called") {
            val string = ids.uniqueId()

            Then("it should be the id") {
                string shouldBe "\"tv_show:1405:1396\""
            }
        }

        When("encode to string") {
            val string = Json.encodeToString<ScreenplayIds>(ids)

            And("decode from string") {
                val decoded = Json.decodeFromString<ScreenplayIds>(string)

                Then("it should be the same") {
                    decoded shouldBe ids
                }
            }
        }
    }
})
