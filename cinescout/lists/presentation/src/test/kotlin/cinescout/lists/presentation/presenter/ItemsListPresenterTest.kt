package cinescout.lists.presentation.presenter

import app.cash.molecule.RecompositionClock
import app.cash.molecule.moleculeFlow
import app.cash.turbine.test
import cinescout.lists.domain.ListSorting
import cinescout.lists.presentation.ItemsListPresenter
import cinescout.lists.presentation.action.ItemsListAction
import cinescout.lists.presentation.mapper.ListItemUiModelMapper
import cinescout.lists.presentation.model.ListFilter
import cinescout.rating.domain.usecase.FakeGetPagedPersonalRatings
import cinescout.screenplay.domain.model.ScreenplayType
import cinescout.test.android.MoleculeTestExtension
import cinescout.test.android.PagingTestExtension
import cinescout.utils.compose.paging.FakePagingItemsStateMapper
import cinescout.voting.domain.usecase.FakeGetPagedDislikedScreenplays
import cinescout.voting.domain.usecase.FakeGetPagedLikedScreenplays
import cinescout.watchlist.domain.usecase.FakeGetPagedWatchlist
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.distinctUntilChanged

class ItemsListPresenterTest : BehaviorSpec({
    extensions(MoleculeTestExtension(), PagingTestExtension())
    coroutineTestScope = true

    Given("presenter") {

        When("started") {
            val scenario = TestScenario()

            Then("filter is watchlist") {
                scenario.flow.test {
                    awaitItem().filter shouldBe ListFilter.Watchlist
                }
            }

            Then("sorting is rating descending") {
                scenario.flow.test {
                    awaitItem().sorting shouldBe ListSorting.Rating.Descending
                }
            }

            Then("type is all") {
                scenario.flow.test {
                    awaitItem().type shouldBe ScreenplayType.All
                }
            }
        }
    }

    Given("filter is watchlist") {

        When("filter is changed to liked") {
            val scenario = TestScenario()

            scenario.flow.test {
                awaitItem().filter shouldBe ListFilter.Watchlist
                scenario.actions.emit(ItemsListAction.SelectFilter(ListFilter.Liked))

                Then("filter is liked") {
                    awaitItem().filter shouldBe ListFilter.Liked
                }
            }
        }

        When("filter is changed to disliked") {
            val scenario = TestScenario()

            scenario.flow.test {
                awaitItem().filter shouldBe ListFilter.Watchlist
                scenario.actions.emit(ItemsListAction.SelectFilter(ListFilter.Disliked))

                Then("filter is disliked") {
                    awaitItem().filter shouldBe ListFilter.Disliked
                }
            }
        }

        When("filter is changed to rated") {
            val scenario = TestScenario()

            scenario.flow.test {
                awaitItem().filter shouldBe ListFilter.Watchlist
                scenario.actions.emit(ItemsListAction.SelectFilter(ListFilter.Rated))

                Then("filter is rated") {
                    awaitItem().filter shouldBe ListFilter.Rated
                }
            }
        }
    }

    Given("sorting is rating descending") {

        When("sorting is changed to rating ascending") {
            val scenario = TestScenario()

            scenario.flow.test {
                awaitItem().sorting shouldBe ListSorting.Rating.Descending
                scenario.actions.emit(ItemsListAction.SelectSorting(ListSorting.Rating.Ascending))

                Then("sorting is rating ascending") {
                    awaitItem().sorting shouldBe ListSorting.Rating.Ascending
                }
            }
        }
    }

    Given("type is all") {

        When("type is changed to movies") {
            val scenario = TestScenario()

            scenario.flow.test {
                awaitItem().type shouldBe ScreenplayType.All
                scenario.actions.emit(ItemsListAction.SelectType(ScreenplayType.Movies))

                Then("type is movies") {
                    awaitItem().type shouldBe ScreenplayType.Movies
                }
            }
        }

        When("type is changed to tv shows") {
            val scenario = TestScenario()

            scenario.flow.test {
                awaitItem().type shouldBe ScreenplayType.All
                scenario.actions.emit(ItemsListAction.SelectType(ScreenplayType.TvShows))

                Then("type is series") {
                    awaitItem().type shouldBe ScreenplayType.TvShows
                }
            }
        }
    }
})

private class ItemsListPresenterTestScenario(
    val sut: ItemsListPresenter
) {

    val actions = MutableSharedFlow<ItemsListAction>()

    val flow = moleculeFlow(clock = RecompositionClock.Immediate) {
        sut.models(actions = actions)
    }.distinctUntilChanged()
}

private fun TestScenario(): ItemsListPresenterTestScenario {
    return ItemsListPresenterTestScenario(
        sut = ItemsListPresenter(
            getPagedDislikedScreenplays = FakeGetPagedDislikedScreenplays(),
            getPagedLikedScreenplays = FakeGetPagedLikedScreenplays(),
            getPagedPersonalRatings = FakeGetPagedPersonalRatings(),
            getPagedWatchlist = FakeGetPagedWatchlist(),
            listItemUiModelMapper = ListItemUiModelMapper(),
            pagingItemsStateMapper = FakePagingItemsStateMapper()
        )
    )
}
