package cinescout.search.presentation.presenter

import app.cash.molecule.RecompositionClock
import app.cash.molecule.moleculeFlow
import app.cash.turbine.test
import cinescout.search.presentation.action.SearchAction
import cinescout.test.android.MoleculeTestExtension
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldBeBlank
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf

class SearchPresenterTest : BehaviorSpec({
    extensions(MoleculeTestExtension())

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

    val flow = moleculeFlow(clock = RecompositionClock.Immediate) {
        sut.models(actions = actions)
    }.distinctUntilChanged()
}

private fun TestScenario(actions: Flow<SearchAction> = emptyFlow()) = SearchPresenterTestScenario(
    actions = actions,
    sut = SearchPresenter()
)