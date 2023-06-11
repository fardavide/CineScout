package cinescout.details.presentation.presenter

import app.cash.molecule.RecompositionClock
import app.cash.molecule.moleculeFlow
import app.cash.turbine.test
import arrow.core.none
import arrow.core.some
import cinescout.details.domain.model.ScreenplayWithExtra
import cinescout.details.domain.sample.ScreenplayWithExtraSample
import cinescout.details.domain.usecase.FakeGetScreenplayWithExtras
import cinescout.details.presentation.action.ScreenplayDetailsAction
import cinescout.details.presentation.mapper.DetailsActionsUiModelMapper
import cinescout.details.presentation.mapper.ScreenplayDetailsUiModelMapper
import cinescout.details.presentation.model.DetailsActionItemUiModel
import cinescout.history.domain.model.MovieHistory
import cinescout.history.domain.model.ScreenplayHistory
import cinescout.history.domain.sample.ScreenplayHistoryItemSample
import cinescout.history.domain.usecase.FakeAddToHistory
import cinescout.network.usecase.FakeObserveConnectionStatus
import cinescout.progress.domain.usecase.FakeCalculateProgress
import cinescout.rating.domain.usecase.FakeRateScreenplay
import cinescout.resources.ImageRes
import cinescout.resources.R.drawable
import cinescout.resources.R.string
import cinescout.resources.TextRes
import cinescout.screenplay.domain.sample.ScreenplayIdsSample
import cinescout.seasons.domain.store.FakeTvShowSeasonsWithEpisodesStore
import cinescout.test.android.MoleculeTestExtension
import cinescout.utils.compose.NetworkErrorToMessageMapper
import cinescout.watchlist.domain.usecase.FakeToggleWatchlist
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf

class ScreenplayDetailsPresenterTest : BehaviorSpec({
    extensions(MoleculeTestExtension())

    Given("a movie without history") {
        val screenplayFlow = MutableStateFlow(
            ScreenplayWithExtraSample.Inception.apply {
                history = ScreenplayHistory.empty(ScreenplayIdsSample.Inception)
            }
        )
        val screenplayWithHistory = ScreenplayWithExtraSample.Inception.apply {
            history = MovieHistory(
                items = listOf(ScreenplayHistoryItemSample.Inception),
                screenplayIds = ScreenplayIdsSample.Inception
            )
        }

        xWhen("history is added") {
            val scenario = TestScenario(screenplayWithExtrasFlow = screenplayFlow)
            scenario.flow.test {
                awaitItem().actionsUiModel.actionItemUiModel shouldBe DetailsActionItemUiModel(
                    badgeResource = none(),
                    contentDescription = TextRes(string.details_add_to_history),
                    imageRes = ImageRes(drawable.ic_clock)
                )
                screenplayFlow.emit(screenplayWithHistory)

                Then("history is in actions") {
                    awaitItem().actionsUiModel.actionItemUiModel shouldBe DetailsActionItemUiModel(
                        badgeResource = ImageRes(drawable.ic_check_round_color).some(),
                        contentDescription = TextRes(string.details_add_to_history),
                        imageRes = ImageRes(drawable.ic_clock)
                    )
                }
            }
        }
    }
})

private class ScreenplayDetailsPresenterTestScenario(
    actionsFlow: Flow<ScreenplayDetailsAction>,
    val sut: ScreenplayDetailsPresenter
) {

    val flow = moleculeFlow(clock = RecompositionClock.Immediate) {
        sut.models(ScreenplayIdsSample.Inception, actions = actionsFlow)
    }.distinctUntilChanged()
}

private fun TestScenario(
    actionsFlow: Flow<ScreenplayDetailsAction> = emptyFlow(),
    screenplayWithExtras: ScreenplayWithExtra? = null,
    screenplayWithExtrasFlow: Flow<ScreenplayWithExtra>? = screenplayWithExtras?.let(::flowOf)
) = ScreenplayDetailsPresenterTestScenario(
    actionsFlow = actionsFlow,
    sut = ScreenplayDetailsPresenter(
        addToHistory = FakeAddToHistory(),
        calculateProgress = FakeCalculateProgress(),
        detailsActionsUiModelMapper = DetailsActionsUiModelMapper(),
        detailsUiModelMapper = ScreenplayDetailsUiModelMapper(),
        getScreenplayWithExtras = FakeGetScreenplayWithExtras(screenplayWithExtraFlow = screenplayWithExtrasFlow),
        networkErrorToMessageMapper = NetworkErrorToMessageMapper(),
        observeConnectionStatus = FakeObserveConnectionStatus(),
        rateScreenplay = FakeRateScreenplay(),
        seasonsWithEpisodesStore = FakeTvShowSeasonsWithEpisodesStore(),
        toggleWatchlist = FakeToggleWatchlist()
    )
)
