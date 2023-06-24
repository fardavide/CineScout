package cinescout.watchlist.data.remote.service

import arrow.core.right
import cinescout.network.trakt.CineScoutTraktClient
import cinescout.screenplay.domain.model.ScreenplayTypeFilter
import cinescout.watchlist.data.remote.mock.TraktWatchlistMockEngine
import cinescout.watchlist.data.remote.sample.TraktScreenplayWatchlistExtendedBodySample
import cinescout.watchlist.data.remote.sample.TraktScreenplayWatchlistMetadataBodySample
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

class TraktWatchlistServiceTest : BehaviorSpec({

    Given("logged in") {
        val scenario = TestScenario()

        When("get watchlist ids") {

            And("type is all") {
                val result = scenario.sut.getAllWatchlistIds(ScreenplayTypeFilter.All)

                Then("should return watchlist") {
                    result shouldBe listOf(
                        TraktScreenplayWatchlistMetadataBodySample.Inception,
                        TraktScreenplayWatchlistMetadataBodySample.BreakingBad
                    ).right()
                }
            }

            And("type is movies") {
                val result = scenario.sut.getAllWatchlistIds(ScreenplayTypeFilter.Movies)

                Then("should return watchlist") {
                    result shouldBe listOf(
                        TraktScreenplayWatchlistMetadataBodySample.Inception
                    ).right()
                }
            }

            And("type is tv shows") {
                val result = scenario.sut.getAllWatchlistIds(ScreenplayTypeFilter.TvShows)

                Then("should return watchlist") {
                    result shouldBe listOf(
                        TraktScreenplayWatchlistMetadataBodySample.BreakingBad
                    ).right()
                }
            }
        }

        When("get watchlist") {

            And("type is all") {
                val result = scenario.sut.getWatchlist(ScreenplayTypeFilter.All, 1)

                Then("should return watchlist") {
                    result shouldBe listOf(
                        TraktScreenplayWatchlistExtendedBodySample.Inception,
                        TraktScreenplayWatchlistExtendedBodySample.BreakingBad
                    ).right()
                }
            }

            And("type is movies") {
                val result = scenario.sut.getWatchlist(ScreenplayTypeFilter.Movies, 1)

                Then("should return watchlist") {
                    result shouldBe listOf(
                        TraktScreenplayWatchlistExtendedBodySample.Inception
                    ).right()
                }
            }

            And("type is tv shows") {
                val result = scenario.sut.getWatchlist(ScreenplayTypeFilter.TvShows, 1)

                Then("should return watchlist") {
                    result shouldBe listOf(
                        TraktScreenplayWatchlistExtendedBodySample.BreakingBad
                    ).right()
                }
            }
        }
    }
})

private class TraktWatchlistServiceTestScenario(
    val sut: TraktWatchlistService
)

private fun TestScenario(): TraktWatchlistServiceTestScenario {
    val engine = TraktWatchlistMockEngine(forceLoggedIn = true)
    val client = CineScoutTraktClient(engine)
    return TraktWatchlistServiceTestScenario(
        sut = TraktWatchlistService(client = client)
    )
}
