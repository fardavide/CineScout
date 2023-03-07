package store

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

class StoreKeyTest : BehaviorSpec({

    Given("store key with id only") {
        val key = StoreKey("1")

        When("another store key with same id") {
            val otherKey = StoreKey("1")

            Then("keys are equal") {
                key shouldBe otherKey
            }
        }

        When("another store key with different id") {
            val otherKey = StoreKey("2")

            Then("keys are not equal") {
                key shouldNotBe otherKey
            }
        }
    }

    Given("store key from id and int item id") {
        val key = StoreKey("id", 1)

        When("another store key with the same id and item id") {
            val otherKey = StoreKey("id", 1)

            Then("keys are equal") {
                key shouldBe otherKey
            }
        }

        When("another store key with same id and equal string item id") {
            val otherKey = StoreKey("id", "1")

            Then("keys are not equal") {
                key shouldNotBe otherKey
            }
        }

        When("another store key with the same id and different item id") {
            val otherKey = StoreKey("id", 2)

            Then("keys are not equal") {
                key shouldNotBe otherKey
            }
        }

        When("another store key with different id and same item id") {
            val otherKey = StoreKey("otherId", 1)

            Then("keys are not equal") {
                key shouldNotBe otherKey
            }
        }
    }

    Given("store key from id and string item id") {
        val key = StoreKey("id", "1")

        When("another store key with the same id and item id") {
            val otherKey = StoreKey("id", "1")

            Then("keys are equal") {
                key shouldBe otherKey
            }
        }

        When("another store key with the same id and different item id") {
            val otherKey = StoreKey("id", "2")

            Then("keys are not equal") {
                key shouldNotBe otherKey
            }
        }

        When("another store key with different id and same item id") {
            val otherKey = StoreKey("otherId", "1")

            Then("keys are not equal") {
                key shouldNotBe otherKey
            }
        }
    }

    Given("store key from type and int item id") {
        class Type
        val key = StoreKey<Type>(1)

        When("another store key with the same type and item id") {
            val otherKey = StoreKey<Type>(1)

            Then("keys are equal") {
                key shouldBe otherKey
            }
        }

        When("another store key with the same type and different item id") {
            val otherKey = StoreKey<Type>(2)

            Then("keys are not equal") {
                key shouldNotBe otherKey
            }
        }

        When("another store key with different type and same item id") {
            class OtherType
            val otherKey = StoreKey<OtherType>(1)

            Then("keys are not equal") {
                key shouldNotBe otherKey
            }
        }
    }
})
