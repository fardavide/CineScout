package screenplay.data.remote.trakt.service

import arrow.core.right
import cinescout.network.testutil.jsonArrayOf
import cinescout.network.trakt.CineScoutTraktClient
import cinescout.screenplay.domain.model.TmdbScreenplayId
import cinescout.screenplay.domain.sample.ScreenplayIdsSample
import cinescout.screenplay.domain.sample.TmdbScreenplayIdSample
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.ktor.client.engine.mock.MockEngine
import screenplay.data.remote.trakt.mock.TraktScreenplayMockEngine
import screenplay.data.remote.trakt.mock.addSimilarHandler
import screenplay.data.remote.trakt.res.TraktExtendedScreenplayJson
import screenplay.data.remote.trakt.sample.TraktScreenplayExtendedBodySample

class RealTraktScreenplayServiceTest : BehaviorSpec({

    Given("service") {
        val screenplayId = ScreenplayIdsSample.BreakingBad.trakt
        val scenario = TestScenario {
            addSimilarHandler(
                screenplayId = screenplayId,
                responseJson = jsonArrayOf(TraktExtendedScreenplayJson.Sherlock, TraktExtendedScreenplayJson.Dexter)
            )
        }

        When("get a tv show") {
            val result = scenario.sut.getScreenplay(screenplayId)

            Then("the tv show is returned") {
                result shouldBe TraktScreenplayExtendedBodySample.BreakingBad.right()
            }
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

    Given("a Tv Show without Tmdb id") {
        val screenplayId = ScreenplayIdsSample.BreakingBad.trakt
        val scenario = TestScenario {
            addSimilarHandler(
                screenplayId = screenplayId,
                responseJson = jsonArrayOf(
                    TraktExtendedScreenplayJson.Sherlock,
                    TraktExtendedScreenplayJson.TomAndJerry
                )
            )
        }

        When("get similar tv shows") {
            val result = scenario.sut.getSimilar(screenplayId, page = 0)

            Then("an invalid Tmdb id is assigned to the tv shows without Tmdb") {
                result.getOrNull()?.map { it.tmdbId } shouldBe listOf(
                    TmdbScreenplayIdSample.Sherlock,
                    TmdbScreenplayId.invalid()
                )
            }
        }
    }
})

private class RealTraktScreenplayServiceTestScenario(
    val sut: RealTraktScreenplayService
)

private fun TestScenario(
    engineConfig: MockEngine.() -> Unit = {
    }
): RealTraktScreenplayServiceTestScenario {
    val client = CineScoutTraktClient(
        engine = TraktScreenplayMockEngine().apply(engineConfig),
        logBody = true
    )
    return RealTraktScreenplayServiceTestScenario(
        sut = RealTraktScreenplayService(client)
    )
}
