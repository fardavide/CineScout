package cinescout.fetchdata.data.repository

import cinescout.FakeGetCurrentDateTime
import cinescout.fetchdata.data.datasource.FakeFetchDataDataSource
import cinescout.fetchdata.domain.model.FetchData
import cinescout.fetchdata.domain.model.Page
import cinescout.sample.DateTimeSample
import cinescout.screenplay.domain.model.ScreenplayType
import cinescout.screenplay.domain.model.ScreenplayTypeFilter
import cinescout.screenplay.domain.sample.ScreenplayIdsSample
import cinescout.screenplay.domain.sample.TmdbScreenplayIdSample
import cinescout.screenplay.domain.sample.TraktScreenplayIdSample
import cinescout.utils.kotlin.toTimeSpan
import io.kotest.assertions.throwables.shouldThrowWithMessage
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import korlibs.time.DateTime
import kotlin.time.Duration.Companion.days

class RealFetchDataRepositoryTest : BehaviorSpec({

    val currentTime = DateTimeSample.Xmas2023

    Given("getting fetch data") {
        val scenario = TestScenario(currentTime)

        When("key is TmdbScreenplayId") {
            val key = TmdbScreenplayIdSample.Dexter

            Then("forbidden key exception is thrown") {
                shouldThrowWithMessage<IllegalArgumentException>(
                    message = "Key cannot be of type TmdbTvShowId, to avoid collisions with other entities",
                    block = { scenario.sut.get(key, 1.days) }
                )
            }
        }

        When("key is TraktScreenplayId") {
            val key = TraktScreenplayIdSample.Dexter

            Then("forbidden key exception is thrown") {
                shouldThrowWithMessage<IllegalArgumentException>(
                    message = "Key cannot be of type TraktTvShowId, to avoid collisions with other entities",
                    block = { scenario.sut.get(key, 1.days) }
                )
            }
        }

        When("key is ScreenplayIds") {
            val key = ScreenplayIdsSample.Dexter

            Then("forbidden key exception is thrown") {
                shouldThrowWithMessage<IllegalArgumentException>(
                    message = "Key cannot be of type TvShowIds, to avoid collisions with other entities",
                    block = { scenario.sut.get(key, 1.days) }
                )
            }
        }

        When("key is ScreenplayType") {
            val key = ScreenplayType.Movie

            Then("forbidden key exception is thrown") {
                shouldThrowWithMessage<IllegalArgumentException>(
                    message = "Key cannot be of type ScreenplayType, to avoid collisions with other entities",
                    block = { scenario.sut.get(key, 1.days) }
                )
            }
        }

        When("key is ScreenplayTypeFilter") {
            val key = ScreenplayTypeFilter.All

            Then("forbidden key exception is thrown") {
                shouldThrowWithMessage<IllegalArgumentException>(
                    message = "Key cannot be of type ScreenplayTypeFilter, to avoid collisions with other entities",
                    block = { scenario.sut.get(key, 1.days) }
                )
            }
        }

        When("key is Unit") {
            val key = Unit

            Then("forbidden key exception is thrown") {
                shouldThrowWithMessage<IllegalArgumentException>(
                    message = "Key cannot be of type Unit, to avoid collisions with other entities",
                    block = { scenario.sut.get(key, 1.days) }
                )
            }
        }
    }

    Given("setting fetch data") {
        val scenario = TestScenario(currentTime)

        When("key is TmdbScreenplayId") {
            val key = TmdbScreenplayIdSample.Dexter

            Then("forbidden key exception is thrown") {
                shouldThrowWithMessage<IllegalArgumentException>(
                    message = "Key cannot be of type TmdbTvShowId, to avoid collisions with other entities",
                    block = { scenario.sut.set(key, Page(1)) }
                )
            }
        }

        When("key is TraktScreenplayId") {
            val key = TraktScreenplayIdSample.Dexter

            Then("forbidden key exception is thrown") {
                shouldThrowWithMessage<IllegalArgumentException>(
                    message = "Key cannot be of type TraktTvShowId, to avoid collisions with other entities",
                    block = { scenario.sut.set(key, Page(1)) }
                )
            }
        }

        When("key is ScreenplayIds") {
            val key = ScreenplayIdsSample.Dexter

            Then("forbidden key exception is thrown") {
                shouldThrowWithMessage<IllegalArgumentException>(
                    message = "Key cannot be of type TvShowIds, to avoid collisions with other entities",
                    block = { scenario.sut.set(key, Page(1)) }
                )
            }
        }

        When("key is ScreenplayType") {
            val key = ScreenplayType.Movie

            Then("forbidden key exception is thrown") {
                shouldThrowWithMessage<IllegalArgumentException>(
                    message = "Key cannot be of type ScreenplayType, to avoid collisions with other entities",
                    block = { scenario.sut.set(key, Page(1)) }
                )
            }
        }

        When("key is ScreenplayTypeFilter") {
            val key = ScreenplayTypeFilter.All

            Then("forbidden key exception is thrown") {
                shouldThrowWithMessage<IllegalArgumentException>(
                    message = "Key cannot be of type ScreenplayTypeFilter, to avoid collisions with other entities",
                    block = { scenario.sut.set(key, Page(1)) }
                )
            }
        }

        When("key is Unit") {
            val key = Unit

            Then("forbidden key exception is thrown") {
                shouldThrowWithMessage<IllegalArgumentException>(
                    message = "Key cannot be of type Unit, to avoid collisions with other entities",
                    block = { scenario.sut.set(key, Page(1)) }
                )
            }
        }
    }

    Given("fetch data stored") {
        val aWeekAgo = currentTime - 7.days.toTimeSpan()
        val fetchDataMap = mapOf(
            "key" to FetchData(aWeekAgo, Page(1))
        )

        And("not expired") {
            val expiration = 8.days

            When("when getting fetch data") {
                val scenario = TestScenario(currentTime, fetchDataMap)
                val result = scenario.sut.get("key", expiration)

                Then("fetch data is returned") {
                    result shouldBe FetchData(aWeekAgo, Page(1))
                }
            }
        }

        And("expired") {
            val expiration = 6.days

            When("when getting fetch data") {
                val scenario = TestScenario(currentTime, fetchDataMap)
                val result = scenario.sut.get("key", expiration)

                Then("null is returned") {
                    result shouldBe null
                }
            }
        }
    }

    Given("no fetch data stored") {

        When("when getting fetch data") {
            val scenario = TestScenario(currentTime)
            val result = scenario.sut.get("key", 1.days)

            Then("null is returned") {
                result shouldBe null
            }
        }

        When("setting fetch data") {
            val scenario = TestScenario(currentTime)
            scenario.sut.set("key", Page(1))

            Then("fetch data is stored") {
                scenario.sut.get("key", 1.days) shouldBe FetchData(currentTime, Page(1))
            }
        }
    }
})

private class RealFetchDataRepositoryTestScenario(
    val sut: RealFetchDataRepository
)

private fun TestScenario(
    currentTime: DateTime,
    fetchDataMap: Map<out Any, FetchData> = emptyMap()
): RealFetchDataRepositoryTestScenario {
    return RealFetchDataRepositoryTestScenario(
        sut = RealFetchDataRepository(
            dataSource = FakeFetchDataDataSource(fetchDataMap),
            getCurrentDateTime = FakeGetCurrentDateTime(currentTime)
        )
    )
}
