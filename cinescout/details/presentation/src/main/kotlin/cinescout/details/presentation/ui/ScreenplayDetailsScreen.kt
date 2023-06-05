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
import cinescout.details.presentation.state.ScreenplayDetailsItemState
import cinescout.details.presentation.state.ScreenplayDetailsState
import cinescout.details.presentation.ui.component.DetailsActionBar
import cinescout.details.presentation.ui.component.DetailsBottomBar
import cinescout.details.presentation.ui.component.DetailsCreditsModal
import cinescout.details.presentation.ui.component.DetailsSideBar
import cinescout.details.presentation.viewmodel.ScreenplayDetailsViewModel
import cinescout.resources.R.string
import cinescout.screenplay.domain.model.Rating
import cinescout.screenplay.domain.model.ids.ScreenplayIds
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
        addToHistory = { viewModel.submit(ScreenplayDetailsAction.AddToHistory) },
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

    var shouldShowAddToHistoryModal by remember { mutableStateOf(false) }
    var shouldShowCreditsModal by remember { mutableStateOf(false) }
    var shouldShowRateModal by remember { mutableStateOf(false) }

    val itemState = state.itemState

    if (shouldShowAddToHistoryModal && itemState is ScreenplayDetailsItemState.Data) {
        val modalActions = AddToHistoryModal.Actions(
            dismiss = { shouldShowAddToHistoryModal = false },
            addToHistory = screenplayActions.addToHistory
        )
        AddToHistoryModal(
            itemTitle = itemState.uiModel.title,
            actions = modalActions
        )
    }
    if (shouldShowCreditsModal && itemState is ScreenplayDetailsItemState.Data) {
        DetailsCreditsModal(
            creditsMembers = itemState.uiModel.creditsMembers,
            onDismiss = { shouldShowCreditsModal = false }
        )
    }
    if (shouldShowRateModal && itemState is ScreenplayDetailsItemState.Data) {
        val modalActions = RateItemModal.Actions(
            dismiss = { shouldShowRateModal = false },
            saveRating = screenplayActions.rate
        )
        RateItemModal(
            itemTitle = itemState.uiModel.title,
            itemPersonalRating = itemState.uiModel.personalRating ?: 0,
            actions = modalActions
        )
    }

    val barActions = DetailsActionBar.Actions(
        back = actions.back,
        openAddToHistory = { shouldShowAddToHistoryModal = true },
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
                mode = ScreenplayDetailsScreen.Mode.forClass(windowSizeClass),
                back = actions.back,
                openCredits = { shouldShowCreditsModal = true }
            )
        }
    }
}

@Composable
private fun ScreenplayDetailsContent(
    state: ScreenplayDetailsItemState,
    mode: ScreenplayDetailsScreen.Mode,
    back: () -> Unit,
    openCredits: () -> Unit
) {
    when (state) {
        is ScreenplayDetailsItemState.Data -> ScreenplayDetailsBody(
            uiModel = state.uiModel,
            mode = mode,
            back = back,
            openCredits = openCredits
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
        val addToHistory: () -> Unit,
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
                    WindowWidthSizeClass.Compact -> OnePane(spacing = Dimens.Margin.Medium)
                    WindowWidthSizeClass.Medium -> when (windowSizeClass.height) {
                        WindowHeightSizeClass.Compact -> OnePane(spacing = Dimens.Margin.Medium)
                        WindowHeightSizeClass.Medium,
                        WindowHeightSizeClass.Expanded -> TwoPane
                    }
                    WindowWidthSizeClass.Expanded -> when (windowSizeClass.height) {
                        WindowHeightSizeClass.Compact,
                        WindowHeightSizeClass.Medium -> ThreePane

                        WindowHeightSizeClass.Expanded -> OnePane(spacing = Dimens.Margin.XLarge)
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
