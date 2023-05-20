package cinescout.lists.presentation.presenter

import app.cash.molecule.RecompositionClock
import app.cash.molecule.moleculeFlow
import app.cash.turbine.test
import cinescout.lists.domain.ListSorting
import cinescout.lists.presentation.ItemsListPresenter
import cinescout.lists.presentation.action.ItemsListAction
import cinescout.lists.presentation.mapper.ListItemUiModelMapper
import cinescout.lists.presentation.mapper.SavedListOptionsMapper
import cinescout.lists.presentation.model.ListFilter
import cinescout.rating.domain.usecase.FakeGetPagedPersonalRatings
import cinescout.screenplay.domain.model.ScreenplayTypeFilter
import cinescout.settings.domain.model.SavedListOptions
import cinescout.settings.domain.usecase.FakeGetSavedListOptions
import cinescout.settings.domain.usecase.FakeUpdateSavedListOptions
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

    Given("presenter started") {

        When("no settings saved") {
            val scenario = TestScenario()

            Then("filter is default") {
                scenario.flow.test {
                    awaitItem().filter shouldBe ItemsListPresenter.DefaultListOptions.listFilter
                }
            }

            Then("sorting is default") {
                scenario.flow.test {
                    awaitItem().sorting shouldBe ItemsListPresenter.DefaultListOptions.listSorting
                }
            }

            Then("type is default") {
                scenario.flow.test {
                    awaitItem().type shouldBe ItemsListPresenter.DefaultListOptions.screenplayTypeFilter
                }
            }
        }

        When("settings saved") {
            val savedListOptions = SavedListOptions(
                filter = SavedListOptions.Filter.Liked,
                sorting = SavedListOptions.Sorting.ReleaseDateAscending,
                type = SavedListOptions.Type.TvShows
            )
            val scenario = TestScenario(savedListOptions)

            Then("filter is liked") {
                scenario.flow.test {
                    awaitItem().filter shouldBe ListFilter.Liked
                }
            }

            Then("sorting is release date ascending") {
                scenario.flow.test {
                    awaitItem().sorting shouldBe ListSorting.ReleaseDate.Ascending
                }
            }

            Then("type is tv shows") {
                scenario.flow.test {
                    awaitItem().type shouldBe ScreenplayTypeFilter.TvShows
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

                And("filter is saved") {
                    scenario.lastSavedListOptions()?.filter shouldBe SavedListOptions.Filter.Liked
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

                And("filter is saved") {
                    scenario.lastSavedListOptions()?.filter shouldBe SavedListOptions.Filter.Disliked
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

                And("filter is saved") {
                    scenario.lastSavedListOptions()?.filter shouldBe SavedListOptions.Filter.Rated
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

                And("sorting is saved") {
                    scenario.lastSavedListOptions()?.sorting shouldBe SavedListOptions.Sorting.RatingAscending
                }
            }
        }
    }

    Given("type is all") {

        When("type is changed to movies") {
            val scenario = TestScenario()

            scenario.flow.test {
                awaitItem().type shouldBe ScreenplayTypeFilter.All
                scenario.actions.emit(ItemsListAction.SelectType(ScreenplayTypeFilter.Movies))

                Then("type is movies") {
                    awaitItem().type shouldBe ScreenplayTypeFilter.Movies
                }

                And("type is saved") {
                    scenario.lastSavedListOptions()?.type shouldBe SavedListOptions.Type.Movies
                }
            }
        }

        When("type is changed to tv shows") {
            val scenario = TestScenario()

            scenario.flow.test {
                awaitItem().type shouldBe ScreenplayTypeFilter.All
                scenario.actions.emit(ItemsListAction.SelectType(ScreenplayTypeFilter.TvShows))

                Then("type is series") {
                    awaitItem().type shouldBe ScreenplayTypeFilter.TvShows
                }

                And("type is saved") {
                    scenario.lastSavedListOptions()?.type shouldBe SavedListOptions.Type.TvShows
                }
            }
        }
    }
})

private class ItemsListPresenterTestScenario(
    val sut: ItemsListPresenter,
    private val updateSavedListOptions: FakeUpdateSavedListOptions
) {

    val actions = MutableSharedFlow<ItemsListAction>()

    val flow = moleculeFlow(clock = RecompositionClock.Immediate) {
        sut.models(actions = actions)
    }.distinctUntilChanged()

    fun lastSavedListOptions() = updateSavedListOptions.lastSavedListOptions
}

private fun TestScenario(savedListOptions: SavedListOptions? = null): ItemsListPresenterTestScenario {
    val updateSavedListOptions = FakeUpdateSavedListOptions()
    return ItemsListPresenterTestScenario(
        sut = ItemsListPresenter(
            getPagedDislikedScreenplays = FakeGetPagedDislikedScreenplays(),
            getPagedLikedScreenplays = FakeGetPagedLikedScreenplays(),
            getPagedPersonalRatings = FakeGetPagedPersonalRatings(),
            getPagedWatchlist = FakeGetPagedWatchlist(),
            getSavedListOptions = FakeGetSavedListOptions(savedListOptions),
            listItemUiModelMapper = ListItemUiModelMapper(),
            pagingItemsStateMapper = FakePagingItemsStateMapper(),
            savedListOptionsMapper = SavedListOptionsMapper(),
            updateSavedListOptions = updateSavedListOptions
        ),
        updateSavedListOptions = updateSavedListOptions
    )
}
