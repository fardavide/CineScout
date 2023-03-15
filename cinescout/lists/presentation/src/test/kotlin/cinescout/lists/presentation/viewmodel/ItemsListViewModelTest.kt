package cinescout.lists.presentation.viewmodel

import app.cash.turbine.test
import arrow.core.nonEmptyListOf
import cinescout.design.FakeNetworkErrorToMessageMapper
import cinescout.lists.presentation.action.ItemsListAction
import cinescout.lists.presentation.mapper.ListItemUiModelMapper
import cinescout.lists.presentation.model.ListFilter
import cinescout.lists.presentation.model.ListItemUiModel
import cinescout.lists.presentation.sample.ListItemUiModelSample
import cinescout.lists.presentation.state.ItemsListState
import cinescout.rating.domain.model.ScreenplayWithPersonalRating
import cinescout.rating.domain.sample.ScreenplayWithPersonalRatingSample
import cinescout.screenplay.domain.model.Screenplay
import cinescout.screenplay.domain.model.ScreenplayType
import cinescout.screenplay.domain.sample.ScreenplaySample
import cinescout.test.android.ViewModelExtension
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.core.test.TestScope
import io.kotest.core.test.testCoroutineScheduler
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf

class ItemsListViewModelTest : BehaviorSpec({
    extension(ViewModelExtension())

    Given("view model") {

        When("started") {
            val scenario = TestScenario()

            Then("state is loading") {
                scenario.sut.state.test {
                    // TODO awaitItem() shouldBe ItemsListState.Initial
                }
            }

            Then("filter is watchlist") {
                scenario.sut.state.test {
                    awaitItem().filter shouldBe ListFilter.Watchlist
                }
            }

            Then("type is all") {
                scenario.sut.state.test {
                    awaitItem().type shouldBe ScreenplayType.All
                }
            }
        }

        When("watchlist is success") {
            val screenplays = nonEmptyListOf(
                ScreenplaySample.BreakingBad,
                ScreenplaySample.Dexter,
                ScreenplaySample.Inception,
                ScreenplaySample.TheWolfOfWallStreet
            )

            val scenario = TestScenario(watchlist = screenplays)
            testCoroutineScheduler.advanceUntilIdle()

            Then("watchlist is emitted") {
                scenario.sut.state.test {
                    val items = awaitItem().items
                    items.shouldBeInstanceOf<ItemsListState.ItemsState.Data.NotEmpty>()
                    items.items.sortedByTitle() shouldBe with(ListItemUiModelSample) {
                        nonEmptyListOf(
                            BreakingBad,
                            Dexter,
                            Inception,
                            TheWolfOfWallStreet
                        ).withoutPersonalRating()
                    }
                }
            }
        }

        When("select filter action") {
            val newFilter = ListFilter.Disliked
            val scenario = TestScenario()

            scenario.sut.submit(ItemsListAction.SelectFilter(newFilter))
            testCoroutineScheduler.advanceUntilIdle()

            Then("new filter is emitted") {
                scenario.sut.state.test {
                    awaitItem().filter shouldBe newFilter
                }
            }
        }

        When("select type action") {
            val newType = ScreenplayType.Movies
            val scenario = TestScenario()

            scenario.sut.submit(ItemsListAction.SelectType(newType))
            testCoroutineScheduler.advanceUntilIdle()

            Then("new type is emitted") {
                scenario.sut.state.test {
                    awaitItem().type shouldBe newType
                }
            }
        }
    }

    Given("filter is rated") {
        val ratings = nonEmptyListOf(
            ScreenplayWithPersonalRatingSample.BreakingBad,
            ScreenplayWithPersonalRatingSample.Dexter,
            ScreenplayWithPersonalRatingSample.Inception,
            ScreenplayWithPersonalRatingSample.TheWolfOfWallStreet
        )
        val filter = ListFilter.Rated

        And("type is all") {
            val type = ScreenplayType.All

            When("is success") {
                val scenario = TestScenario(rated = ratings).withFilter(filter).withType(type)

                Then("items are emitted") {
                    scenario.sut.state.test {
                        val items = awaitItem().items
                        items.shouldBeInstanceOf<ItemsListState.ItemsState.Data.NotEmpty>()
                        items.items.sortedByTitle() shouldBe with(ListItemUiModelSample) {
                            nonEmptyListOf(
                                BreakingBad,
                                Dexter,
                                Inception,
                                TheWolfOfWallStreet
                            )
                        }
                    }
                }
            }
        }

        And("type is movies") {
            val type = ScreenplayType.Movies

            When("is success") {
                val scenario = TestScenario(rated = ratings).withFilter(filter).withType(type)

                Then("items are emitted") {
                    scenario.sut.state.test {
                        val items = awaitItem().items
                        items.shouldBeInstanceOf<ItemsListState.ItemsState.Data.NotEmpty>()
                        items.items.sortedByTitle() shouldBe with(ListItemUiModelSample) {
                            nonEmptyListOf(
                                Inception,
                                TheWolfOfWallStreet
                            )
                        }
                    }
                }
            }
        }

        And("type is tv shows") {
            val type = ScreenplayType.TvShows

            When("is success") {
                val scenario = TestScenario(rated = ratings).withFilter(filter).withType(type)

                Then("items are emitted") {
                    scenario.sut.state.test {
                        val items = awaitItem().items
                        items.shouldBeInstanceOf<ItemsListState.ItemsState.Data.NotEmpty>()
                        items.items.sortedByTitle() shouldBe with(ListItemUiModelSample) {
                            nonEmptyListOf(
                                BreakingBad,
                                Dexter
                            )
                        }
                    }
                }
            }
        }
    }

    Given("filter is liked") {
        val screenplays = nonEmptyListOf(
            ScreenplaySample.BreakingBad,
            ScreenplaySample.Dexter,
            ScreenplaySample.Inception,
            ScreenplaySample.TheWolfOfWallStreet
        )
        val filter = ListFilter.Liked

        And("type is all") {
            val type = ScreenplayType.All

            When("is success") {
                val scenario = TestScenario(likes = screenplays).withFilter(filter).withType(type)

                Then("items are emitted") {
                    scenario.sut.state.test {
                        val items = awaitItem().items
                        items.shouldBeInstanceOf<ItemsListState.ItemsState.Data.NotEmpty>()
                        items.items.sortedByTitle() shouldBe with(ListItemUiModelSample) {
                            nonEmptyListOf(
                                BreakingBad,
                                Dexter,
                                Inception,
                                TheWolfOfWallStreet
                            ).withoutPersonalRating()
                        }
                    }
                }
            }
        }

        And("type is movies") {
            val type = ScreenplayType.Movies

            When("is success") {
                val scenario = TestScenario(likes = screenplays).withFilter(filter).withType(type)

                Then("items are emitted") {
                    scenario.sut.state.test {
                        val items = awaitItem().items
                        items.shouldBeInstanceOf<ItemsListState.ItemsState.Data.NotEmpty>()
                        items.items.sortedByTitle() shouldBe with(ListItemUiModelSample) {
                            nonEmptyListOf(
                                Inception,
                                TheWolfOfWallStreet
                            ).withoutPersonalRating()
                        }
                    }
                }
            }
        }

        And("type is tv shows") {
            val type = ScreenplayType.TvShows

            When("is success") {
                val scenario = TestScenario(likes = screenplays).withFilter(filter).withType(type)

                Then("items are emitted") {
                    scenario.sut.state.test {
                        val items = awaitItem().items
                        items.shouldBeInstanceOf<ItemsListState.ItemsState.Data.NotEmpty>()
                        items.items.sortedByTitle() shouldBe with(ListItemUiModelSample) {
                            nonEmptyListOf(
                                BreakingBad,
                                Dexter
                            ).withoutPersonalRating()
                        }
                    }
                }
            }
        }
    }

    Given("filter is disliked") {
        val screenplays = nonEmptyListOf(
            ScreenplaySample.BreakingBad,
            ScreenplaySample.Dexter,
            ScreenplaySample.Inception,
            ScreenplaySample.TheWolfOfWallStreet
        )
        val filter = ListFilter.Disliked

        And("type is all") {
            val type = ScreenplayType.All

            When("is success") {
                val scenario = TestScenario(dislikes = screenplays).withFilter(filter).withType(type)

                Then("items are emitted") {
                    scenario.sut.state.test {
                        val items = awaitItem().items
                        items.shouldBeInstanceOf<ItemsListState.ItemsState.Data.NotEmpty>()
                        items.items.sortedByTitle() shouldBe with(ListItemUiModelSample) {
                            nonEmptyListOf(
                                BreakingBad,
                                Dexter,
                                Inception,
                                TheWolfOfWallStreet
                            ).withoutPersonalRating()
                        }
                    }
                }
            }
        }

        And("type is movies") {
            val type = ScreenplayType.Movies

            When("is success") {
                val scenario = TestScenario(dislikes = screenplays).withFilter(filter).withType(type)

                Then("items are emitted") {
                    scenario.sut.state.test {
                        val items = awaitItem().items
                        items.shouldBeInstanceOf<ItemsListState.ItemsState.Data.NotEmpty>()
                        items.items.sortedByTitle() shouldBe with(ListItemUiModelSample) {
                            nonEmptyListOf(
                                Inception,
                                TheWolfOfWallStreet
                            ).withoutPersonalRating()
                        }
                    }
                }
            }
        }

        And("type is tv shows") {
            val type = ScreenplayType.TvShows

            When("is success") {
                val scenario = TestScenario(dislikes = screenplays).withFilter(filter).withType(type)

                Then("items are emitted") {
                    scenario.sut.state.test {
                        val items = awaitItem().items
                        items.shouldBeInstanceOf<ItemsListState.ItemsState.Data.NotEmpty>()
                        items.items.sortedByTitle() shouldBe with(ListItemUiModelSample) {
                            nonEmptyListOf(
                                BreakingBad,
                                Dexter
                            ).withoutPersonalRating()
                        }
                    }
                }
            }
        }
    }
})

private fun List<ListItemUiModel>.sortedByTitle() = sortedBy { it.title }

private class ItemsListViewModelTestScenario(
    val sut: ItemsListViewModel
) {

    context(TestScope)
    fun withFilter(filter: ListFilter) = apply {
        sut.submit(ItemsListAction.SelectFilter(filter))
        testCoroutineScheduler.advanceUntilIdle()
    }

    context(TestScope)
    fun withType(type: ScreenplayType) = apply {
        sut.submit(ItemsListAction.SelectType(type))
        testCoroutineScheduler.advanceUntilIdle()
    }
}

@Suppress("UNUSED_PARAMETER")
private fun TestScenario(
    dislikes: List<Screenplay>? = null,
    likes: List<Screenplay>? = null,
    rated: List<ScreenplayWithPersonalRating>? = null,
    watchlist: List<Screenplay>? = null
) = ItemsListViewModelTestScenario(
    sut = ItemsListViewModel(
        errorToMessageMapper = FakeNetworkErrorToMessageMapper(),
        listItemUiModelMapper = ListItemUiModelMapper()
    )
)
