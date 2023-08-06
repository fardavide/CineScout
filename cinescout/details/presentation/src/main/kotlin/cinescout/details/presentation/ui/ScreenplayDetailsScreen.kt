package cinescout.details.presentation.ui

import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import cinescout.design.TestTag
import cinescout.design.theme.CineScoutTheme
import cinescout.design.theme.Dimens
import cinescout.design.ui.BannerScaffold
import cinescout.design.ui.CenteredProgress
import cinescout.design.ui.ConnectionStatusBanner
import cinescout.design.ui.ErrorScreen
import cinescout.design.util.collectAsStateLifecycleAware
import cinescout.details.presentation.action.ScreenplayDetailsAction
import cinescout.details.presentation.previewdata.ScreenplayDetailsScreenPreviewDataProvider
import cinescout.details.presentation.state.DetailsSeasonsState
import cinescout.details.presentation.state.ScreenplayDetailsItemState
import cinescout.details.presentation.state.ScreenplayDetailsState
import cinescout.details.presentation.ui.component.DetailsActionBar
import cinescout.details.presentation.ui.component.DetailsBottomBar
import cinescout.details.presentation.ui.component.DetailsCreditsModal
import cinescout.details.presentation.ui.component.DetailsSideBar
import cinescout.details.presentation.viewmodel.ScreenplayDetailsViewModel
import cinescout.history.domain.usecase.AddToHistory
import cinescout.resources.R.string
import cinescout.screenplay.domain.model.Rating
import cinescout.screenplay.domain.model.id.MovieIds
import cinescout.screenplay.domain.model.id.ScreenplayIds
import cinescout.screenplay.domain.model.id.SeasonIds
import cinescout.screenplay.domain.model.id.TvShowIds
import cinescout.utils.compose.WindowHeightSizeClass
import cinescout.utils.compose.WindowSizeClass
import cinescout.utils.compose.WindowWidthSizeClass
import co.touchlab.kermit.Logger
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun ScreenplayDetailsScreen(
    screenplayIds: ScreenplayIds,
    actions: ScreenplayDetailsScreen.Actions,
    modifier: Modifier = Modifier
) {
    val viewModel: ScreenplayDetailsViewModel = koinViewModel(parameters = {
        parametersOf(screenplayIds)
    })
    val state by viewModel.state.collectAsStateLifecycleAware()
    val screenplayActions = ScreenplayDetailsScreen.ScreenplayActions(
        addToHistory = { viewModel.submit(ScreenplayDetailsAction.AddItemToHistory(it)) },
        rate = { viewModel.submit(ScreenplayDetailsAction.Rate(it)) },
        toggleWatchlist = { viewModel.submit(ScreenplayDetailsAction.ToggleWatchlist) }
    )
    ScreenplayDetailsScreen(
        state = state,
        actions = actions,
        screenplayActions = screenplayActions,
        modifier = modifier
    )
}

@Composable
internal fun ScreenplayDetailsScreen(
    state: ScreenplayDetailsState,
    actions: ScreenplayDetailsScreen.Actions,
    screenplayActions: ScreenplayDetailsScreen.ScreenplayActions,
    modifier: Modifier = Modifier
) {
    Logger.withTag("ScreenplayDetailsScreen").d("State: $state")
    val scope = rememberCoroutineScope()
    val snackbarHostState = SnackbarHostState()

    val comingSoonMessage = stringResource(id = string.coming_soon)
    val comingSoon: () -> Unit = {
        scope.launch { snackbarHostState.showSnackbar(comingSoonMessage) }
    }

    val itemState = state.itemState
    val detailsUiModel = (itemState as? ScreenplayDetailsItemState.Data)?.uiModel
    val screenplayIds = detailsUiModel?.ids

    var addToHistoryModalParams: AddToHistoryModal.Params? by remember(screenplayIds) { mutableStateOf(null) }
    var currentModalSeasonIds: SeasonIds? by remember(screenplayIds) { mutableStateOf(null) }
    var shouldShowCreditsModal by remember(screenplayIds) { mutableStateOf(false) }
    var shouldShowRateModal by remember(screenplayIds) { mutableStateOf(false) }
    var shouldShowSeasonsModal by remember(screenplayIds) { mutableStateOf(false) }

    val seasonsUiModel = (detailsUiModel?.seasonsState as? DetailsSeasonsState.Data)?.uiModel
    val currentModalSeasonUiModel = remember(currentModalSeasonIds, seasonsUiModel) {
        currentModalSeasonIds?.let { seasonIds ->
            seasonsUiModel?.seasonUiModels?.first { it.seasonIds == seasonIds }
        }
    }

    if (addToHistoryModalParams != null) {
        val modalParams = checkNotNull(addToHistoryModalParams)
        val addToHistoryParam = modalParams.addToHistoryParams
        val modalActions = AddToHistoryModal.Actions(
            addToHistory = { screenplayActions.addToHistory(addToHistoryParam) },
            dismiss = { addToHistoryModalParams = null }
        )
        AddToHistoryModal(
            itemTitle = modalParams.itemTitle,
            actions = modalActions
        )
    }
    if (currentModalSeasonUiModel != null) {
        DetailsEpisodesModal(
            uiModel = currentModalSeasonUiModel,
            addToHistory = { addToHistoryModalParams = it },
            dismiss = { currentModalSeasonIds = null }
        )
    }
    if (shouldShowSeasonsModal && seasonsUiModel != null) {
        val modalActions = DetailsSeasonsModal.Actions(
            addToHistory = { addToHistoryModalParams = it },
            dismiss = { shouldShowSeasonsModal = false },
            openEpisodes = { currentModalSeasonIds = it.seasonIds }
        )
        DetailsSeasonsModal(
            uiModel = seasonsUiModel,
            actions = modalActions
        )
    }
    if (shouldShowCreditsModal && detailsUiModel != null) {
        DetailsCreditsModal(
            creditsMembers = detailsUiModel.creditsMembers,
            onDismiss = { shouldShowCreditsModal = false }
        )
    }
    if (shouldShowRateModal && detailsUiModel != null) {
        val modalActions = RateItemModal.Actions(
            dismiss = { shouldShowRateModal = false },
            saveRating = screenplayActions.rate
        )
        RateItemModal(
            itemTitle = detailsUiModel.title,
            itemPersonalRating = detailsUiModel.personalRating ?: 0,
            actions = modalActions
        )
    }

    val barActions = DetailsActionBar.Actions(
        back = actions.back,
        onProgressClick = {
            if (itemState is ScreenplayDetailsItemState.Data) {
                when (itemState.uiModel.ids) {
                    is MovieIds -> addToHistoryModalParams = AddToHistoryModal.Params(
                        itemTitle = itemState.uiModel.title,
                        addToHistoryParams = AddToHistory.Params.Movie(movieIds = itemState.uiModel.ids)
                    )
                    is TvShowIds -> when (itemState.uiModel.seasonsState) {
                        is DetailsSeasonsState.Data -> shouldShowSeasonsModal = true
                        is DetailsSeasonsState.Error,
                        DetailsSeasonsState.Loading,
                        DetailsSeasonsState.NoSeasons -> Unit
                    }
                }
            }
        },
        openEdit = comingSoon,
        openRating = { shouldShowRateModal = true },
        toggleWatchlist = screenplayActions.toggleWatchlist
    )

    BannerScaffold(
        modifier = modifier.testTag(TestTag.ScreenplayDetails),
        banner = { ConnectionStatusBanner(uiModel = state.connectionStatus) },
        sideRail = { windowSizeClass ->
            if (windowSizeClass.width == WindowWidthSizeClass.Expanded) {
                DetailsSideBar(uiModel = state.actionsUiModel, actions = barActions)
            }
        },
        bottomBar = { windowSizeClass ->
            if (windowSizeClass.width != WindowWidthSizeClass.Expanded) {
                DetailsBottomBar(uiModel = state.actionsUiModel, actions = barActions)
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues, windowSizeClass ->
        val layoutDirection = LocalLayoutDirection.current
        Surface(
            modifier = Modifier.padding(
                start = paddingValues.calculateStartPadding(layoutDirection),
                end = paddingValues.calculateEndPadding(layoutDirection),
                bottom = paddingValues.calculateBottomPadding()
            )
        ) {
            ScreenplayDetailsContent(
                state = state.itemState,
                actions = ScreenplayDetailsBody.Actions(
                    back = actions.back,
                    openCredits = { shouldShowCreditsModal = true },
                    openSeasons = { shouldShowSeasonsModal = true }
                ),
                mode = ScreenplayDetailsScreen.Mode.forClass(windowSizeClass)
            )
        }
    }
}

@Composable
private fun ScreenplayDetailsContent(
    state: ScreenplayDetailsItemState,
    actions: ScreenplayDetailsBody.Actions,
    mode: ScreenplayDetailsScreen.Mode
) {
    when (state) {
        is ScreenplayDetailsItemState.Data -> ScreenplayDetailsBody(
            uiModel = state.uiModel,
            actions = actions,
            mode = mode
        )
        is ScreenplayDetailsItemState.Error -> ErrorScreen(text = state.message)
        ScreenplayDetailsItemState.Loading -> CenteredProgress()
    }
}


object ScreenplayDetailsScreen {

    const val ScreenplayIdsKey = "screenplay_ids"

    data class Actions(
        val back: () -> Unit
    ) {

        companion object {

            val Empty = Actions(back = {})
        }
    }

    data class ScreenplayActions(
        val addToHistory: (AddToHistory.Params) -> Unit,
        val rate: (Rating) -> Unit,
        val toggleWatchlist: () -> Unit
    ) {

        companion object {

            val Empty = ScreenplayActions(
                addToHistory = {},
                rate = {},
                toggleWatchlist = {}
            )
        }
    }

    sealed interface Mode {

        data class OnePane(val spacing: Dp) : Mode
        object TwoPane : Mode
        object ThreePane : Mode

        companion object {

            fun forClass(windowSizeClass: WindowSizeClass): Mode {
                return when (windowSizeClass.width) {
                    WindowWidthSizeClass.Compact -> OnePane(spacing = Dimens.Margin.medium)
                    WindowWidthSizeClass.Medium -> when (windowSizeClass.height) {
                        WindowHeightSizeClass.Compact -> OnePane(spacing = Dimens.Margin.medium)
                        WindowHeightSizeClass.Medium,
                        WindowHeightSizeClass.Expanded -> TwoPane
                    }
                    WindowWidthSizeClass.Expanded -> when (windowSizeClass.height) {
                        WindowHeightSizeClass.Compact,
                        WindowHeightSizeClass.Medium -> ThreePane

                        WindowHeightSizeClass.Expanded -> OnePane(spacing = Dimens.Margin.xLarge)
                    }
                }
            }
        }
    }
}

@Composable
@Preview
@Preview(device = Devices.TABLET)
private fun ScreenplayDetailsScreenPreview(
    @PreviewParameter(ScreenplayDetailsScreenPreviewDataProvider::class) state: ScreenplayDetailsState
) {
    CineScoutTheme {
        ScreenplayDetailsScreen(
            state = state,
            actions = ScreenplayDetailsScreen.Actions.Empty,
            screenplayActions = ScreenplayDetailsScreen.ScreenplayActions.Empty
        )
    }
}
