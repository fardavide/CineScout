package cinescout.rating.data.remote.service

import arrow.core.right
import cinescout.network.trakt.CineScoutTraktClient
import cinescout.rating.data.remote.mock.TraktRatingMockEngine
import cinescout.rating.data.remote.sample.TraktRatingExtendedBodySample
import cinescout.rating.data.remote.sample.TraktRatingMetadataBodySample
import cinescout.screenplay.domain.model.ScreenplayType
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

class TraktRatingServiceTest : BehaviorSpec({

    Given("logged in") {
        val scenario = TestScenario()

        When("get rating ids") {

            And("type is all") {
                val result = scenario.sut.getAllRatingIds(ScreenplayType.All)

                Then("should return rating") {
                    result shouldBe listOf(
                        TraktRatingMetadataBodySample.Inception,
                        TraktRatingMetadataBodySample.BreakingBad
                    ).right()
                }
            }

            And("type is movies") {
                val result = scenario.sut.getAllRatingIds(ScreenplayType.Movies)

                Then("should return rating") {
                    result shouldBe listOf(
                        TraktRatingMetadataBodySample.Inception
                    ).right()
                }
            }

            And("type is tv shows") {
                val result = scenario.sut.getAllRatingIds(ScreenplayType.TvShows)

                Then("should return rating") {
                    result shouldBe listOf(
                        TraktRatingMetadataBodySample.BreakingBad
                    ).right()
                }
            }
        }

        When("get ratings") {

            And("type is all") {
                val result = scenario.sut.getRatings(ScreenplayType.All, 1)

                Then("should return rating") {
                    result shouldBe listOf(
                        TraktRatingExtendedBodySample.Inception,
                        TraktRatingExtendedBodySample.BreakingBad
                    ).right()
                }
            }

            And("type is movies") {
                val result = scenario.sut.getRatings(ScreenplayType.Movies, 1)

                Then("should return rating") {
                    result shouldBe listOf(
                        TraktRatingExtendedBodySample.Inception
                    ).right()
                }
            }

            And("type is tv shows") {
                val result = scenario.sut.getRatings(ScreenplayType.TvShows, 1)

                Then("should return rating") {
                    result shouldBe listOf(
                        TraktRatingExtendedBodySample.BreakingBad
                    ).right()
                }
            }
        }
    }
})

private class TraktRatingServiceTestScenario(
    val sut: TraktRatingService
)

private fun TestScenario(): TraktRatingServiceTestScenario {
    val engine = TraktRatingMockEngine(forceLoggedIn = true)
    val client = CineScoutTraktClient(engine, logBody = true)
    return TraktRatingServiceTestScenario(
        sut = TraktRatingService(client)
    )
}
