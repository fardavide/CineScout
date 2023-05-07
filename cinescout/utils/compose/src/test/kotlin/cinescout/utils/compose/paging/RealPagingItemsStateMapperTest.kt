package cinescout.utils.compose.paging

import androidx.paging.compose.LazyPagingItems
import cinescout.error.NetworkError
import cinescout.resources.TextRes
import cinescout.resources.sample.MessageSample
import cinescout.store5.FetchException
import cinescout.test.android.CombinedLoadStates
import cinescout.test.android.getValue
import cinescout.utils.compose.Effect
import cinescout.utils.compose.FakeNetworkErrorToMessageMapper
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk

private val errorMessage = MessageSample.NoNetworkError
private val equalsLazyPagingItems: LazyPagingItems<Item> = mockk {
    every { this@mockk == any() } answers { firstArg<Any>() is LazyPagingItems<*> }
}

class RealPagingItemsStateMapperTest : BehaviorSpec({

    Given("refresh is loading") {
        val combinedLoadStates by CombinedLoadStates.refresh.loading()

        When("is empty") {
            val isEmpty = true

            val scenario = TestScenario(
                errorMessage = errorMessage,
                lazyPagingItemsSurrogate = LazyPagingItemsSurrogate(
                    isEmpty = isEmpty,
                    loadStates = combinedLoadStates
                )
            )
            val state = scenario.toState()

            Then("state should be loading") {
                state shouldBe PagingItemsState.Loading
            }
        }

        When("is not empty") {
            val isEmpty = false

            val scenario = TestScenario(
                errorMessage = errorMessage,
                lazyPagingItemsSurrogate = LazyPagingItemsSurrogate(
                    isEmpty = isEmpty,
                    loadStates = combinedLoadStates
                )
            )
            val state = scenario.toState()

            Then("state should be not empty") {
                state shouldBe PagingItemsState.NotEmpty(
                    items = equalsLazyPagingItems,
                    error = Effect.empty(),
                    isAlsoLoading = true
                )
            }
        }
    }

    Given("refresh is not loading") {
        val combinedLoadStates by CombinedLoadStates.refresh.incomplete()

        When("is empty") {
            val isEmpty = true

            val scenario = TestScenario(
                errorMessage = errorMessage,
                lazyPagingItemsSurrogate = LazyPagingItemsSurrogate(
                    isEmpty = isEmpty,
                    loadStates = combinedLoadStates
                )
            )
            val state = scenario.toState()

            Then("state should be empty") {
                state shouldBe PagingItemsState.Empty
            }
        }

        When("is not empty") {
            val isEmpty = false

            val scenario = TestScenario(
                errorMessage = errorMessage,
                lazyPagingItemsSurrogate = LazyPagingItemsSurrogate(
                    isEmpty = isEmpty,
                    loadStates = combinedLoadStates
                )
            )
            val state = scenario.toState()

            Then("state should be not empty") {
                state shouldBe PagingItemsState.NotEmpty(
                    items = equalsLazyPagingItems,
                    error = Effect.empty(),
                    isAlsoLoading = false
                )
            }
        }
    }

    Given("refresh is error") {
        val combinedLoadStates by CombinedLoadStates.refresh.error(FetchException(NetworkError.NoNetwork))

        When("is empty") {
            val isEmpty = true

            val scenario = TestScenario(
                errorMessage = errorMessage,
                lazyPagingItemsSurrogate = LazyPagingItemsSurrogate(
                    isEmpty = isEmpty,
                    loadStates = combinedLoadStates
                )
            )
            val state = scenario.toState()

            Then("state should be error") {
                state shouldBe PagingItemsState.Error(message = errorMessage)
            }
        }

        When("is not empty") {
            val isEmpty = false

            val scenario = TestScenario(
                errorMessage = errorMessage,
                lazyPagingItemsSurrogate = LazyPagingItemsSurrogate(
                    isEmpty = isEmpty,
                    loadStates = combinedLoadStates
                )
            )
            val state = scenario.toState()

            Then("state should be not empty with error") {
                state shouldBe PagingItemsState.NotEmpty(
                    items = equalsLazyPagingItems,
                    error = Effect.of(errorMessage),
                    isAlsoLoading = false
                )
            }
        }
    }
})

private data class Item(val id: Int)

private class RealPagingItemsStateMapperTestScenario(
    val sut: RealPagingItemsStateMapper
) {

    fun toState() = sut.toState(items = equalsLazyPagingItems)
}

private fun TestScenario(errorMessage: TextRes, lazyPagingItemsSurrogate: LazyPagingItemsSurrogate) =
    RealPagingItemsStateMapperTestScenario(
        sut = RealPagingItemsStateMapper(
            FakeNetworkErrorToMessageMapper(message = errorMessage),
            FakeLazyPagingItemsSurrogateMapper(surrogate = lazyPagingItemsSurrogate)
        )
    )
