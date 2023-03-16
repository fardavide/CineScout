package cinescout.database

import cinescout.database.model.DatabaseFetchKey
import cinescout.database.sample.DatabaseDateTimeSample
import cinescout.test.database.TestDatabaseExtension
import cinescout.test.database.requireTestDatabaseExtension
import io.kotest.core.spec.Spec
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

class FetchDataQueriesTest : BehaviorSpec({
    extension(TestDatabaseExtension())

    Given("a key without page") {
        val key = DatabaseFetchKey.WithoutPage("key")

        When("inserting the key") {
            val scenario = TestScenario()
            scenario.sut.insert(key, DatabaseDateTimeSample.Xmas2023)

            Then("its value can be found") {
                val result = scenario.sut.find(key).executeAsOneOrNull()
                result shouldBe DatabaseDateTimeSample.Xmas2023
            }
        }

        And("another equal key") {
            val anotherKey = DatabaseFetchKey.WithoutPage("key")
            val scenario = TestScenario()

            When("inserting the keys") {
                scenario.sut.insert(key, DatabaseDateTimeSample.Xmas2023)
                scenario.sut.insert(anotherKey, DatabaseDateTimeSample.Xmas2024)

                Then("the value is updated") {
                    val result = scenario.sut.find(key).executeAsOneOrNull()
                    result shouldBe DatabaseDateTimeSample.Xmas2024
                }
            }
        }

        And("another key with page and same value") {
            val anotherKey = DatabaseFetchKey.WithPage("key", 1)
            val scenario = TestScenario()

            When("inserting the keys") {
                scenario.sut.insert(key, DatabaseDateTimeSample.Xmas2023)
                scenario.sut.insert(anotherKey, DatabaseDateTimeSample.Xmas2024)

                Then("the value is not updated") {
                    val result = scenario.sut.find(key).executeAsOneOrNull()
                    result shouldBe DatabaseDateTimeSample.Xmas2023
                }
            }
        }
    }

    Given("a key with page") {
        val key = DatabaseFetchKey.WithPage("key", 1)

        When("inserting the key") {
            val scenario = TestScenario()
            scenario.sut.insert(key, DatabaseDateTimeSample.Xmas2023)

            Then("its value can be found") {
                val result = scenario.sut.find(key).executeAsOneOrNull()
                result shouldBe DatabaseDateTimeSample.Xmas2023
            }
        }

        And("another equal key") {
            val anotherKey = DatabaseFetchKey.WithPage("key", 1)
            val scenario = TestScenario()

            When("inserting the keys") {
                scenario.sut.insert(key, DatabaseDateTimeSample.Xmas2023)
                scenario.sut.insert(anotherKey, DatabaseDateTimeSample.Xmas2024)

                Then("the value is updated") {
                    val result = scenario.sut.find(key).executeAsOneOrNull()
                    result shouldBe DatabaseDateTimeSample.Xmas2024
                }
            }
        }

        And("another key with same value but different page") {
            val anotherKey = DatabaseFetchKey.WithPage("key", 2)
            val scenario = TestScenario()

            When("inserting the keys") {
                scenario.sut.insert(key, DatabaseDateTimeSample.Xmas2023)
                scenario.sut.insert(anotherKey, DatabaseDateTimeSample.Xmas2024)

                Then("the value is not updated") {
                    val result = scenario.sut.find(key).executeAsOneOrNull()
                    result shouldBe DatabaseDateTimeSample.Xmas2023
                }

                And("both of the values can be found") {
                    scenario.sut.find(key).executeAsOneOrNull() shouldBe DatabaseDateTimeSample.Xmas2023
                    scenario.sut.find(anotherKey).executeAsOneOrNull() shouldBe DatabaseDateTimeSample.Xmas2024
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
