package cinescout.history.data.remote.mapper

import cinescout.history.data.remote.sample.TraktHistoryMetadataResponseSample
import cinescout.history.domain.sample.HistorySample
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import screenplay.data.remote.trakt.mapper.TraktScreenplayIdMapper

class TraktHistoryMapperTest : BehaviorSpec({

    Given("2 episodes history") {
        val response = TraktHistoryMetadataResponseSample.BreakingBad

        When("mapping") {
            val scenario = TestScenario()
            val result = scenario.sut.toHistories(response)

            Then("it groups by tv show") {
                result shouldBe listOf(HistorySample.BreakingBad)
            }
        }
    }
})

private class TraktHistoryMapperTestScenario(
    val sut: TraktHistoryMapper
)

private fun TestScenario() = TraktHistoryMapperTestScenario(
    sut = TraktHistoryMapper(idMapper = TraktScreenplayIdMapper())
)
