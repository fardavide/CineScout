package cinescout.suggestions.presentation.util

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

class FixedSizeStackTest : BehaviorSpec({

    Given("empty stack") {
        val stack = FixedSizeStack.empty<Int>(size = 3)

        When("isEmpty is called") {

            Then("result should be true") {
                stack.isEmpty() shouldBe true
            }
        }

        When("isFull is called") {

            Then("result should be false") {
                stack.isFull() shouldBe false
            }
        }

        When("all is called") {

            Then("result should be empty") {
                stack.all() shouldBe emptySet()
            }
        }

        When("pop is called") {
            val (newStack, element) = stack.pop()

            Then("element should be null") {
                element shouldBe null
            }

            Then("new stack should be empty") {
                newStack.isEmpty() shouldBe true
            }
        }
    }

    Given("full stack") {
        val stack = FixedSizeStack.fromCollection(size = 3, listOf(1, 2, 3))

        When("isEmpty is called") {

            Then("result should be false") {
                stack.isEmpty() shouldBe false
            }
        }

        When("isFull is called") {

            Then("result should be true") {
                stack.isFull() shouldBe true
            }
        }

        When("all is called") {

            Then("result should be [1, 2, 3]") {
                stack.all() shouldBe setOf(1, 2, 3)
            }
        }

        When("pop is called") {
            val (newStack, element) = stack.pop()

            Then("element should be 1") {
                element shouldBe 1
            }

            Then("new stack should be [2, 3]") {
                newStack.all() shouldBe setOf(2, 3)
            }
        }

        When("join") {

            And("a single element") {
                val newStack = stack.join(4)

                Then("new stack should be [1, 4, 2]") {
                    newStack.all() shouldBe setOf(1, 4, 2)
                }
            }

            And("another empty collection") {
                val anotherCollection = emptyList<Int>()
                val newStack = stack.join(anotherCollection)

                Then("new stack should be [1, 2, 3]") {
                    newStack.all() shouldBe setOf(1, 2, 3)
                }
            }

            And("another non empty collection") {
                val anotherCollection = listOf(4, 5, 6)
                val newStack = stack.join(anotherCollection)

                Then("new stack should be [1, 4, 5]") {
                    newStack.all() shouldBe setOf(1, 4, 5)
                }
            }
        }
    }

    Given("non empty non full stack") {
        val stack = FixedSizeStack.fromCollection(size = 3, listOf(1, 2))

        When("isFull is called") {

            Then("result should be false") {
                stack.isFull() shouldBe false
            }
        }

        When("all is called") {

            Then("result should be [1, 2]") {
                stack.all() shouldBe setOf(1, 2)
            }
        }

        When("pop is called") {
            val (newStack, element) = stack.pop()

            Then("element should be 1") {
                element shouldBe 1
            }

            Then("new stack should be [2]") {
                newStack.all() shouldBe setOf(2)
            }
        }
    }

    Given("a stack of TestElement") {
        data class TestElement(val id: Int, val name: String)
        val collection = listOf(
            TestElement(id = 1, name = "one"),
            TestElement(id = 2, name = "two"),
            TestElement(id = 3, name = "three")
        )
        val stack = FixedSizeStack.fromCollection(size = 3, collection)

        When("join by") {

            And("a single element") {
                val element = TestElement(id = 2, name = "updated")
                val newStack = stack.joinBy(element) { it.id }

                Then("""new stack should be [{1, "one"}, {2, "updated"}, {3, "three"}]""") {
                    newStack.all() shouldBe setOf(
                        TestElement(id = 1, name = "one"),
                        TestElement(id = 2, name = "updated"),
                        TestElement(id = 3, name = "three")
                    )
                }
            }
        }
    }

    Given("a list with duplicates") {
        val list = listOf(1, 2, 3, 3, 4)

        When("fromCollection is called") {
            val stack = FixedSizeStack.fromCollection(size = 5, list)

            Then("result should be [1, 2, 3, 4]") {
                stack.all() shouldBe setOf(1, 2, 3, 4)
            }
        }
    }
})
