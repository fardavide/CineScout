package cinescout.network

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import korlibs.time.Date
import kotlinx.serialization.json.Json

class DateSerializerTest : BehaviorSpec({

    Given("date with Trakt Movie format") {
        val date = "\"2010-07-16\""

        When("deserialize") {
            val result = Json.decodeFromString(DateSerializer(), date)

            Then("the date is returned") {
                result shouldBe Date.Companion(year = 2010, month = 7, day = 16)
            }
        }
    }

    Given("date with Trakt Tv Show format") {
        val date = "\"2008-01-21T02:00:00.000Z\""

        When("deserialize") {
            val result = Json.decodeFromString(DateSerializer(), date)

            Then("the date is returned") {
                result shouldBe Date.Companion(year = 2008, month = 1, day = 21)
            }
        }
    }
})
