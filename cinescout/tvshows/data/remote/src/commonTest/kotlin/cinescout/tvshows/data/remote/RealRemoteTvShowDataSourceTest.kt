package cinescout.tvshows.data.remote

import arrow.core.left
import arrow.core.right
import cinescout.auth.domain.usecase.FakeCallWithCurrentUser
import cinescout.model.NetworkOperation
import cinescout.tvshows.domain.model.TvShow
import cinescout.tvshows.domain.sample.TvShowSample
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import store.Paging
import store.builder.toPagedData

class RealRemoteTvShowDataSourceTest : BehaviorSpec({

    val page = Paging.Page.Initial

    Given("Tmdb account is linked") {
        val tvShows = listOf(
            TvShowSample.BreakingBad,
            TvShowSample.Grimm
        )
        val scenario = TestScenario(
            watchlistTvShows = tvShows,
            isTmdbLinked = true,
            isTraktLinked = false
        )

        When("get watchlist") {
            val result = scenario.sut.getWatchlistTvShows(page)

            Then("tv shows are returned") {
                result shouldBe tvShows.map { it.tmdbId }.toPagedData(page).right()
            }
        }
    }

    Given("Trakt account is linked") {
        val tvShows = listOf(
            TvShowSample.BreakingBad,
            TvShowSample.Grimm
        )
        val scenario = TestScenario(
            watchlistTvShows = tvShows,
            isTmdbLinked = false,
            isTraktLinked = true
        )

        When("get watchlist") {
            val result = scenario.sut.getWatchlistTvShows(page)

            Then("tv shows are returned") {
                result shouldBe tvShows.map { it.tmdbId }.toPagedData(page).right()
            }
        }
    }

    Given("no account is linked") {
        val scenario = TestScenario(isTmdbLinked = false, isTraktLinked = false)

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

private fun TestScenario(
    isTmdbLinked: Boolean,
    isTraktLinked: Boolean,
    watchlistTvShows: List<TvShow>? = null
) = RealRemoteTvShowDataSourceTestScenario(
    sut = RealRemoteTvShowDataSource(
        callWithCurrentUser = FakeCallWithCurrentUser(isTmdbLinked = isTmdbLinked, isTraktLinked = isTraktLinked),
        tmdbSource = FakeTmdbRemoteTvShowDataSource(watchlistTvShows = watchlistTvShows),
        traktSource = FakeTraktRemoteTvShowDataSource(watchlistTvShows = watchlistTvShows)
    )
)
