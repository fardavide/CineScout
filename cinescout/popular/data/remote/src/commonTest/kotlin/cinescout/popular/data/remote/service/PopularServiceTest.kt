package cinescout.popular.data.remote.service

import cinescout.network.trakt.CineScoutTraktClient
import cinescout.popular.data.remote.mock.TraktPopularMockEngine
import cinescout.screenplay.domain.model.ScreenplayType
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import screenplay.data.remote.trakt.sample.TraktScreenplayMetadataBodySample

class PopularServiceTest : BehaviorSpec({

    Given("service") {

        When("get popular movies' ids") {
            val scenario = TestScenario()
            val result = scenario.sut.getMostPopularIds(ScreenplayType.Movies)

            Then("movies' ids should be returned") {
                result.getOrNull() shouldBe listOf(TraktScreenplayMetadataBodySample.Avatar3)
            }
        }

        When("get popular tv shows' ids") {
            val scenario = TestScenario()
            val result = scenario.sut.getMostPopularIds(ScreenplayType.TvShows)

            Then("tv shows' ids should be returned") {
                result.getOrNull() shouldBe listOf(TraktScreenplayMetadataBodySample.TheWalkingDeadDeadCity)
            }
        }

        When("get popular screenplays' ids") {
            val scenario = TestScenario()
            val result = scenario.sut.getMostPopularIds(ScreenplayType.All)

            Then("screenplays' ids should be returned") {
                result.getOrNull() shouldBe listOf(
                    TraktScreenplayMetadataBodySample.Avatar3,
                    TraktScreenplayMetadataBodySample.TheWalkingDeadDeadCity
                )
            }
        }
    }
})

private class PopularServiceTestScenario(
    val sut: PopularService
)

private fun TestScenario() = PopularServiceTestScenario(
    sut = PopularService(CineScoutTraktClient(TraktPopularMockEngine()))
)
