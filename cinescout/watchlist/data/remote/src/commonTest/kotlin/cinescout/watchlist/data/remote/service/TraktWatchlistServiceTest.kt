package cinescout.watchlist.data.remote.service

import arrow.core.right
import cinescout.lists.data.remote.mapper.TraktListSortingMapper
import cinescout.lists.domain.ListSorting
import cinescout.network.trakt.CineScoutTraktClient
import cinescout.screenplay.domain.model.ScreenplayType
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
                val result = scenario.sut.getAllWatchlistIds(ScreenplayType.All)

                Then("should return watchlist") {
                    result shouldBe listOf(
                        TraktScreenplayWatchlistMetadataBodySample.Inception,
                        TraktScreenplayWatchlistMetadataBodySample.BreakingBad
                    ).right()
                }
            }

            And("type is movies") {
                val result = scenario.sut.getAllWatchlistIds(ScreenplayType.Movies)

                Then("should return watchlist") {
                    result shouldBe listOf(
                        TraktScreenplayWatchlistMetadataBodySample.Inception
                    ).right()
                }
            }

            And("type is tv shows") {
                val result = scenario.sut.getAllWatchlistIds(ScreenplayType.TvShows)

                Then("should return watchlist") {
                    result shouldBe listOf(
                        TraktScreenplayWatchlistMetadataBodySample.BreakingBad
                    ).right()
                }
            }
        }

        When("get watchlist") {
            val sorting = ListSorting.Rating.Descending

            And("type is all") {
                val result = scenario.sut.getWatchlist(sorting, ScreenplayType.All, 1)

                Then("should return watchlist") {
                    result shouldBe listOf(
                        TraktScreenplayWatchlistExtendedBodySample.Inception,
                        TraktScreenplayWatchlistExtendedBodySample.BreakingBad
                    ).right()
                }
            }

            And("type is movies") {
                val result = scenario.sut.getWatchlist(sorting, ScreenplayType.Movies, 1)

                Then("should return watchlist") {
                    result shouldBe listOf(
                        TraktScreenplayWatchlistExtendedBodySample.Inception
                    ).right()
                }
            }

            And("type is tv shows") {
                val result = scenario.sut.getWatchlist(sorting, ScreenplayType.TvShows, 1)

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
    val traktListSortingMapper = TraktListSortingMapper()
    return TraktWatchlistServiceTestScenario(
        sut = TraktWatchlistService(client = client, traktListSortingMapper = traktListSortingMapper)
    )
}
