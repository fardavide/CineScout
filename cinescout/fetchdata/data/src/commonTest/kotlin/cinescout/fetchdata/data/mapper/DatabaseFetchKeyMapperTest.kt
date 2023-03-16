package cinescout.fetchdata.data.mapper

import cinescout.database.model.DatabaseFetchKey
import cinescout.fetchdata.domain.model.FetchKey
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

class DatabaseFetchKeyMapperTest : BehaviorSpec({

    data class TestClass(val value: Int)

    Given("a fetch key") {
        val key = FetchKey.WithoutPage(TestClass(1))

        When("converting to database fetch key") {
            val databaseFetchKey = DatabaseFetchKeyMapper().toDatabaseFetchKey(key)

            Then("the database fetch key should be correct") {
                databaseFetchKey shouldBe DatabaseFetchKey.WithoutPage("TestClass(value=1)")
            }
        }
    }

    Given("a fetch key with page") {
        val key = FetchKey.WithPage(TestClass(1), 2)

        When("converting to database fetch key") {
            val databaseFetchKey = DatabaseFetchKeyMapper().toDatabaseFetchKey(key)

            Then("the database fetch key should be correct") {
                databaseFetchKey shouldBe DatabaseFetchKey.WithPage("TestClass(value=1)", 2)
            }
        }
    }
})
