package cinescout.fetchdata.data.mapper

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldStartWith
import io.kotest.matchers.throwable.shouldHaveMessage

class FetchDataKeyMapperTest : BehaviorSpec({

    val mapper = FetchDataKeyMapper()

    Given("a string key") {
        val key = "some key"

        When("converting to database key") {
            val databaseKey = mapper.toDatabaseKey(key)

            Then("it should be the same") {
                databaseKey shouldBe "some key"
            }
        }
    }

    Given("a data class key") {
        val key = DataClassKey("key")

        When("converting to database key") {
            val databaseKey = mapper.toDatabaseKey(key)

            Then("it should contain the full qualified name") {
                databaseKey shouldBe "cinescout.fetchdata.data.mapper.DataClassKey(key=key)"
            }
        }
    }

    Given("a data class nested into another class") {
        val key = Parent.NestedDataClassKey("key")

        When("converting to database key") {
            val databaseKey = mapper.toDatabaseKey(key)

            Then("it should contain the full qualified name") {
                databaseKey shouldBe "cinescout.fetchdata.data.mapper.Parent.NestedDataClassKey(key=key)"
            }
        }
    }

    Given("a non data class key") {
        val key = NonDataClassKey("key")

        When("converting to database key") {

            Then("it should throw an exception") {
                val exception = shouldThrow<IllegalArgumentException> { mapper.toDatabaseKey(key) }
                exception.message shouldStartWith
                    "String value for the key is not atomic, you may want to use a data class. " +
                    "Key: cinescout.fetchdata.data.mapper.NonDataClassKey@"
            }
        }
    }

    Given("a local data class key") {
        data class LocalDataClassKey(val key: String)
        val key = LocalDataClassKey("key")

        When("converting to database key") {

            Then("it should throw an exception") {
                val exception = shouldThrow<IllegalArgumentException> { mapper.toDatabaseKey(key) }
                exception shouldHaveMessage
                    "Cannot resolve class name for LocalDataClassKey(key=key), perhaps you're using a local class"
            }
        }
    }
})

private data class DataClassKey(val key: String)

class NonDataClassKey(@Suppress("unused") val key: String)

private class Parent {

    data class NestedDataClassKey(val key: String)
}

