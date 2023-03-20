package cinescout.lists.presentation.mapper

import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import cinescout.design.mapper.FakeNetworkErrorToMessageMapper
import cinescout.design.util.Effect
import cinescout.error.NetworkError
import cinescout.lists.presentation.model.ListFilter
import cinescout.lists.presentation.model.ListItemUiModel
import cinescout.lists.presentation.state.ItemsListState
import cinescout.resources.sample.MessageSample
import cinescout.screenplay.domain.model.ScreenplayType
import cinescout.store5.FetchException
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.every
import io.mockk.mockk

class ItemsListStateMapperTest : BehaviorSpec({

    val errorMessage = MessageSample.NoNetworkError
    val mapper = ItemsListStateMapper(messageMapper = FakeNetworkErrorToMessageMapper(message = errorMessage))

    Given("any filter") {
        val filter = ListFilter.Rated

        And("any type") {
            val type = ScreenplayType.All

            When("refresh is loading") {
                val refresh = LoadState.Loading

                And("is empty") {
                    val isEmpty = true

                    val items = mockLazyPagingItems(
                        isEmpty = isEmpty,
                        refresh = refresh
                    )
                    val state = mapper.toState(
                        filter = filter,
                        items = items,
                        type = type
                    )

                    Then("items state should be loading") {
                        state.itemsState shouldBe ItemsListState.ItemsState.Loading
                    }
                }

                And("is not empty") {
                    val isEmpty = false

                    val items = mockLazyPagingItems(
                        isEmpty = isEmpty,
                        refresh = LoadState.Loading
                    )
                    val state = mapper.toState(
                        filter = filter,
                        items = items,
                        type = type
                    )

                    Then("items state should be NonEmpty") {
                        state.itemsState shouldBe ItemsListState.ItemsState.NotEmpty(items)
                    }
                }
            }

            When("refresh is not loading") {
                val refresh = LoadState.NotLoading(false)

                And("is empty") {
                    val isEmpty = true

                    val items = mockLazyPagingItems(
                        isEmpty = isEmpty,
                        refresh = refresh
                    )
                    val state = mapper.toState(
                        filter = filter,
                        items = items,
                        type = type
                    )

                    Then("items state should be empty") {
                        state.itemsState.shouldBeInstanceOf<ItemsListState.ItemsState.Empty>()
                    }
                }

                And("is not empty") {
                    val isEmpty = false

                    val items = mockLazyPagingItems(
                        isEmpty = isEmpty,
                        refresh = refresh
                    )
                    val state = mapper.toState(
                        filter = filter,
                        items = items,
                        type = type
                    )

                    Then("items state should be NonEmpty") {
                        state.itemsState shouldBe ItemsListState.ItemsState.NotEmpty(items)
                    }
                }
            }

            When("refresh is error") {
                val refresh = LoadState.Error(FetchException(NetworkError.BadRequest))

                And("is empty") {
                    val isEmpty = true

                    val items = mockLazyPagingItems(
                        isEmpty = isEmpty,
                        refresh = refresh
                    )
                    val state = mapper.toState(
                        filter = filter,
                        items = items,
                        type = type
                    )

                    Then("items state should be error") {
                        state.itemsState shouldBe ItemsListState.ItemsState.Error(
                            message = errorMessage
                        )
                    }

                    Then("error effect should not contain the message") {
                        state.errorMessage shouldBe Effect.empty()
                    }
                }

                And("is not empty") {
                    val isEmpty = false

                    val items = mockLazyPagingItems(
                        isEmpty = isEmpty,
                        refresh = refresh
                    )
                    val state = mapper.toState(
                        filter = filter,
                        items = items,
                        type = type
                    )

                    Then("items state should be NonEmpty") {
                        state.itemsState shouldBe ItemsListState.ItemsState.NotEmpty(items)
                    }

                    Then("error effect should contain the message") {
                        state.errorMessage shouldBe Effect.of(errorMessage)
                    }
                }
            }
        }
    }
})

private fun mockLazyPagingItems(isEmpty: Boolean, refresh: LoadState): LazyPagingItems<ListItemUiModel> =
    mockk {
        every { loadState } returns mockk loadState@{
            every { this@loadState.refresh } returns refresh
            every { this@loadState.prepend } returns LoadState.Loading
        }
        every { itemCount } returns if (isEmpty) 0 else 1
    }
