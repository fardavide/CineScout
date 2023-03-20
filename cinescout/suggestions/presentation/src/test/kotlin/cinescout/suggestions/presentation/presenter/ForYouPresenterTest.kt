package cinescout.suggestions.presentation.presenter

import app.cash.molecule.RecompositionClock
import app.cash.molecule.moleculeFlow
import app.cash.turbine.test
import cinescout.design.FakeNetworkErrorToMessageMapper
import cinescout.suggestions.presentation.mapper.RealForYouItemUiModelMapper
import cinescout.suggestions.presentation.state.ForYouState
import cinescout.test.android.MoleculeTestExtension
import cinescout.voting.domain.usecase.FakeSetDisliked
import cinescout.voting.domain.usecase.FakeSetLiked
import cinescout.watchlist.domain.usecase.FakeAddToWatchlist
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emptyFlow

class ForYouPresenterTest : BehaviorSpec({
    extensions(MoleculeTestExtension())

    Given("presenter") {

        When("started") {
            val scenario = TestScenario()
            moleculeFlow(clock = RecompositionClock.Immediate) {
                scenario.sut.models(
                    actionsFlow = emptyFlow(),
                    suggestedMoviesFlow = emptyFlow(),
                    suggestedTvShowsFlow = emptyFlow()
                )
            }.distinctUntilChanged().test {

                Then("should emit loading state") {
                    awaitItem() shouldBe ForYouState.Loading
                }
            }
        }
    }
})

private class ForYouPresenterTestScenario(
    val sut: ForYouPresenter
)

private fun TestScenario() = ForYouPresenterTestScenario(
    sut = ForYouPresenter(
        addToWatchlist = FakeAddToWatchlist(),
        forYouItemUiModelMapper = RealForYouItemUiModelMapper(),
        networkErrorMapper = FakeNetworkErrorToMessageMapper(),
        setDisliked = FakeSetDisliked(),
        setLiked = FakeSetLiked()
    )
)
