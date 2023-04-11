package cinescout.lists.presentation

import app.cash.molecule.RecompositionClock
import app.cash.molecule.moleculeFlow
import app.cash.turbine.test
import cinescout.lists.domain.ListSorting
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
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emptyFlow

class ItemsListPresenterTest : BehaviorSpec({
    extensions(MoleculeTestExtension(), PagingTestExtension())

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
})

private class ItemsListPresenterTestScenario(
    val sut: ItemsListPresenter
) {

    val flow = moleculeFlow(clock = RecompositionClock.Immediate) {
        sut.models(actions = emptyFlow())
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
