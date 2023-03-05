package cinescout.tvshows.data.remote

import arrow.core.left
import arrow.core.right
import cinescout.auth.domain.usecase.FakeCallWithTraktAccount
import cinescout.model.NetworkOperation
import cinescout.tvshows.domain.model.TvShow
import cinescout.tvshows.domain.sample.TvShowSample
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import store.Paging
import store.builder.toRemotePagedData

class RealRemoteTvShowDataSourceTest : BehaviorSpec({

    val page = Paging.Page.Initial

    Given("Trakt account is linked") {
        val tvShows = listOf(
            TvShowSample.BreakingBad,
            TvShowSample.Grimm
        )
        val scenario = TestScenario(
            isTraktLinked = true,
            watchlistTvShows = tvShows
        )

        When("get watchlist") {
            val result = scenario.sut.getWatchlistTvShows(page)

            Then("tv shows are returned") {
                result shouldBe tvShows.map { it.tmdbId }.toRemotePagedData(page).right()
            }
        }
    }

    Given("no account is linked") {
        val scenario = TestScenario(isTraktLinked = false)

        When("get watchlist") {

            Then("skipped is returned") {
                scenario.sut.getWatchlistTvShows(page) shouldBe NetworkOperation.Skipped.left()
            }
        }
    }
})

private class RealRemoteTvShowDataSourceTestScenario(
    val sut: RealRemoteTvShowDataSource
)

private fun TestScenario(isTraktLinked: Boolean, watchlistTvShows: List<TvShow>? = null) =
    RealRemoteTvShowDataSourceTestScenario(
        sut = RealRemoteTvShowDataSource(
            callWithTraktAccount = FakeCallWithTraktAccount(isLinked = isTraktLinked),
            tmdbSource = FakeTmdbRemoteTvShowDataSource(),
            traktSource = FakeTraktRemoteTvShowDataSource(watchlistTvShows = watchlistTvShows)
        )
    )
