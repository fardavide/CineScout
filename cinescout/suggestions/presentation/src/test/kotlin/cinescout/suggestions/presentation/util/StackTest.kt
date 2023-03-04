package cinescout.suggestions.presentation.util

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

class StackTest : BehaviorSpec({

    Given("empty stack") {
        val stack = Stack.empty<Int>()

        When("join") {

            And("a single element") {
                val newStack = stack.join(1)

                Then("new stack should be [1]") {
                    newStack shouldBe Stack.fromCollection(listOf(1))
                }
            }

            And("another empty collection") {
                val anotherCollection = emptyList<Int>()
                val newStack = stack.join(anotherCollection)

                Then("new stack should be empty") {
                    newStack shouldBe Stack.empty()
                }
            }

            And("another non empty collection") {
                val anotherCollection = listOf(4, 5, 6)
                val newStack = stack.join(anotherCollection)

                Then("new stack should be [4, 5, 6]") {
                    newStack shouldBe Stack.fromCollection(listOf(4, 5, 6))
                }
            }
        }

        When("equals") {

            And("another empty stack") {
                val anotherStack = Stack.empty<Int>()

                Then("result should be true") {
                    stack shouldBe anotherStack
                }
            }

            And("another non empty stack") {
                val anotherStack = Stack.fromCollection(listOf(1, 2, 3))

                Then("result should be false") {
                    stack shouldNotBe anotherStack
                }
            }
        }

        When("isEmpty is called") {

            Then("result should be true") {
                stack.isEmpty() shouldBe true
            }
        }

        When("toString") {
            val result = stack.toString()

            Then("result should be '[]'") {
                result shouldBe "[]"
            }
        }
    }

    Given("non empty stack") {
        val stack = Stack.fromCollection(listOf(1, 2, 3))

        When("pop") {
            val (newStack, element) = stack.pop()

            Then("element should be 1") {
                element shouldBe 1
            }

            Then("new stack should be [2, 3]") {
                newStack shouldBe Stack.fromCollection(listOf(2, 3))
            }
        }

        When("join") {

            And("a single element") {
                val newStack = stack.join(4)

                Then("new stack should be [1, 4, 2, 3]") {
                    newStack shouldBe Stack.fromCollection(listOf(1, 4, 2, 3))
                }
            }

            And("another empty collection") {
                val anotherCollection = emptyList<Int>()
                val newStack = stack.join(anotherCollection)

                Then("new stack should be [1, 2, 3]") {
                    newStack shouldBe Stack.fromCollection(listOf(1, 2, 3))
                }
            }

            And("another non empty collection") {
                val anotherCollection = listOf(4, 5, 6)
                val newStack = stack.join(anotherCollection)

                Then("new stack should be [1, 4, 5, 6, 2, 3]") {
                    newStack shouldBe Stack.fromCollection(listOf(1, 4, 5, 6, 2, 3))
                }
            }
        }

        When("equals") {
            And("another non empty stack with same collection") {
                val anotherStack = Stack.fromCollection(listOf(1, 2, 3))

                Then("result should be true") {
                    stack shouldBe anotherStack
                }
            }

            And("another non empty stack with same content but different collection") {
                val anotherStack = Stack.fromCollection(setOf(1, 2, 3))

                Then("result should be true") {
                    stack shouldBe anotherStack
                }
            }

            And("another non empty stack with different content") {
                val anotherStack = Stack.fromCollection(listOf(1, 2, 4))

                Then("result should be false") {
                    stack shouldNotBe anotherStack
                }
            }

            And("another non empty stack with same elements but different order") {
                val anotherStack = Stack.fromCollection(listOf(3, 2, 1))

                Then("result should be false") {
                    stack shouldNotBe anotherStack
                }
            }
        }

        When("isEmpty is called") {

            Then("result should be false") {
                stack.isEmpty() shouldBe false
            }
        }

        When("toString") {
            val result = stack.toString()

            Then("result should be '[1, 2, 3]'") {
                result shouldBe "[1, 2, 3]"
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
        val stack = Stack.fromCollection(collection)

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

    Given("a list") {
        val list = listOf(1, 2, 3)

        When("fromCollection is called") {
            val stack = Stack.fromCollection(list)

            Then("stack should contain all elements from the list") {
                stack.all() shouldBe setOf(1, 2, 3)
            }
        }
    }

    Given("a list with duplicates") {
        val list = listOf(1, 2, 3, 3, 4)

        When("fromCollection is called") {
            val stack = Stack.fromCollection(list)

            Then("stack should contain all elements from the list without duplications") {
                stack.all() shouldBe setOf(1, 2, 3, 4)
            }
        }
    }
})
