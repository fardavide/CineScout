package cinescout.details.presentation.presenter

import app.cash.molecule.RecompositionMode
import app.cash.molecule.moleculeFlow
import app.cash.turbine.test
import arrow.core.none
import arrow.core.some
import cinescout.FakeGetCurrentDateTime
import cinescout.details.domain.model.ScreenplayWithExtra
import cinescout.details.domain.sample.ScreenplayWithExtraSample
import cinescout.details.domain.usecase.FakeGetScreenplayWithExtras
import cinescout.details.presentation.action.ScreenplayDetailsAction
import cinescout.details.presentation.mapper.DetailsActionsUiModelMapper
import cinescout.details.presentation.mapper.DetailsSeasonsUiModelMapper
import cinescout.details.presentation.mapper.ScreenplayDetailsUiModelMapper
import cinescout.details.presentation.model.DetailsActionItemUiModel
import cinescout.history.domain.usecase.FakeAddToHistory
import cinescout.network.usecase.FakeObserveConnectionStatus
import cinescout.progress.domain.model.MovieProgress
import cinescout.progress.domain.sample.ScreenplayProgressSample
import cinescout.progress.domain.usecase.FakeCalculateProgress
import cinescout.rating.domain.usecase.FakeRateScreenplay
import cinescout.resources.ImageRes
import cinescout.resources.R.drawable
import cinescout.resources.R.string
import cinescout.resources.TextRes
import cinescout.sample.DateTimeSample
import cinescout.screenplay.domain.sample.ScreenplayIdsSample
import cinescout.seasons.domain.usecase.FakeGetTvShowSeasonsWithEpisodes
import cinescout.test.android.MoleculeTestExtension
import cinescout.test.kotlin.awaitLastItem
import cinescout.utils.android.NetworkErrorToMessageMapper
import cinescout.watchlist.domain.usecase.FakeToggleWatchlist
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf

class ScreenplayDetailsPresenterTest : BehaviorSpec({
    extensions(MoleculeTestExtension())

    val movie = ScreenplayWithExtraSample.Inception

    Given("a movie without history") {
        val movieProgress = ScreenplayProgressSample.Inception_Unwatched

        When("started") {
            val scenario = TestScenario(
                screenplayWithExtras = movie,
                movieProgress = movieProgress
            )

            Then("progress is unwatched") {
                scenario.flow.test {
                    awaitLastItem().actionsUiModel.actionItemUiModel shouldBe DetailsActionItemUiModel(
                        badgeResource = none(),
                        contentDescription = TextRes(string.details_add_to_history),
                        imageRes = ImageRes(drawable.ic_clock)
                    )
                }
            }
        }
    }

    Given("a movie with history") {
        val movieProgress = ScreenplayProgressSample.Inception_WatchedOnce

        When("started") {
            val scenario = TestScenario(
                screenplayWithExtras = movie,
                movieProgress = movieProgress
            )

            Then("progress is watched") {
                scenario.flow.test {
                    awaitLastItem().actionsUiModel.actionItemUiModel shouldBe DetailsActionItemUiModel(
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

    val flow = moleculeFlow(mode = RecompositionMode.Immediate) {
        sut.models(ScreenplayIdsSample.Inception, actions = actionsFlow)
    }.distinctUntilChanged()
}

private fun TestScenario(
    actionsFlow: Flow<ScreenplayDetailsAction> = emptyFlow(),
    movieProgress: MovieProgress = ScreenplayProgressSample.Inception_Unwatched,
    screenplayWithExtras: ScreenplayWithExtra? = null,
    screenplayWithExtrasFlow: Flow<ScreenplayWithExtra>? = screenplayWithExtras?.let(::flowOf)
) = ScreenplayDetailsPresenterTestScenario(
    actionsFlow = actionsFlow,
    sut = ScreenplayDetailsPresenter(
        addToHistory = FakeAddToHistory(),
        calculateProgress = FakeCalculateProgress(movieProgress = movieProgress),
        detailsUiModelMapper = ScreenplayDetailsUiModelMapper(),
        detailsActionsUiModelMapper = DetailsActionsUiModelMapper(),
        getScreenplayWithExtras = FakeGetScreenplayWithExtras(screenplayWithExtraFlow = screenplayWithExtrasFlow),
        getTvShowSeasonsWithEpisodes = FakeGetTvShowSeasonsWithEpisodes(),
        networkErrorToMessageMapper = NetworkErrorToMessageMapper(),
        observeConnectionStatus = FakeObserveConnectionStatus(),
        rateScreenplay = FakeRateScreenplay(),
        seasonsUiModelMapper = DetailsSeasonsUiModelMapper(FakeGetCurrentDateTime(DateTimeSample.Xmas2023)),
        toggleWatchlist = FakeToggleWatchlist()
    )
)
