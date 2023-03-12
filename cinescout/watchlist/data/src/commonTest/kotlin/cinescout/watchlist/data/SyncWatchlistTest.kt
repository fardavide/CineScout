package cinescout.watchlist.data

import app.cash.turbine.test
import arrow.core.left
import arrow.core.right
import cinescout.error.NetworkError
import cinescout.lists.domain.ListType
import cinescout.screenplay.domain.model.Movie
import cinescout.screenplay.domain.model.Screenplay
import cinescout.screenplay.domain.model.TvShow
import cinescout.screenplay.domain.sample.ScreenplaySample
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.Flow

class SyncWatchlistTest : BehaviorSpec({

    Given("connected") {
        val isConnected = true

        And("type is all") {
            val listType = ListType.All

            When("both calls are success") {
                val scenario = TestScenario(
                    isConnected = isConnected,
                    movies = listOf(ScreenplaySample.Inception),
                    tvShow = listOf(ScreenplaySample.BreakingBad)
                )
                val result = scenario.sut(listType, page = 1)

                Then("unit is returned") {
                    result shouldBe Unit.right()
                }
                
                And("items are inserted") {
                    scenario.cachedWatchlist.test {
                        awaitItem() shouldContainExactly listOf(
                            ScreenplaySample.Inception,
                            ScreenplaySample.BreakingBad
                        )
                    }
                }
            }

            When("movies call is not found") {
                val scenario = TestScenario(
                    isConnected = isConnected,
                    movies = listOf(ScreenplaySample.Inception),
                    tvShow = listOf(ScreenplaySample.BreakingBad, ScreenplaySample.Dexter)
                )
                val result = scenario.sut(listType, page = 2)

                Then("unit is returned") {
                    result shouldBe Unit.right()
                }

                And("items are inserted") {
                    scenario.cachedWatchlist.test {
                        awaitItem() shouldContainExactly listOf(
                            ScreenplaySample.Dexter
                        )
                    }
                }
            }

            When("tv show call is not found") {
                val scenario = TestScenario(
                    isConnected = isConnected,
                    movies = listOf(ScreenplaySample.Inception, ScreenplaySample.TheWolfOfWallStreet),
                    tvShow = listOf(ScreenplaySample.BreakingBad)
                )
                val result = scenario.sut(listType, page = 2)

                Then("unit is returned") {
                    result shouldBe Unit.right()
                }

                And("items are inserted") {
                    scenario.cachedWatchlist.test {
                        awaitItem() shouldContainExactly listOf(
                            ScreenplaySample.TheWolfOfWallStreet
                        )
                    }
                }
            }

            When("both calls are not found") {
                val scenario = TestScenario(
                    isConnected = isConnected,
                    movies = listOf(ScreenplaySample.Inception),
                    tvShow = listOf(ScreenplaySample.BreakingBad)
                )
                val result = scenario.sut(listType, page = 2)

                Then("not found is returned") {
                    result shouldBe NetworkError.NotFound.left()
                }

                And("items are not inserted") {
                    scenario.cachedWatchlist.test {
                        awaitItem().shouldBeEmpty()
                    }
                }
            }
        }

        And("type is movies") {
            val listType = ListType.Movies

            When("movies call is success") {
                val scenario = TestScenario(
                    isConnected = isConnected,
                    movies = listOf(ScreenplaySample.Inception),
                    tvShow = listOf(ScreenplaySample.BreakingBad)
                )
                val result = scenario.sut(listType, page = 1)

                Then("unit is returned") {
                    result shouldBe Unit.right()
                }

                And("items are inserted") {
                    scenario.cachedWatchlist.test {
                        awaitItem() shouldContainExactly listOf(
                            ScreenplaySample.Inception
                        )
                    }
                }
            }

            When("movies call is not found") {
                val scenario = TestScenario(
                    isConnected = isConnected,
                    movies = listOf(ScreenplaySample.Inception),
                    tvShow = listOf(ScreenplaySample.BreakingBad, ScreenplaySample.Dexter)
                )
                val result = scenario.sut(listType, page = 2)

                Then("not found is returned") {
                    result shouldBe NetworkError.NotFound.left()
                }

                And("items are not inserted") {
                    scenario.cachedWatchlist.test {
                        awaitItem().shouldBeEmpty()
                    }
                }
            }
        }

        And("type is tv shows") {
            val listType = ListType.TvShows

            When("tv show call is success") {
                val scenario = TestScenario(
                    isConnected = isConnected,
                    movies = listOf(ScreenplaySample.Inception),
                    tvShow = listOf(ScreenplaySample.BreakingBad)
                )
                val result = scenario.sut(listType, page = 1)

                Then("unit is returned") {
                    result shouldBe Unit.right()
                }

                And("items are inserted") {
                    scenario.cachedWatchlist.test {
                        awaitItem() shouldContainExactly listOf(
                            ScreenplaySample.BreakingBad
                        )
                    }
                }
            }

            When("tv show call is not found") {
                val scenario = TestScenario(
                    isConnected = isConnected,
                    movies = listOf(ScreenplaySample.Inception, ScreenplaySample.TheWolfOfWallStreet),
                    tvShow = listOf(ScreenplaySample.BreakingBad)
                )
                val result = scenario.sut(listType, page = 2)

                Then("not found is returned") {
                    result shouldBe NetworkError.NotFound.left()
                }

                And("items are not inserted") {
                    scenario.cachedWatchlist.test {
                        awaitItem().shouldBeEmpty()
                    }
                }
            }
        }
    }

    Given("not connected") {
        val isConnected = false

        When("type is all") {
            val listType = ListType.All
            val scenario = TestScenario(
                isConnected = isConnected,
                movies = listOf(ScreenplaySample.Inception),
                tvShow = listOf(ScreenplaySample.BreakingBad)
            )
            val result = scenario.sut(listType, page = 1)

            Then("unit is returned") {
                result shouldBe Unit.right()
            }

            And("items are not inserted") {
                scenario.cachedWatchlist.test {
                    awaitItem().shouldBeEmpty()
                }
            }
        }
    }
})

private class SyncWatchlistTestScenario(
    val sut: SyncWatchlist,
    val cachedWatchlist: Flow<List<Screenplay>>
)

private fun TestScenario(
    isConnected: Boolean,
    movies: List<Movie>,
    tvShow: List<TvShow>
): SyncWatchlistTestScenario {
    val localDataSource = FakeLocalWatchlistDataSource()
    return SyncWatchlistTestScenario(
        sut = SyncWatchlist(
            localDataSource = localDataSource,
            remoteDataSource = FakeRemoteWatchlistDataSource(
                isConnected = isConnected,
                movies = movies,
                pageSize = 1,
                tvShows = tvShow
            )
        ),
        cachedWatchlist = localDataSource.watchlist
    )
}
