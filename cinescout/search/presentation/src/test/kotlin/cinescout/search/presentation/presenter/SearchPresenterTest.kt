package cinescout.search.presentation.presenter

import app.cash.molecule.RecompositionMode
import app.cash.molecule.moleculeFlow
import app.cash.turbine.test
import cinescout.search.domain.usecase.FakeSearchPagedScreenplays
import cinescout.search.presentation.action.SearchAction
import cinescout.search.presentation.model.SearchItemUiModel
import cinescout.search.presentation.sample.SearchItemUiModelSample
import cinescout.search.presentation.state.SearchState
import cinescout.test.android.MoleculeTestExtension
import cinescout.test.android.PagingTestExtension
import cinescout.test.kotlin.expectItem
import cinescout.test.kotlin.expectLastItem
import cinescout.utils.compose.Effect
import cinescout.utils.compose.paging.PagingItemsState
import cinescout.utils.compose.paging.fakePagingItemsStateMapper
import cinescout.utils.compose.paging.lazyPagingItemsOf
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
                val item = expectItem()

                Then("query should be blank") {
                    item.query.shouldBeBlank()
                }

                Then("icon should be none") {
                    item.searchFieldIcon shouldBe SearchState.SearchFieldIcon.None
                }
            }
        }

        When("search action") {
            val scenario = TestScenario(
                actions = flowOf(SearchAction.Search("Inception"))
            )
            scenario.flow.test {
                awaitItem() // skip initial state
                val item = awaitItem()

                Then("query should be set") {
                    item.query shouldBe "Inception"
                }

                Then("icon should be loading") {
                    item.searchFieldIcon shouldBe SearchState.SearchFieldIcon.Loading
                }
            }
        }

        When("search completed") {
            val scenario = TestScenario(
                actions = flowOf(SearchAction.Search("Inception")),
                pagingItemsState = PagingItemsState.NotEmpty(
                    items = lazyPagingItemsOf(
                        SearchItemUiModelSample.Inception
                    ),
                    error = Effect.empty(),
                    isAlsoLoading = false
                )
            )
            scenario.flow.test {
                val item = expectLastItem()

                Then("icon should be clear") {
                    item.searchFieldIcon shouldBe SearchState.SearchFieldIcon.Clear
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

private fun TestScenario(
    actions: Flow<SearchAction> = emptyFlow(),
    pagingItemsState: PagingItemsState<SearchItemUiModel> = PagingItemsState.Loading
) = SearchPresenterTestScenario(
    actions = actions,
    sut = SearchPresenter(
        pagingItemsStateMapper = fakePagingItemsStateMapper(state = pagingItemsState),
        searchScreenplays = FakeSearchPagedScreenplays()
    )
)
