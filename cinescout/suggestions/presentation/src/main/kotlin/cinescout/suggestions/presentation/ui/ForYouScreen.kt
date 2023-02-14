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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import cinescout.design.R.string
import cinescout.design.TestTag
import cinescout.design.theme.CineScoutTheme
import cinescout.design.theme.Dimens
import cinescout.design.ui.CenteredProgress
import cinescout.design.ui.ErrorScreen
import cinescout.design.util.collectAsStateLifecycleAware
import cinescout.movies.domain.model.TmdbMovieId
import cinescout.screenplay.domain.model.TmdbScreenplayId
import cinescout.search.presentation.model.SearchLikedItemType
import cinescout.search.presentation.ui.SearchLikedItemScreen
import cinescout.suggestions.presentation.model.ForYouAction
import cinescout.suggestions.presentation.model.ForYouState
import cinescout.suggestions.presentation.model.ForYouType
import cinescout.suggestions.presentation.previewdata.ForYouScreenPreviewDataProvider
import cinescout.suggestions.presentation.viewmodel.ForYouViewModel
import cinescout.tvshows.domain.model.TmdbTvShowId
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
        toDetails = { itemId ->
            when (itemId) {
                is TmdbScreenplayId.Movie -> actions.toMovieDetails(itemId)
                is TmdbScreenplayId.TvShow -> actions.toTvShowDetails(itemId)
            }
        }
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

    ConstraintLayout(
        modifier = modifier
            .testTag(TestTag.ForYou)
            .fillMaxSize()
    ) {
        val (typeSelectorRef, bodyRef, buttonsRef) = createRefs()

        ForYouTypeSelector(
            modifier = Modifier.constrainAs(typeSelectorRef) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },
            type = state.type,
            onTypeSelected = selectType
        )

        Box(
            modifier = Modifier.constrainAs(bodyRef) {
                width = Dimension.fillToConstraints
                height = Dimension.fillToConstraints
                top.linkTo(typeSelectorRef.bottom)
                bottom.linkTo(buttonsRef.top)
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

        if (state.suggestedItem is ForYouState.SuggestedItem.Screenplay) {
            ForYouButtons(
                modifier = Modifier.constrainAs(buttonsRef) {
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
                itemId = state.suggestedItem.screenplay.tmdbScreenplayId,
                actions = buttonsActions
            )
        }
    }
}

@Composable
private fun NoSuggestionsScreen(type: ForYouType, searchLikedMovieScreen: @Composable (SearchLikedItemType) -> Unit) {
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
        val toMovieDetails: (TmdbMovieId) -> Unit,
        val toTvShowDetails: (TmdbTvShowId) -> Unit
    ) {

        companion object {

            val Empty = Actions(login = {}, toMovieDetails = {}, toTvShowDetails = {})
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
}

@Composable
@Preview(showBackground = true)
@Preview(showSystemUi = true, device = Devices.TABLET)
private fun ForYouScreenPreview(@PreviewParameter(ForYouScreenPreviewDataProvider::class) state: ForYouState) {
    CineScoutTheme {
        ForYouScreen(
            state = state,
            itemActions = ForYouItem.Actions.Empty,
            buttonsActions = ForYouButtons.Actions.Empty,
            selectType = {}
        )
    }
}
