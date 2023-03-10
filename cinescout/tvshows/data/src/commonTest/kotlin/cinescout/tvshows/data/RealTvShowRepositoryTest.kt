package cinescout.tvshows.data

import app.cash.turbine.test
import arrow.core.right
import cinescout.tvshows.data.store.FakeRatedTvShowIdsStore
import cinescout.tvshows.data.store.FakeRatedTvShowsStore
import cinescout.tvshows.data.store.FakeTvShowDetailsStore
import cinescout.tvshows.data.store.FakeWatchlistTvShowIdsStore
import cinescout.tvshows.data.store.FakeWatchlistTvShowsStore
import cinescout.tvshows.domain.model.TmdbTvShowId
import cinescout.tvshows.domain.model.TvShow
import cinescout.tvshows.domain.model.TvShowIdWithPersonalRating
import cinescout.tvshows.domain.model.TvShowWithPersonalRating
import cinescout.tvshows.domain.sample.TmdbTvShowIdSample
import cinescout.tvshows.domain.sample.TvShowIdWithPersonalRatingSample
import cinescout.tvshows.domain.sample.TvShowSample
import cinescout.tvshows.domain.sample.TvShowWithDetailsSample
import cinescout.tvshows.domain.sample.TvShowWithPersonalRatingSample
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import store.test.MockStoreOwner

class RealTvShowRepositoryTest : BehaviorSpec({
    coroutineTestScope = true

    Given("no cache") {
        val freshMode = MockStoreOwner.Mode.Fresh

        When("get rated tv shows") {
            val tvShowIds = listOf(
                TvShowIdWithPersonalRatingSample.BreakingBad,
                TvShowIdWithPersonalRatingSample.Dexter
            )
            val ratedTvShows = listOf(
                TvShowWithPersonalRatingSample.BreakingBad,
                TvShowWithPersonalRatingSample.Dexter
            )

            And("refresh is once") {
                val scenario = TestScenario(
                    remoteRatedTvShowIds = tvShowIds,
                    ratedTvShows = ratedTvShows,
                    storeOwnerMode = freshMode
                )

                Then("rated tv shows are emitted from remote source") {
                    scenario.sut.getAllRatedTvShows(refresh = true).test {
                        awaitItem().dataOrNull() shouldBe ratedTvShows.right()
                        awaitComplete()
                    }
                }
            }
        }

        When("get watchlist tv shows") {
            val tvShowIds = listOf(
                TmdbTvShowIdSample.BreakingBad,
                TmdbTvShowIdSample.Dexter
            )
            val watchlistTvShows = listOf(
                TvShowSample.BreakingBad,
                TvShowSample.Dexter
            )

            And("refresh is once") {
                val scenario = TestScenario(
                    remoteWatchlistTvShowIds = tvShowIds,
                    storeOwnerMode = freshMode,
                    watchlistTvShows = watchlistTvShows
                )

                Then("watchlist tv shows are emitted from remote source") {
                    scenario.sut.getAllWatchlistTvShows(refresh = true).test {
                        awaitItem().dataOrNull() shouldBe watchlistTvShows.right()
                        awaitComplete()
                    }
                }
            }
        }
    }
})

private class RealTvShowRepositoryTestScenario(
    val sut: RealTvShowRepository
)

private fun TestScenario(
    ratedTvShows: List<TvShowWithPersonalRating>? = null,
    remoteRatedTvShowIds: List<TvShowIdWithPersonalRating>? = null,
    remoteWatchlistTvShowIds: List<TmdbTvShowId>? = null,
    storeOwnerMode: MockStoreOwner.Mode,
    watchlistTvShows: List<TvShow>? = null
): RealTvShowRepositoryTestScenario {
    val remoteTvShowsDetails = listOf(
        TvShowWithDetailsSample.BreakingBad,
        TvShowWithDetailsSample.Dexter,
        TvShowWithDetailsSample.Grimm
    )
    return RealTvShowRepositoryTestScenario(
        sut = RealTvShowRepository(
            localTvShowDataSource = FakeLocalTvShowDataSource(),
            ratedTvShowIdsStore = FakeRatedTvShowIdsStore(tvShows = ratedTvShows),
            ratedTvShowsStore = FakeRatedTvShowsStore(tvShows = ratedTvShows),
            remoteTvShowDataSource = FakeRemoteTvShowDataSource(
                ratedTvShowIds = remoteRatedTvShowIds,
                tvShowsDetails = remoteTvShowsDetails,
                watchlistTvShowIds = remoteWatchlistTvShowIds
            ),
            storeOwner = MockStoreOwner(mode = storeOwnerMode),
            tvShowDetailsStore = FakeTvShowDetailsStore(tvShowsDetails = remoteTvShowsDetails),
            watchlistTvShowIdsStore = FakeWatchlistTvShowIdsStore(tvShows = watchlistTvShows),
            watchlistTvShowsStore = FakeWatchlistTvShowsStore(tvShows = watchlistTvShows)
        )
    )
}
