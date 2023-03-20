package cinescout.suggestions.presentation.presenter

import app.cash.molecule.RecompositionClock
import app.cash.molecule.moleculeFlow
import app.cash.turbine.test
import arrow.core.Either
import arrow.core.Nel
import arrow.core.nonEmptyListOf
import arrow.core.right
import cinescout.suggestions.domain.model.SuggestedScreenplayWithExtras
import cinescout.suggestions.domain.model.SuggestionError
import cinescout.suggestions.domain.sample.SuggestedScreenplayWithExtrasSample
import cinescout.suggestions.presentation.action.ForYouAction
import cinescout.suggestions.presentation.mapper.RealForYouItemUiModelMapper
import cinescout.suggestions.presentation.model.ForYouType
import cinescout.suggestions.presentation.sample.ForYouStateSample
import cinescout.suggestions.presentation.state.ForYouState
import cinescout.test.android.MoleculeTestExtension
import cinescout.utils.compose.FakeNetworkErrorToMessageMapper
import cinescout.voting.domain.usecase.FakeSetDisliked
import cinescout.voting.domain.usecase.FakeSetLiked
import cinescout.watchlist.domain.usecase.FakeAddToWatchlist
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf

class ForYouPresenterTest : BehaviorSpec({
    extensions(MoleculeTestExtension())

    Given("presenter") {

        When("started") {
            val scenario = TestScenario()
            scenario.flow.test {

                Then("should emit loading state") {
                    awaitItem() shouldBe ForYouState.Loading
                }
            }
        }

        When("suggestions are loaded") {
            val movies = flowOf(
                nonEmptyListOf(SuggestedScreenplayWithExtrasSample.Inception).right()
            )
            val tvShows = flowOf(
                nonEmptyListOf(SuggestedScreenplayWithExtrasSample.Grimm).right()
            )

            And("type isn't changed") {
                val scenario = TestScenario(
                    suggestedMoviesFlow = movies,
                    suggestedTvShowsFlow = tvShows
                )
                scenario.flow.test {
                    awaitItem() shouldBe ForYouState.Loading

                    Then("suggested item is the first movie") {
                        awaitItem() shouldBe ForYouStateSample.WithInception
                    }
                }
            }

            And("type is tv shows") {
                val scenario = TestScenario(
                    actionsFlow = flowOf(ForYouAction.SelectForYouType(ForYouType.TvShows)),
                    suggestedMoviesFlow = movies,
                    suggestedTvShowsFlow = tvShows
                )
                scenario.flow.test {
                    awaitItem() shouldBe ForYouState.Loading
                    awaitItem() shouldBe ForYouStateSample.WithInception

                    Then("suggested item is the first tv show") {
                        awaitItem() shouldBe ForYouStateSample.WithGrimm
                    }
                }
            }
        }
    }
})

private class ForYouPresenterTestScenario(
    actionsFlow: Flow<ForYouAction>,
    suggestedMoviesFlow: Flow<Either<SuggestionError, Nel<SuggestedScreenplayWithExtras>>>,
    suggestedTvShowsFlow: Flow<Either<SuggestionError, Nel<SuggestedScreenplayWithExtras>>>,
    val sut: ForYouPresenter
) {

    val flow = moleculeFlow(clock = RecompositionClock.Immediate) {
        sut.models(
            actionsFlow = actionsFlow,
            suggestedMoviesFlow = suggestedMoviesFlow,
            suggestedTvShowsFlow = suggestedTvShowsFlow
        )
    }.distinctUntilChanged()
}

private fun TestScenario(
    actionsFlow: Flow<ForYouAction> = emptyFlow(),
    suggestedMoviesFlow: Flow<Either<SuggestionError, Nel<SuggestedScreenplayWithExtras>>> = emptyFlow(),
    suggestedTvShowsFlow: Flow<Either<SuggestionError, Nel<SuggestedScreenplayWithExtras>>> = emptyFlow()
) = ForYouPresenterTestScenario(
    actionsFlow = actionsFlow,
    suggestedMoviesFlow = suggestedMoviesFlow,
    suggestedTvShowsFlow = suggestedTvShowsFlow,
    sut = ForYouPresenter(
        addToWatchlist = FakeAddToWatchlist(),
        forYouItemUiModelMapper = RealForYouItemUiModelMapper(),
        networkErrorMapper = FakeNetworkErrorToMessageMapper(),
        setDisliked = FakeSetDisliked(),
        setLiked = FakeSetLiked()
    )
)
