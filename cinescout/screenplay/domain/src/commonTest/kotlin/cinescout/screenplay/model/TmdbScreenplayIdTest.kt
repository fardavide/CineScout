package cinescout.screenplay.model

import cinescout.screenplay.domain.model.TmdbScreenplayId
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class TmdbScreenplayIdTest : BehaviorSpec({

    Given("a Movie Id") {
        val tmdbScreenplayId = TmdbScreenplayId.Movie(123)

        When("unique id is called") {
            val string = tmdbScreenplayId.uniqueId()

            Then("it should be the id") {
                string shouldBe "\"movie:123\""
            }
        }

        When("encode to string") {
            val string = Json.encodeToString<TmdbScreenplayId>(tmdbScreenplayId)

            And("decode from string") {
                val decoded = Json.decodeFromString<TmdbScreenplayId>(string)

                Then("it should be the same") {
                    decoded shouldBe tmdbScreenplayId
                }
            }
        }
    }

    Given("a TvShow Id") {
        val tmdbScreenplayId = TmdbScreenplayId.TvShow(123)

        When("unique id is called") {
            val string = tmdbScreenplayId.uniqueId()

            Then("it should be the id") {
                string shouldBe "\"tv_show:123\""
            }
        }

        When("encode to string") {
            val string = Json.encodeToString<TmdbScreenplayId>(tmdbScreenplayId)

            And("decode from string") {
                val decoded = Json.decodeFromString<TmdbScreenplayId>(string)

                Then("it should be the same") {
                    decoded shouldBe tmdbScreenplayId
                }
            }
        }
    }
})
