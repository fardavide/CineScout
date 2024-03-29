package cinescout.suggestions.presentation.presenter

import app.cash.molecule.RecompositionMode
import app.cash.molecule.moleculeFlow
import app.cash.turbine.test
import arrow.core.Either
import arrow.core.Nel
import arrow.core.NonEmptyList
import arrow.core.left
import arrow.core.nonEmptyListOf
import arrow.core.right
import cinescout.error.NetworkError
import cinescout.suggestions.domain.model.SuggestedScreenplayWithExtra
import cinescout.suggestions.domain.model.SuggestionError
import cinescout.suggestions.domain.sample.SuggestedScreenplayWithExtraSample
import cinescout.suggestions.domain.usecase.FakeGetSuggestionsWithExtras
import cinescout.suggestions.presentation.action.ForYouAction
import cinescout.suggestions.presentation.mapper.RealForYouItemUiModelMapper
import cinescout.suggestions.presentation.model.ForYouType
import cinescout.suggestions.presentation.sample.ForYouStateSample
import cinescout.suggestions.presentation.state.ForYouState
import cinescout.test.android.MoleculeTestExtension
import cinescout.utils.android.FakeNetworkErrorToMessageMapper
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
                    cancelAndIgnoreRemainingEvents()
                }
            }
        }

        When("suggestions are loaded") {
            val suggestions = nonEmptyListOf(
                SuggestedScreenplayWithExtraSample.Inception,
                SuggestedScreenplayWithExtraSample.Grimm
            )

            And("type isn't changed") {
                val scenario = TestScenario(
                    suggestions = suggestions
                )
                scenario.flow.test {
                    awaitItem() shouldBe ForYouState.Loading

                    Then("suggested item is the first movie") {
                        awaitItem() shouldBe ForYouStateSample.Inception
                    }
                }
            }

            And("type is tv shows") {
                val scenario = TestScenario(
                    actionsFlow = flowOf(ForYouAction.SelectForYouType(ForYouType.TvShows)),
                    suggestions = suggestions
                )
                scenario.flow.test {
                    awaitItem() shouldBe ForYouState.Loading
                    awaitItem() shouldBe ForYouStateSample.Inception

                    Then("suggested item is the first tv show") {
                        awaitItem() shouldBe ForYouStateSample.Grimm
                    }
                }
            }
        }

        When("network error") {
            val scenario = TestScenario(
                suggestionsEither = SuggestionError.Source(NetworkError.NoNetwork).left()
            )
            scenario.flow.test {
                awaitItem() shouldBe ForYouState.Loading

                Then("state is error") {
                    awaitItem() shouldBe ForYouStateSample.NetworkError
                }
            }
        }
    }
})

private class ForYouPresenterTestScenario(
    actionsFlow: Flow<ForYouAction>,
    val sut: ForYouPresenter
) {

    val flow = moleculeFlow(mode = RecompositionMode.Immediate) {
        sut.models(actionsFlow = actionsFlow)
    }.distinctUntilChanged()
}

private fun TestScenario(
    actionsFlow: Flow<ForYouAction> = emptyFlow(),
    suggestions: Nel<SuggestedScreenplayWithExtra>? = null,
    suggestionsEither: Either<SuggestionError, NonEmptyList<SuggestedScreenplayWithExtra>> =
        suggestions?.right() ?: SuggestionError.Source(NetworkError.Unknown).left(),
    suggestionsEitherFlow: Flow<Either<SuggestionError, NonEmptyList<SuggestedScreenplayWithExtra>>> =
        flowOf(suggestionsEither)
) = ForYouPresenterTestScenario(
    actionsFlow = actionsFlow,
    sut = ForYouPresenter(
        addToWatchlist = FakeAddToWatchlist(),
        forYouItemUiModelMapper = RealForYouItemUiModelMapper(),
        getSuggestionsWithExtras = FakeGetSuggestionsWithExtras(suggestionsEitherFlow = suggestionsEitherFlow),
        networkErrorMapper = FakeNetworkErrorToMessageMapper(),
        setDisliked = FakeSetDisliked(),
        setLiked = FakeSetLiked(),
        suggestionsStackSize = 1
    )
)
