package cinescout.screenplay.data.repository

import app.cash.turbine.test
import arrow.core.Either
import arrow.core.left
import cinescout.error.DataError
import cinescout.error.NetworkError
import cinescout.screenplay.data.datasource.FakeLocalScreenplayDataSource
import cinescout.screenplay.data.datasource.FakeRemoteScreenplayDataSource
import cinescout.screenplay.domain.sample.ScreenplaySample
import cinescout.screenplay.domain.sample.TmdbScreenplayIdSample
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldContainOnly
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import store.Refresh
import store.test.MockStoreOwner

class RealScreenplayRepositoryTest : BehaviorSpec({

    val noNetwork = DataError.Remote(NetworkError.NoNetwork).left()

    Given("no cache") {
        val hasCache = false

        And("network is not available") {
            val hasNetwork = false

            When("get recommended ids") {

                And("refresh is never") {
                    val scenario = TestScenario(hasCache = hasCache, hasNetwork = hasNetwork)
                    val refresh = Refresh.Never

                    Then("no cache error is emitted") {
                        scenario.sut.getRecommendedIds(refresh).test {
                            awaitItem() shouldBe DataError.Local.NoCache.left()
                        }
                    }
                }

                And("refresh is if needed") {
                    val scenario = TestScenario(hasCache = hasCache, hasNetwork = hasNetwork)
                    val refresh = Refresh.IfNeeded

                    Then("no network error is emitted") {
                        scenario.sut.getRecommendedIds(refresh).test {
                            awaitItem() shouldBe noNetwork
                        }
                    }
                }

                And("refresh is once") {
                    val scenario = TestScenario(hasCache = hasCache, hasNetwork = hasNetwork)
                    val refresh = Refresh.Once

                    Then("no network error is emitted") {
                        scenario.sut.getRecommendedIds(refresh).test {
                            awaitItem() shouldBe noNetwork
                        }
                    }
                }
            }
        }
        
        And("network is available") {
            val hasNetwork = true

            When("get recommended ids") {

                And("refresh is never") {
                    val scenario = TestScenario(hasCache = hasCache, hasNetwork = hasNetwork)
                    val refresh = Refresh.Never

                    Then("no cache error is emitted") {
                        scenario.sut.getRecommendedIds(refresh).test {
                            awaitItem() shouldBe DataError.Local.NoCache.left()
                        }
                    }
                }

                And("refresh is if needed") {
                    val scenario = TestScenario(hasCache = hasCache, hasNetwork = hasNetwork)
                    val refresh = Refresh.IfNeeded

                    Then("remote data is emitted") {
                        scenario.sut.getRecommendedIds(refresh).test {
                            awaitItem().getOrNull() shouldContainOnly AllRecommendedIds
                        }
                    }
                }

                And("refresh is once") {
                    val scenario = TestScenario(hasCache = hasCache, hasNetwork = hasNetwork)
                    val refresh = Refresh.Once

                    Then("remote data is emitted") {
                        scenario.sut.getRecommendedIds(refresh).test {
                            awaitItem().getOrNull() shouldContainOnly AllRecommendedIds
                        }
                    }
                }
            }
        }
    }

    Given("cache") {
        val hasCache = true

        And("network is not available") {
            val hasNetwork = false

            When("get recommended ids") {

                And("refresh is never") {
                    val scenario = TestScenario(hasCache = hasCache, hasNetwork = hasNetwork)
                    val refresh = Refresh.Never

                    Then("cache data is emitted") {
                        scenario.sut.getRecommendedIds(refresh).test {
                            awaitItem().let { either ->
                                either.shouldBeInstanceOf<Either.Right<*>>()
                                either.getOrNull() shouldContainOnly AllRecommendedIds
                            }
                        }
                    }
                }

                And("refresh is if needed") {
                    val scenario = TestScenario(hasCache = hasCache, hasNetwork = hasNetwork)
                    val refresh = Refresh.IfNeeded

                    Then("cache data is emitted") {
                        scenario.sut.getRecommendedIds(refresh).test {
                            awaitItem().let { either ->
                                either.shouldBeInstanceOf<Either.Right<*>>()
                                either.getOrNull() shouldContainOnly AllRecommendedIds
                            }
                        }
                    }
                }

                And("refresh is once") {
                    val scenario = TestScenario(hasCache = hasCache, hasNetwork = hasNetwork)
                    val refresh = Refresh.Once

                    scenario.sut.getRecommendedIds(refresh).test {
                        Then("cached data is emitted") {
                            awaitItem().getOrNull() shouldBe AllRecommendedIds
                        }
                        Then("no network error is emitted") {
                            awaitItem() shouldBe noNetwork
                        }
                    }
                }
            }
        }

        And("network is available") {
            val hasNetwork = true

            When("get recommended ids") {

                And("refresh is never") {
                    val scenario = TestScenario(hasCache = hasCache, hasNetwork = hasNetwork)
                    val refresh = Refresh.Never

                    Then("data is emitted") {
                        scenario.sut.getRecommendedIds(refresh).test {
                            awaitItem().getOrNull() shouldContainOnly AllRecommendedIds
                        }
                    }
                }

                And("refresh is if needed") {
                    val scenario = TestScenario(hasCache = hasCache, hasNetwork = hasNetwork)
                    val refresh = Refresh.IfNeeded

                    Then("data is emitted") {
                        scenario.sut.getRecommendedIds(refresh).test {
                            awaitItem().getOrNull() shouldContainOnly AllRecommendedIds
                        }
                    }
                }

                And("refresh is once") {
                    val scenario = TestScenario(hasCache = hasCache, hasNetwork = hasNetwork)
                    val refresh = Refresh.Once

                    Then("data is emitted") {
                        scenario.sut.getRecommendedIds(refresh).test {
                            awaitItem().getOrNull() shouldContainOnly AllRecommendedIds
                        }
                    }
                }
            }
        }
    }
})

private val AllRecommended = listOf(
    ScreenplaySample.BreakingBad,
    ScreenplaySample.Dexter,
    ScreenplaySample.Grimm,
    ScreenplaySample.Inception,
    ScreenplaySample.TheWolfOfWallStreet,
    ScreenplaySample.War
)

private val AllRecommendedIds = AllRecommended.map { it.tmdbId }

private class RealScreenplayRepositoryTestScenario(
    val sut: RealScreenplayRepository
)

private fun TestScenario(hasCache: Boolean, hasNetwork: Boolean): RealScreenplayRepositoryTestScenario {
    val storeOwnerMode = when (hasCache) {
        true -> MockStoreOwner.Mode.Updated
        false -> MockStoreOwner.Mode.Fresh
    }
    val remoteRecommended = listOf(
        TmdbScreenplayIdSample.BreakingBad,
        TmdbScreenplayIdSample.Dexter,
        TmdbScreenplayIdSample.Grimm,
        TmdbScreenplayIdSample.Inception,
        TmdbScreenplayIdSample.TheWolfOfWallStreet,
        TmdbScreenplayIdSample.War
    )
    return RealScreenplayRepositoryTestScenario(
        sut = RealScreenplayRepository(
            localDataSource = FakeLocalScreenplayDataSource(
                recommended = if (hasCache) AllRecommended else emptyList()
            ),
            remoteDataSource = FakeRemoteScreenplayDataSource(
                hasNetwork = hasNetwork,
                recommended = remoteRecommended
            ),
            storeOwner = MockStoreOwner(mode = storeOwnerMode)
        )
    )
}
