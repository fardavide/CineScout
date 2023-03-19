package cinescout.suggestions.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import cinescout.design.R.string
import cinescout.design.TestTag
import cinescout.design.string
import cinescout.design.theme.CineScoutTheme
import cinescout.design.theme.Dimens
import cinescout.design.ui.CenteredProgress
import cinescout.design.ui.ErrorScreen
import cinescout.design.util.collectAsStateLifecycleAware
import cinescout.screenplay.domain.model.TmdbScreenplayId
import cinescout.search.presentation.model.SearchLikedItemType
import cinescout.search.presentation.ui.SearchLikedItemScreen
import cinescout.suggestions.presentation.action.ForYouAction
import cinescout.suggestions.presentation.model.ForYouType
import cinescout.suggestions.presentation.sample.ForYouScreenPreviewDataProvider
import cinescout.suggestions.presentation.state.ForYouState
import cinescout.suggestions.presentation.viewmodel.ForYouViewModel
import cinescout.utils.compose.Adaptive
import cinescout.utils.compose.WindowHeightSizeClass
import cinescout.utils.compose.WindowSizeClass
import cinescout.utils.compose.WindowWidthSizeClass
import co.touchlab.kermit.Logger
import org.koin.androidx.compose.koinViewModel

@Composable
fun ForYouScreen(actions: ForYouScreen.Actions, modifier: Modifier = Modifier) {
    val viewModel: ForYouViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateLifecycleAware()

    val itemActions = ForYouItem.Actions(
        addToWatchlist = { itemId -> viewModel.submit(ForYouAction.AddToWatchlist(itemId)) },
        toDetails = actions.toScreenplayDetails
    )

    val buttonsActions = ForYouButtons.Actions(
        dislike = { itemId -> viewModel.submit(ForYouAction.Dislike(itemId)) },
        like = { itemId -> viewModel.submit(ForYouAction.Like(itemId)) }
    )

    ForYouScreen(
        state = state,
        itemActions = itemActions,
        buttonsActions = buttonsActions,
        selectType = { type -> viewModel.submit(ForYouAction.SelectForYouType(type)) },
        modifier = modifier
    )
}

@Composable
internal fun ForYouScreen(
    state: ForYouState,
    itemActions: ForYouItem.Actions,
    buttonsActions: ForYouButtons.Actions,
    selectType: (ForYouType) -> Unit,
    modifier: Modifier = Modifier,
    searchLikedItemScreen: @Composable (type: SearchLikedItemType) -> Unit = { SearchLikedItemScreen(it) }
) {
    Logger.withTag("ForYouScreen").d("State: $state")

    val verticalConstraintSet = ConstraintSet {
        val (typeSelectorRef, suggestionSourceRef, bodyRef, buttonsRef) = createRefsFor(
            ForYouScreen.LayoutId.TypeFilter,
            ForYouScreen.LayoutId.SuggestionSource,
            ForYouScreen.LayoutId.Body,
            ForYouScreen.LayoutId.Buttons
        )
        
        constrain(typeSelectorRef) {
            top.linkTo(parent.top)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        }
        constrain(suggestionSourceRef) {
            top.linkTo(typeSelectorRef.bottom)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        }
        constrain(bodyRef) {
            width = Dimension.fillToConstraints
            height = Dimension.fillToConstraints
            top.linkTo(suggestionSourceRef.bottom)
            bottom.linkTo(buttonsRef.top)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        }
        constrain(buttonsRef) {
            bottom.linkTo(parent.bottom)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        }
    }

    val horizontalConstraintSet = ConstraintSet {
        val (typeSelectorRef, bodyRef, buttonsRef) = createRefsFor(
            ForYouScreen.LayoutId.TypeFilter,
            ForYouScreen.LayoutId.Body,
            ForYouScreen.LayoutId.Buttons
        )
        constrain(bodyRef) {
            width = Dimension.fillToConstraints
            height = Dimension.fillToConstraints
            start.linkTo(parent.start)
            end.linkTo(typeSelectorRef.start)
            top.linkTo(parent.top)
            bottom.linkTo(parent.bottom)
        }
        constrain(typeSelectorRef) {
            top.linkTo(parent.top, margin = Dimens.Margin.Small)
            start.linkTo(bodyRef.end)
            end.linkTo(parent.end)
        }
        constrain(buttonsRef) {
            start.linkTo(bodyRef.end)
            bottom.linkTo(parent.bottom, margin = Dimens.Margin.Small)
            end.linkTo(parent.end)
        }
    }

    Adaptive { windowSizeClass ->
        val mode = ForYouScreen.Mode.forClass(windowSizeClass)

        ConstraintLayout(
            modifier = modifier
                .testTag(TestTag.ForYou)
                .fillMaxSize()
                .padding(Dimens.Margin.Small),
            constraintSet = when (mode) {
                is ForYouScreen.Mode.Vertical -> verticalConstraintSet
                ForYouScreen.Mode.Horizontal -> horizontalConstraintSet
            }
        ) {

            ForYouTypeFilter(
                modifier = Modifier.layoutId(ForYouScreen.LayoutId.TypeFilter),
                type = state.type,
                onTypeChange = selectType
            )

            Text(
                modifier = Modifier.layoutId(ForYouScreen.LayoutId.SuggestionSource)
                    .padding(Dimens.Margin.Small),
                text = (state.suggestedItem as? ForYouState.SuggestedItem.Screenplay)
                    ?.screenplay
                    ?.suggestionSource
                    ?.let { string(it) }
                    ?: "",
                style = MaterialTheme.typography.labelLarge,
                textAlign = TextAlign.Center,
                maxLines = 2
            )

            Box(modifier = Modifier.layoutId(ForYouScreen.LayoutId.Body)) {
                when (val suggestedItem = state.suggestedItem) {
                    is ForYouState.SuggestedItem.Error -> ErrorScreen(text = suggestedItem.message)
                    ForYouState.SuggestedItem.Loading -> CenteredProgress()

                    ForYouState.SuggestedItem.NoSuggestedMovies ->
                        NoSuggestionsScreen(ForYouType.Movies, searchLikedItemScreen)

                    ForYouState.SuggestedItem.NoSuggestedTvShows ->
                        NoSuggestionsScreen(ForYouType.TvShows, searchLikedItemScreen)

                    is ForYouState.SuggestedItem.Screenplay -> ForYouItem(
                        model = suggestedItem.screenplay,
                        actions = itemActions
                    )
                }
            }

            ForYouButtons(
                modifier = Modifier.layoutId(ForYouScreen.LayoutId.Buttons),
                mode = mode,
                itemId = (state.suggestedItem as? ForYouState.SuggestedItem.Screenplay)?.screenplay?.tmdbScreenplayId
                    ?: TmdbScreenplayId.Movie(0),
                actions = buttonsActions
            )
        }
    }
}

@Composable
private fun NoSuggestionsScreen(
    type: ForYouType,
    searchLikedMovieScreen: @Composable (SearchLikedItemType) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Dimens.Margin.Small),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val textRes = when (type) {
            ForYouType.Movies -> string.suggestions_no_movie_suggestions
            ForYouType.TvShows -> string.suggestions_no_tv_show_suggestions
        }
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = textRes),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(Dimens.Margin.Medium))
        val searchLikedItemType = when (type) {
            ForYouType.Movies -> SearchLikedItemType.Movies
            ForYouType.TvShows -> SearchLikedItemType.TvShows
        }
        searchLikedMovieScreen(searchLikedItemType)
    }
}

object ForYouScreen {

    data class Actions(
        val login: () -> Unit,
        val toScreenplayDetails: (TmdbScreenplayId) -> Unit
    ) {

        companion object {

            val Empty = Actions(login = {}, toScreenplayDetails = {})
        }
    }

    sealed interface Mode {

        object Horizontal : Mode
        data class Vertical(val spacing: Dp) : Mode

        companion object {

            fun forClass(windowSizeClass: WindowSizeClass): Mode {
                return when (windowSizeClass.width) {
                    WindowWidthSizeClass.Compact -> Vertical(spacing = Dimens.Margin.Medium)
                    WindowWidthSizeClass.Medium -> Vertical(spacing = Dimens.Margin.Large)
                    WindowWidthSizeClass.Expanded -> when (windowSizeClass.height) {
                        WindowHeightSizeClass.Compact,
                        WindowHeightSizeClass.Medium -> Horizontal

                        WindowHeightSizeClass.Expanded -> Vertical(spacing = Dimens.Margin.XLarge)
                    }
                }
            }
        }
    }

    object LayoutId {

        const val TypeFilter = "Type filter"
        const val SuggestionSource = "Suggestion source"
        const val Body = "Body"
        const val Buttons = "Buttons"
    }
}

@Composable
@Preview(backgroundColor = 0xFFFFFFFF, showBackground = true)
@Preview(device = Devices.TABLET, backgroundColor = 0xFFFFFFFF, showBackground = true)
private fun ForYouScreenPreview(
    @PreviewParameter(ForYouScreenPreviewDataProvider::class) state: ForYouState
) {
    CineScoutTheme {
        ForYouScreen(
            state = state,
            itemActions = ForYouItem.Actions.Empty,
            buttonsActions = ForYouButtons.Actions.Empty,
            selectType = {},
            searchLikedItemScreen = {}
        )
    }
}
