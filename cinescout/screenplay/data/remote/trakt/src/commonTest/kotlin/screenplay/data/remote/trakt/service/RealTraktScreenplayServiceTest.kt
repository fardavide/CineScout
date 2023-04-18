package screenplay.data.remote.trakt.service

import cinescout.network.testutil.jsonArrayOf
import cinescout.network.trakt.CineScoutTraktClient
import cinescout.screenplay.domain.sample.ScreenplayIdsSample
import cinescout.screenplay.domain.sample.TmdbScreenplayIdSample
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.ktor.client.engine.mock.MockEngine
import screenplay.data.remote.trakt.mock.TraktScreenplayMockEngine
import screenplay.data.remote.trakt.mock.addSimilarHandler
import screenplay.data.remote.trakt.res.TraktExtendedScreenplayJson

class RealTraktScreenplayServiceTest : BehaviorSpec({

    Given("service") {
        val screenplayId = ScreenplayIdsSample.BreakingBad.trakt
        val scenario = TestScenario {
            addSimilarHandler(
                screenplayId = screenplayId,
                responseJson = jsonArrayOf(TraktExtendedScreenplayJson.Sherlock, TraktExtendedScreenplayJson.Dexter)
            )
        }

        When("get similar tv shows") {
            val result = scenario.sut.getSimilar(screenplayId, page = 0)

            Then("the similar tv shows are returned") {
                result.getOrNull()?.map { it.tmdbId } shouldBe listOf(
                    TmdbScreenplayIdSample.Sherlock,
                    TmdbScreenplayIdSample.Dexter
                )
            }
        }
    }
})

private class RealTraktScreenplayServiceTestScenario(
    val sut: RealTraktScreenplayService
)

private fun TestScenario(engineConfig: MockEngine.() -> Unit = {}): RealTraktScreenplayServiceTestScenario {
    val client = CineScoutTraktClient(
        engine = TraktScreenplayMockEngine().apply(engineConfig),
        logBody = true,
    )
    return RealTraktScreenplayServiceTestScenario(
        sut = RealTraktScreenplayService(client)
    )
}
