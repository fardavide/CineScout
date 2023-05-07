package cinescout.utils.compose.paging

import androidx.paging.LoadState
import cinescout.error.NetworkError
import cinescout.resources.sample.MessageSample
import cinescout.store5.FetchException
import cinescout.test.android.PagingTestExtension
import cinescout.test.android.createLazyPagingItems
import cinescout.utils.compose.Effect
import cinescout.utils.compose.FakeNetworkErrorToMessageMapper
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

// https://github.com/fardavide/CineScout/issues/291
class RealPagingItemsStateMapperTest : BehaviorSpec({
    coroutineTestScope = true
    extensions(PagingTestExtension())

    val errorMessage = MessageSample.NoNetworkError
    val mapper = RealPagingItemsStateMapper(messageMapper = FakeNetworkErrorToMessageMapper(message = errorMessage))

    xGiven("refresh is loading") {
        val refresh = LoadState.Loading

        When("is empty") {
            val isEmpty = true

            val items = createLazyPagingItems<Item>(
                isEmpty = isEmpty,
                refresh = refresh
            )
            val state = mapper.toState(items = items)

            Then("state should be loading") {
                state shouldBe PagingItemsState.Loading
            }
        }

        When("is not empty") {
            val isEmpty = false

            val items = createLazyPagingItems<Item>(
                isEmpty = isEmpty,
                refresh = refresh
            )
            val state = mapper.toState(items = items)

            Then("state should be not empty") {
                state shouldBe PagingItemsState.NotEmpty(items = items, error = Effect.empty(), isAlsoLoading = true)
            }
        }
    }

    xGiven("refresh is not loading") {
        val refresh = LoadState.NotLoading(false)

        When("is empty") {
            val isEmpty = true

            val items = createLazyPagingItems<Item>(
                isEmpty = isEmpty,
                refresh = refresh
            )
            val state = mapper.toState(items = items)

            Then("state should be empty") {
                state shouldBe PagingItemsState.Empty
            }
        }

        When("is not empty") {
            val isEmpty = false

            val items = createLazyPagingItems<Item>(
                isEmpty = isEmpty,
                refresh = refresh
            )
            val state = mapper.toState(items = items)

            Then("state should be not empty") {
                state shouldBe PagingItemsState.NotEmpty(items = items, error = Effect.empty(), isAlsoLoading = false)
            }
        }
    }

    xGiven("refresh is error") {
        val refresh = LoadState.Error(FetchException(NetworkError.NoNetwork))

        When("is empty") {
            val isEmpty = true

            val items = createLazyPagingItems<Item>(
                isEmpty = isEmpty,
                refresh = refresh
            )
            val state = mapper.toState(items = items)

            Then("state should be error") {
                state shouldBe PagingItemsState.Error(message = errorMessage)
            }
        }

        When("is not empty") {
            val isEmpty = false

            val items = createLazyPagingItems<Item>(
                isEmpty = isEmpty,
                refresh = refresh
            )
            val state = mapper.toState(items = items)

            Then("state should be not empty with error") {
                state shouldBe PagingItemsState.NotEmpty(
                    items = items,
                    error = Effect.of(errorMessage),
                    isAlsoLoading = false
                )
            }
        }
    }
})

private data class Item(val id: Int)
