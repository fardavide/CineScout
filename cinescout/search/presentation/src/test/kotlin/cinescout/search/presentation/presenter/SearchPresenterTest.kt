package cinescout.search.presentation.presenter

import app.cash.molecule.RecompositionMode
import app.cash.molecule.moleculeFlow
import app.cash.turbine.test
import cinescout.search.domain.usecase.FakeSearchPagedScreenplays
import cinescout.search.presentation.action.SearchAction
import cinescout.test.android.MoleculeTestExtension
import cinescout.test.android.PagingTestExtension
import cinescout.utils.compose.paging.FakePagingItemsStateMapper
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldBeBlank
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf

class SearchPresenterTest : BehaviorSpec({
    extensions(MoleculeTestExtension(), PagingTestExtension())

    Given("a search presenter") {

        When("no actions") {
            val scenario = TestScenario()
            scenario.flow.test {

                Then("query should be blank") {
                    awaitItem().query.shouldBeBlank()
                }
            }
        }

        When("search action") {
            val scenario = TestScenario(
                actions = flowOf(SearchAction.Search("Inception"))
            )
            scenario.flow.test {
                awaitItem() // skip initial state

                Then("query should be set") {
                    awaitItem().query shouldBe "Inception"
                }
            }
        }
    }
})

private class SearchPresenterTestScenario(
    actions: Flow<SearchAction>,
    val sut: SearchPresenter
) {

    val flow = moleculeFlow(mode = RecompositionMode.Immediate) {
        sut.models(actions = actions)
    }.distinctUntilChanged()
}

private fun TestScenario(actions: Flow<SearchAction> = emptyFlow()) = SearchPresenterTestScenario(
    actions = actions,
    sut = SearchPresenter(
        pagingItemsStateMapper = FakePagingItemsStateMapper(),
        searchScreenplays = FakeSearchPagedScreenplays()
    )
)
