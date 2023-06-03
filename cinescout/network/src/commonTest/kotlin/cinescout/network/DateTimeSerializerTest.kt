package cinescout.network

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import korlibs.time.DateTime
import kotlinx.serialization.json.Json

class DateTimeSerializerTest : BehaviorSpec({

    Given("date time with Trakt Movie format") {
        val date = "\"2010-07-16\""

        When("deserialize") {
            val result = Json.decodeFromString(DateTimeSerializer(), date)

            Then("the date is returned") {
                result shouldBe DateTime(year = 2010, month = 7, day = 16)
            }
        }
    }

    Given("date time with Trakt Tv Show format") {
        val date = "\"2008-01-21T02:05:09.000Z\""

        When("deserialize") {
            val result = Json.decodeFromString(DateTimeSerializer(), date)

            Then("the date is returned") {
                result shouldBe DateTime(year = 2008, month = 1, day = 21, hour = 2, minute = 5, second = 9)
            }
        }
    }
})
