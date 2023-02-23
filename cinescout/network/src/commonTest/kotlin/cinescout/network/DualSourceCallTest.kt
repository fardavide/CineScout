package cinescout.network

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import cinescout.error.NetworkError
import cinescout.model.NetworkOperation
import cinescout.utils.kotlin.mapToUnit
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import store.PagedData
import store.Paging
import store.builder.pagedDataOf

class DualSourceCallTest : BehaviorSpec({

    val networkError = NetworkError.Unauthorized
    val operationError = NetworkOperation.Error(networkError)

    Given("dual source call") {

        When("both sources are error") {
            val scenario = TestScenario(
                firstCallResult = operationError.left(),
                secondCallResult = operationError.left()
            )
            Then("error is returned") {
                scenario.call() shouldBe networkError.left()
            }
            Then("error is returned")
        }

        When("both sources are success") {
            val scenario = TestScenario(
                firstCallResult = Unit.right(),
                secondCallResult = Unit.right()
            )
            Then("success is returned") {
                scenario.call() shouldBe Unit.right()
            }
        }

        When("both sources are skipped") {
            val scenario = TestScenario(
                firstCallResult = NetworkOperation.Skipped.left(),
                secondCallResult = NetworkOperation.Skipped.left()
            )
            Then("success is returned") {
                scenario.call() shouldBe Unit.right()
            }
        }

        When("first source is success") {

            And("second source is error") {
                val scenario = TestScenario(
                    firstCallResult = Unit.right(),
                    secondCallResult = operationError.left()
                )
                Then("error is returned") {
                    scenario.call() shouldBe networkError.left()
                }
            }

            And("second source is skipped") {
                val scenario = TestScenario(
                    firstCallResult = Unit.right(),
                    secondCallResult = NetworkOperation.Skipped.left()
                )
                Then("success is returned") {
                    scenario.call() shouldBe Unit.right()
                }
            }
        }

        When("first source is error") {

            And("second source is success") {
                val scenario = TestScenario(
                    firstCallResult = operationError.left(),
                    secondCallResult = Unit.right()
                )
                Then("error is returned") {
                    scenario.call() shouldBe networkError.left()
                }
            }

            And("second source is skipped") {
                val scenario = TestScenario(
                    firstCallResult = operationError.left(),
                    secondCallResult = NetworkOperation.Skipped.left()
                )
                Then("error is returned") {
                    scenario.call() shouldBe networkError.left()
                }
            }
        }
    }

    Given("dual source call with result") {
        val firstSuccess = 1.right()
        val secondSuccess = 2.right()

        When("both sources are error") {
            val scenario = TestScenario(
                firstCallResult = operationError.left(),
                secondCallResult = operationError.left()
            )
            Then("error is returned") {
                scenario.callWithResult() shouldBe operationError.left()
            }
        }

        When("both sources are success") {
            val scenario = TestScenario(
                firstCallResult = firstSuccess,
                secondCallResult = secondSuccess
            )
            Then("success is returned") {
                scenario.callWithResult() shouldBe firstSuccess
            }
        }

        When("both sources are skipped") {
            val scenario = TestScenario(
                firstCallResult = NetworkOperation.Skipped.left(),
                secondCallResult = NetworkOperation.Skipped.left()
            )
            Then("success is returned") {
                scenario.callWithResult() shouldBe NetworkOperation.Skipped.left()
            }
        }

        When("first source is success") {

            And("second source is error") {
                val scenario = TestScenario(
                    firstCallResult = firstSuccess,
                    secondCallResult = operationError.left()
                )
                Then("error is returned") {
                    scenario.callWithResult() shouldBe operationError.left()
                }
            }

            And("second source is skipped") {
                val scenario = TestScenario(
                    firstCallResult = firstSuccess,
                    secondCallResult = NetworkOperation.Skipped.left()
                )
                Then("success is returned") {
                    scenario.callWithResult() shouldBe firstSuccess
                }
            }
        }

        When("first source is error") {

            And("second source is success") {
                val scenario = TestScenario(
                    firstCallResult = operationError.left(),
                    secondCallResult = secondSuccess
                )
                Then("error is returned") {
                    scenario.callWithResult() shouldBe operationError.left()
                }
            }

            And("second source is skipped") {
                val scenario = TestScenario(
                    firstCallResult = operationError.left(),
                    secondCallResult = NetworkOperation.Skipped.left()
                )
                Then("error is returned") {
                    scenario.callWithResult() shouldBe operationError.left()
                }
            }
        }
    }

    Given("paged dual source call with result") {
        val page = Paging.Page.Initial
        val firstSuccess = pagedDataOf(1, 2).right()
        val secondSuccess = pagedDataOf(3, 4).right()

        When("both sources are error") {
            val scenario = TestScenario<Unit>(
                page = page,
                firstCallResult = operationError.left(),
                secondCallResult = operationError.left()
            )
            Then("error is returned") {
                scenario.callWithResult() shouldBe operationError.left()
            }
        }

        When("both sources are success") {
            val scenario = TestScenario(
                page = page,
                firstCallResult = firstSuccess,
                secondCallResult = secondSuccess
            )
            Then("success is returned") {
                scenario.callWithResult() shouldBe pagedDataOf(1, 2, paging = page).right()
            }
        }

        When("both sources are skipped") {
            val scenario = TestScenario<Unit>(
                page = page,
                firstCallResult = NetworkOperation.Skipped.left(),
                secondCallResult = NetworkOperation.Skipped.left()
            )
            Then("success is returned") {
                scenario.callWithResult() shouldBe NetworkOperation.Skipped.left()
            }
        }

        When("first source is success") {

            And("second source is error") {
                val scenario = TestScenario(
                    page = page,
                    firstCallResult = firstSuccess,
                    secondCallResult = operationError.left()
                )
                Then("error is returned") {
                    scenario.callWithResult() shouldBe operationError.left()
                }
            }

            And("second source is skipped") {
                val scenario = TestScenario(
                    page = page,
                    firstCallResult = firstSuccess,
                    secondCallResult = NetworkOperation.Skipped.left()
                )
                Then("success is returned") {
                    scenario.callWithResult() shouldBe pagedDataOf(1, 2, paging = page).right()
                }
            }
        }

        When("first source is error") {

            And("second source is success") {
                val scenario = TestScenario(
                    page = page,
                    firstCallResult = operationError.left(),
                    secondCallResult = secondSuccess
                )
                Then("error is returned") {
                    scenario.callWithResult() shouldBe operationError.left()
                }
            }

            And("second source is skipped") {
                val scenario = TestScenario<Unit>(
                    page = page,
                    firstCallResult = operationError.left(),
                    secondCallResult = NetworkOperation.Skipped.left()
                )
                Then("error is returned") {
                    scenario.callWithResult() shouldBe operationError.left()
                }
            }
        }
    }
})

private class DualSourceCallTestScenario<T>(
    private val firstSourceCall: suspend () -> Either<NetworkOperation, T>,
    private val secondSourceCall: suspend () -> Either<NetworkOperation, T>
) {

    suspend fun call(): Either<NetworkError, Unit> = dualSourceCall(
        firstSourceCall = { firstSourceCall().mapToUnit() },
        secondSourceCall = { secondSourceCall().mapToUnit() }
    )

    suspend fun callWithResult(): Either<NetworkOperation, T> {
        val result = dualSourceCallWithResult(
            firstSourceCall = firstSourceCall,
            secondSourceCall = secondSourceCall
        )
        result.onRight { right ->
            check(right !is Unit) {
                "Checking result of Unit is not supported as it could be error prone"
            }
        }
        return result
    }
}

private class PagedDualSourceCallTestScenario<T : Any>(
    private val page: Paging.Page,
    private val firstSourceCall: suspend (
        page: Paging.Page
    ) -> Either<NetworkOperation, PagedData.Remote<T>>,
    private val secondSourceCall: suspend (
        page: Paging.Page
    ) -> Either<NetworkOperation, PagedData.Remote<T>>
) {

    suspend fun callWithResult(): Either<NetworkOperation, PagedData.Remote<T>> = dualSourceCallWithResult(
        page = page,
        firstSourceCall = firstSourceCall,
        secondSourceCall = secondSourceCall
    )
}

private fun <T> TestScenario(
    firstCallResult: Either<NetworkOperation, T>,
    secondCallResult: Either<NetworkOperation, T>
) = DualSourceCallTestScenario({ firstCallResult }, { secondCallResult })

private fun <T : Any> TestScenario(
    page: Paging.Page,
    firstCallResult: Either<NetworkOperation, PagedData.Remote<T>>,
    secondCallResult: Either<NetworkOperation, PagedData.Remote<T>>
) = PagedDualSourceCallTestScenario(page, { firstCallResult }, { secondCallResult })
