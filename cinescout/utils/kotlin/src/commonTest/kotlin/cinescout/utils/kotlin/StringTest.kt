package cinescout.utils.kotlin

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

class StringTest : BehaviorSpec({

    Given("a top level object") {
        val testObject = TestObject

        When("getting the short name") {
            val shortName = testObject::class.shortName

            Then("the short name is correct") {
                shortName shouldBe "TestObject"
            }
        }
    }

    Given("a nested class") {
        val testObject = TestObject.TestClass()

        When("getting the short name") {
            val shortName = testObject::class.shortName

            Then("the short name is correct") {
                shortName shouldBe "TestObject.TestClass"
            }
        }
    }
})

private object TestObject {

    class TestClass
}
