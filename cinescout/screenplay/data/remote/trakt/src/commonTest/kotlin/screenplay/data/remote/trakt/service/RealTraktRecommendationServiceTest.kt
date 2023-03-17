package screenplay.data.remote.trakt.service

import arrow.core.left
import arrow.core.right
import cinescout.error.NetworkError
import cinescout.network.trakt.CineScoutTraktClient
import cinescout.network.trakt.FakeTraktAuthProvider
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import screenplay.data.remote.trakt.mock.TraktRecommendationMockEngine
import screenplay.data.remote.trakt.sample.TraktScreenplayMetadataResponseSample

class RealTraktRecommendationServiceTest : BehaviorSpec({

    Given("not connected") {
        val isConnected = false

        When("get personal recommended movies") {
            val scenario = TestScenario(isConnected)

            Then("unauthorized is returned") {
                scenario.sut.getRecommendedMovies() shouldBe NetworkError.Unauthorized.left()
            }
        }

        When("get personal recommended tv shows") {
            val scenario = TestScenario(isConnected)

            Then("unauthorized is returned") {
                scenario.sut.getRecommendedTvShows() shouldBe NetworkError.Unauthorized.left()
            }
        }
    }

    Given("connected") {
        val isConnected = true

        When("get personal recommended movies") {
            val scenario = TestScenario(isConnected)

            Then("movies are returned") {
                scenario.sut.getRecommendedMovies() shouldBe TraktScreenplayMetadataResponseSample.ThreeMovies.right()
            }
        }

        When("get personal recommended tv shows") {
            val scenario = TestScenario(isConnected)

            Then("tv shows are returned") {
                scenario.sut.getRecommendedTvShows() shouldBe TraktScreenplayMetadataResponseSample.ThreeTvShows.right()
            }
        }
    }
})

private class RealTraktRecommendationServiceTestScenario(
    val sut: RealTraktRecommendationService
)

private fun TestScenario(isConnected: Boolean): RealTraktRecommendationServiceTestScenario {
    val client = CineScoutTraktClient(
        engine = TraktRecommendationMockEngine(),
        authProvider = FakeTraktAuthProvider(isConnected)
    )
    return RealTraktRecommendationServiceTestScenario(
        sut = RealTraktRecommendationService(client)
    )
}
