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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import cinescout.design.theme.CineScoutTheme
import cinescout.design.theme.Dimens
import cinescout.design.ui.CenteredProgress
import cinescout.design.ui.ErrorScreen
import cinescout.design.util.collectAsStateLifecycleAware
import cinescout.design.util.visibleIf
import cinescout.resources.R.string
import cinescout.resources.string
import cinescout.screenplay.domain.model.ScreenplayIds
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

    Adaptive { windowSizeClass ->
        val verticalSpacing = when (windowSizeClass.height) {
            WindowHeightSizeClass.Compact -> 0.dp
            WindowHeightSizeClass.Medium -> Dimens.Margin.Small
            WindowHeightSizeClass.Expanded -> Dimens.Margin.Medium
        }
        ForYouScreen(
            modifier = modifier,
            state = state,
            verticalSpacing = verticalSpacing,
            itemActions = itemActions,
            buttonsActions = buttonsActions,
            selectType = { type -> viewModel.submit(ForYouAction.SelectForYouType(type)) }
        )
    }
}

@Composable
internal fun ForYouScreen(
    state: ForYouState,
    verticalSpacing: Dp,
    itemActions: ForYouItem.Actions,
    buttonsActions: ForYouButtons.Actions,
    selectType: (ForYouType) -> Unit,
    modifier: Modifier = Modifier,
    searchLikedItemScreen: @Composable (type: SearchLikedItemType) -> Unit = {
        SearchLikedItemScreen(it)
    }
) {
    Logger.withTag("ForYouScreen").d("State: $state")

    ConstraintLayout(
        modifier = modifier
            .padding(verticalSpacing)
            .fillMaxSize()
    ) {
        val (
            typeFilter,
            suggestionSource,
            content,
            buttons
        ) = createRefs()

        ForYouTypeFilter(
            modifier = Modifier.constrainAs(typeFilter) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },
            type = state.type,
            onTypeChange = selectType
        )

        Text(
            modifier = Modifier.constrainAs(suggestionSource) {
                top.linkTo(typeFilter.bottom, margin = verticalSpacing)
                bottom.linkTo(content.top, margin = verticalSpacing)
                verticalBias = 1f
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },
            text = (state.suggestedItem as? ForYouState.SuggestedItem.Screenplay)
                ?.screenplay
                ?.suggestionSource
                ?.let { string(it) }
                ?: "",
            style = MaterialTheme.typography.labelLarge,
            textAlign = TextAlign.Center,
            maxLines = 2
        )

        Box(
            modifier = Modifier.constrainAs(content) {
                height = Dimension.fillToConstraints
                top.linkTo(suggestionSource.bottom, margin = verticalSpacing)
                bottom.linkTo(buttons.top, margin = verticalSpacing)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        ) {
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
            modifier = Modifier.constrainAs(buttons) {
                visibleIf(state.suggestedItem is ForYouState.SuggestedItem.Screenplay)
                top.linkTo(content.bottom, margin = verticalSpacing)
                bottom.linkTo(parent.bottom)
                verticalBias = 1f
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                horizontalBias = 0.8f
            },
            itemId = (state.suggestedItem as? ForYouState.SuggestedItem.Screenplay)
                ?.screenplay
                ?.screenplayIds
                ?.tmdb
                ?: TmdbScreenplayId.Movie(0),
            actions = buttonsActions
        )
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
        val toScreenplayDetails: (ScreenplayIds) -> Unit
    ) {

        companion object {

            val Empty = Actions(login = {}, toScreenplayDetails = {})
        }
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
            verticalSpacing = Dimens.Margin.Small,
            selectType = {},
            searchLikedItemScreen = {}
        )
    }
}
