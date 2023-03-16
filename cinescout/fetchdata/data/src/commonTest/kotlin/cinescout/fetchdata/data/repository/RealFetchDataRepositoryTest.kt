package cinescout.fetchdata.data.repository

import cinescout.FakeGetCurrentDateTime
import cinescout.fetchdata.data.datasource.FakeFetchDataDataSource
import cinescout.fetchdata.domain.model.FetchData
import cinescout.sample.DateTimeSample
import cinescout.utils.kotlin.toTimeSpan
import com.soywiz.klock.DateTime
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import kotlin.time.Duration.Companion.days

class RealFetchDataRepositoryTest : BehaviorSpec({

    val currentTime = DateTimeSample.Xmas2023

    Given("fetch data stored") {
        val aWeekAgo = currentTime - 7.days.toTimeSpan()
        val fetchDataMap = mapOf(
            "key" to FetchData(aWeekAgo, 1)
        )

        And("not expired") {
            val expiration = 8.days

            When("when getting fetch data") {
                val scenario = TestScenario(currentTime, fetchDataMap)
                val result = scenario.sut.get("key", expiration)

                Then("fetch data is returned") {
                    result shouldBe FetchData(aWeekAgo, 1)
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
            scenario.sut.set("key", FetchData(currentTime, 1))

            Then("fetch data is stored") {
                scenario.sut.get("key", 1.days) shouldBe FetchData(currentTime, 1)
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
