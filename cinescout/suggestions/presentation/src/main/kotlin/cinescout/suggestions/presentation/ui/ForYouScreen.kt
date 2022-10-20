package cinescout.suggestions.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import cinescout.design.TestTag
import cinescout.design.theme.CineScoutTheme
import cinescout.design.theme.Dimens
import cinescout.design.ui.CenteredProgress
import cinescout.design.ui.ErrorScreen
import cinescout.design.util.collectAsStateLifecycleAware
import cinescout.movies.domain.model.TmdbMovieId
import cinescout.search.presentation.ui.SearchLikedMovieScreen
import cinescout.suggestions.presentation.model.ForYouAction
import cinescout.suggestions.presentation.model.ForYouState
import cinescout.suggestions.presentation.model.ForYouType
import cinescout.suggestions.presentation.previewdata.ForYouScreenPreviewDataProvider
import cinescout.suggestions.presentation.viewmodel.ForYouViewModel
import cinescout.utils.compose.Adaptive
import cinescout.utils.compose.WindowHeightSizeClass
import cinescout.utils.compose.WindowSizeClass
import cinescout.utils.compose.WindowWidthSizeClass
import co.touchlab.kermit.Logger
import org.koin.androidx.compose.koinViewModel
import studio.forface.cinescout.design.R.string

@Composable
fun ForYouScreen(actions: ForYouScreen.Actions, modifier: Modifier = Modifier) {
    val viewModel: ForYouViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateLifecycleAware()

    val itemActions = ForYouMovieItem.Actions(
        addMovieToWatchlist = { movieId -> viewModel.submit(ForYouAction.AddToWatchlist(movieId)) },
        dislikeMovie = { movieId -> viewModel.submit(ForYouAction.Dislike(movieId)) },
        likeMovie = { movieId -> viewModel.submit(ForYouAction.Like(movieId)) },
        toMovieDetails = actions.toMovieDetails
    )

    ForYouScreen(
        state = state,
        actions = actions,
        itemActions = itemActions,
        modifier = modifier
    )
}

@Composable
internal fun ForYouScreen(
    state: ForYouState,
    actions: ForYouScreen.Actions,
    itemActions: ForYouMovieItem.Actions,
    modifier: Modifier = Modifier,
    searchLikedMovieScreen: @Composable () -> Unit = { SearchLikedMovieScreen() }
) {
    Logger.withTag("ForYouScreen").d("State: $state")

    if (state.shouldShowHint) {
        actions.toForYouHint()
    }

    Adaptive { windowSizeClass ->
        val mode = ForYouScreen.Mode.forClass(windowSizeClass)
        @Composable
        fun Content() {
            when (val suggestedMovie = state.suggestedMovie) {
                is ForYouState.SuggestedMovie.Data -> ForYouMovieItem(
                    model = suggestedMovie.movie,
                    actions = itemActions
                )
                is ForYouState.SuggestedMovie.Error -> ErrorScreen(text = suggestedMovie.message)
                ForYouState.SuggestedMovie.Loading -> CenteredProgress()
                ForYouState.SuggestedMovie.NoSuggestions -> NoSuggestionsScreen(searchLikedMovieScreen)
            }
        }
        when (mode) {
            is ForYouScreen.Mode.Vertical -> Column(
                modifier = modifier
                    .testTag(TestTag.ForYou)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ForYouTypeSelector(
                    type = ForYouType.Movies,
                    onTypeSelected = {}
                )
                Content()
            }
            ForYouScreen.Mode.Horizontal -> Row(
                modifier = modifier
                    .testTag(TestTag.ForYou)
                    .fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ForYouTypeSelector(
                    modifier = Modifier.rotate(270f),
                    type = ForYouType.Movies,
                    onTypeSelected = {}
                )
                Content()
            }
        }
    }
}

@Composable
private fun NoSuggestionsScreen(searchLikedMovieScreen: @Composable () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Dimens.Margin.Small),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = string.suggestions_no_suggestions),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(Dimens.Margin.Medium))
        searchLikedMovieScreen()
    }
}

object ForYouScreen {

    data class Actions(
        val login: () -> Unit,
        val toForYouHint: () -> Unit,
        val toMovieDetails: (TmdbMovieId) -> Unit
    ) {

        companion object {

            val Empty = Actions(login = {}, toMovieDetails = {}, toForYouHint = {})
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
private fun ForYouScreenPreview(
    @PreviewParameter(ForYouScreenPreviewDataProvider::class) state: ForYouState
) {
    CineScoutTheme {
        ForYouScreen(
            state = state,
            actions = ForYouScreen.Actions.Empty,
            itemActions = ForYouMovieItem.Actions.Empty
        )
    }
}
