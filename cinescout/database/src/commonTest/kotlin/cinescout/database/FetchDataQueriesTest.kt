package cinescout.database

import cinescout.database.model.DatabaseFetchData
import cinescout.database.model.DatabasePage
import cinescout.sample.DateTimeSample
import cinescout.test.database.TestDatabaseExtension
import cinescout.test.database.requireTestDatabaseExtension
import io.kotest.core.spec.Spec
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

class FetchDataQueriesTest : BehaviorSpec({
    extension(TestDatabaseExtension())

    Given("a key with page") {
        val key = "key"
        val page = DatabasePage(1)

        When("inserting the key") {
            val scenario = TestScenario()
            scenario.sut.insert(key, page, DateTimeSample.Xmas2023)

            Then("its page and dateTime can be found") {
                val result = scenario.sut.find(key).executeAsOneOrNull()
                result shouldBe DatabaseFetchData(page, DateTimeSample.Xmas2023)
            }
        }

        And("another equal key and page") {
            val anotherKey = "key"
            val anotherPage = DatabasePage(1)
            val scenario = TestScenario()

            When("inserting the keys") {
                scenario.sut.insert(key, page, DateTimeSample.Xmas2023)
                scenario.sut.insert(anotherKey, anotherPage, DateTimeSample.Xmas2024)

                Then("the dateTime is updated") {
                    val result = scenario.sut.find(key).executeAsOneOrNull()
                    result shouldBe DatabaseFetchData(anotherPage, DateTimeSample.Xmas2024)
                }
            }
        }

        And("another equal key and different page") {
            val anotherKey = "key"
            val anotherPage = DatabasePage(2)
            val scenario = TestScenario()

            When("inserting the keys") {
                scenario.sut.insert(key, page, DateTimeSample.Xmas2023)
                scenario.sut.insert(anotherKey, anotherPage, DateTimeSample.Xmas2024)

                Then("the page and dateTime are updated") {
                    val result = scenario.sut.find(key).executeAsOneOrNull()
                    result shouldBe DatabaseFetchData(anotherPage, DateTimeSample.Xmas2024)
                }
            }
        }

        And("a different key and same page") {
            val anotherKey = "anotherKey"
            val anotherPage = DatabasePage(1)
            val scenario = TestScenario()

            When("inserting the keys") {
                scenario.sut.insert(key, page, DateTimeSample.Xmas2023)
                scenario.sut.insert(anotherKey, anotherPage, DateTimeSample.Xmas2024)

                Then("both pages and dateTime can be found") {
                    val result = scenario.sut.find(key).executeAsOneOrNull()
                    result shouldBe DatabaseFetchData(anotherPage, DateTimeSample.Xmas2023)

                    val anotherResult = scenario.sut.find(anotherKey).executeAsOneOrNull()
                    anotherResult shouldBe DatabaseFetchData(anotherPage, DateTimeSample.Xmas2024)
                }
            }
        }
    }
})

private class FetchDataQueriesTestScenario(
    val sut: FetchDataQueries
)

private fun Spec.TestScenario(): FetchDataQueriesTestScenario {
    val extension = requireTestDatabaseExtension()
    extension.clear()

    return FetchDataQueriesTestScenario(
        sut = extension.database.fetchDataQueries
    )
}
