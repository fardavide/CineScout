package cinescout.tvshows.data

import app.cash.turbine.test
import arrow.core.left
import arrow.core.right
import cinescout.error.DataError
import cinescout.tvshows.domain.model.TmdbTvShowId
import cinescout.tvshows.domain.model.TvShowIdWithPersonalRating
import cinescout.tvshows.domain.sample.TmdbTvShowIdSample
import cinescout.tvshows.domain.sample.TvShowIdWithPersonalRatingSample
import cinescout.tvshows.domain.sample.TvShowSample
import cinescout.tvshows.domain.sample.TvShowWithDetailsSample
import cinescout.tvshows.domain.sample.TvShowWithPersonalRatingSample
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import store.Refresh
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

            And("refresh is never") {
                val scenario = TestScenario(
                    remoteRatedTvShowIds = tvShowIds,
                    storeOwnerMode = freshMode
                )

                Then("no cache is emitted") {
                    scenario.sut.getAllRatedTvShows(Refresh.Never).test {
                        awaitItem() shouldBe DataError.Local.NoCache.left()
                    }
                }
            }

            And("refresh is once") {
                val scenario = TestScenario(
                    remoteRatedTvShowIds = tvShowIds,
                    storeOwnerMode = freshMode
                )

                Then("rated tv shows are emitted from remote source") {
                    scenario.sut.getAllRatedTvShows(Refresh.Once).test {
                        awaitItem() shouldBe ratedTvShows.right()
                    }
                }
            }

            And("refresh is if needed") {
                val scenario = TestScenario(
                    remoteRatedTvShowIds = tvShowIds,
                    storeOwnerMode = freshMode
                )

                Then("rated tv shows are emitted from remote source") {
                    scenario.sut.getAllRatedTvShows(Refresh.IfNeeded).test {
                        awaitItem() shouldBe ratedTvShows.right()
                    }
                }
            }

            And("refresh is if expired") {
                val scenario = TestScenario(
                    remoteRatedTvShowIds = tvShowIds,
                    storeOwnerMode = freshMode
                )

                Then("rated tv shows are emitted from remote source") {
                    scenario.sut.getAllRatedTvShows(Refresh.IfExpired()).test {
                        awaitItem() shouldBe ratedTvShows.right()
                    }
                }
            }

            And("refresh is with interval") {
                val scenario = TestScenario(
                    remoteRatedTvShowIds = tvShowIds,
                    storeOwnerMode = freshMode
                )

                Then("rated tv shows are emitted from remote source") {
                    scenario.sut.getAllRatedTvShows(Refresh.WithInterval()).test {
                        awaitItem() shouldBe ratedTvShows.right()
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

            And("refresh is never") {
                val scenario = TestScenario(
                    remoteWatchlistTvShowIds = tvShowIds,
                    storeOwnerMode = freshMode
                )

                Then("no cache is emitted") {
                    scenario.sut.getAllWatchlistTvShows(Refresh.Never).test {
                        awaitItem() shouldBe DataError.Local.NoCache.left()
                    }
                }
            }

            And("refresh is once") {
                val scenario = TestScenario(
                    remoteWatchlistTvShowIds = tvShowIds,
                    storeOwnerMode = freshMode
                )

                Then("watchlist tv shows are emitted from remote source") {
                    scenario.sut.getAllWatchlistTvShows(Refresh.Once).test {
                        awaitItem() shouldBe watchlistTvShows.right()
                    }
                }
            }

            And("refresh is if needed") {
                val scenario = TestScenario(
                    remoteWatchlistTvShowIds = tvShowIds,
                    storeOwnerMode = freshMode
                )

                Then("watchlist tv shows are emitted from remote source") {
                    scenario.sut.getAllWatchlistTvShows(Refresh.IfNeeded).test {
                        awaitItem() shouldBe watchlistTvShows.right()
                    }
                }
            }

            And("refresh is if expired") {
                val scenario = TestScenario(
                    remoteWatchlistTvShowIds = tvShowIds,
                    storeOwnerMode = freshMode
                )

                Then("watchlist tv shows are emitted from remote source") {
                    scenario.sut.getAllWatchlistTvShows(Refresh.IfExpired()).test {
                        awaitItem() shouldBe watchlistTvShows.right()
                    }
                }
            }

            And("refresh is with interval") {
                val scenario = TestScenario(
                    remoteWatchlistTvShowIds = tvShowIds,
                    storeOwnerMode = freshMode
                )

                Then("watchlist tv shows are emitted from remote source") {
                    scenario.sut.getAllWatchlistTvShows(Refresh.WithInterval()).test {
                        awaitItem() shouldBe watchlistTvShows.right()
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
    remoteRatedTvShowIds: List<TvShowIdWithPersonalRating>? = null,
    remoteWatchlistTvShowIds: List<TmdbTvShowId>? = null,
    storeOwnerMode: MockStoreOwner.Mode
): RealTvShowRepositoryTestScenario {
    val remoteTvShowsDetails = listOf(
        TvShowWithDetailsSample.BreakingBad,
        TvShowWithDetailsSample.Dexter,
        TvShowWithDetailsSample.Grimm
    )
    return RealTvShowRepositoryTestScenario(
        sut = RealTvShowRepository(
            localTvShowDataSource = FakeLocalTvShowDataSource(),
            remoteTvShowDataSource = FakeRemoteTvShowDataSource(
                ratedTvShowIds = remoteRatedTvShowIds,
                tvShowsDetails = remoteTvShowsDetails,
                watchlistTvShowIds = remoteWatchlistTvShowIds
            ),
            storeOwner = MockStoreOwner(mode = storeOwnerMode)
        )
    )
}
