package cinescout.suggestions.presentation.ui

import androidx.compose.foundation.layout.Arrangement
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
import cinescout.design.TestTag
import cinescout.design.theme.CineScoutTheme
import cinescout.design.theme.Dimens
import cinescout.design.ui.CenteredProgress
import cinescout.design.ui.ErrorScreen
import cinescout.design.util.collectAsStateLifecycleAware
import cinescout.movies.domain.model.TmdbMovieId
import cinescout.search.presentation.model.SearchLikedItemType
import cinescout.search.presentation.ui.SearchLikedItemScreen
import cinescout.suggestions.presentation.model.ForYouAction
import cinescout.suggestions.presentation.model.ForYouState
import cinescout.suggestions.presentation.model.ForYouType
import cinescout.suggestions.presentation.previewdata.ForYouScreenPreviewDataProvider
import cinescout.suggestions.presentation.viewmodel.ForYouViewModel
import cinescout.tvshows.domain.model.TmdbTvShowId
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

    val movieActions = ForYouMovieItem.Actions(
        addMovieToWatchlist = { movieId -> viewModel.submit(ForYouAction.AddToWatchlist(movieId)) },
        dislikeMovie = { movieId -> viewModel.submit(ForYouAction.Dislike(movieId)) },
        likeMovie = { movieId -> viewModel.submit(ForYouAction.Like(movieId)) },
        toMovieDetails = actions.toMovieDetails
    )
    val tvShowActions = ForYouTvShowItem.Actions(
        addTvShowToWatchlist = { tvShowId -> viewModel.submit(ForYouAction.AddToWatchlist(tvShowId)) },
        dislikeTvShow = { tvShowId -> viewModel.submit(ForYouAction.Dislike(tvShowId)) },
        likeTvShow = { tvShowId -> viewModel.submit(ForYouAction.Like(tvShowId)) },
        toTvShowDetails = actions.toTvShowDetails
    )

    ForYouScreen(
        state = state,
        actions = actions,
        movieActions = movieActions,
        tvShowActions = tvShowActions,
        selectType = { type -> viewModel.submit(ForYouAction.SelectForYouType(type)) },
        modifier = modifier
    )
}

@Composable
internal fun ForYouScreen(
    state: ForYouState,
    actions: ForYouScreen.Actions,
    movieActions: ForYouMovieItem.Actions,
    tvShowActions: ForYouTvShowItem.Actions,
    selectType: (ForYouType) -> Unit,
    modifier: Modifier = Modifier,
    searchLikedItemScreen: @Composable (type: SearchLikedItemType) -> Unit = { SearchLikedItemScreen(it) }
) {
    Logger.withTag("ForYouScreen").d("State: $state")

    if (state.shouldShowHint) {
        actions.toForYouHint()
    }

    Adaptive { windowSizeClass ->
        Column(
            modifier = modifier
                .testTag(TestTag.ForYou)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ForYouTypeSelector(
                type = state.type,
                onTypeSelected = selectType
            )
            when (val suggestedItem = state.suggestedItem) {
                is ForYouState.SuggestedItem.Error -> ErrorScreen(text = suggestedItem.message)
                ForYouState.SuggestedItem.Loading -> CenteredProgress()
                is ForYouState.SuggestedItem.Movie -> ForYouMovieItem(
                    model = suggestedItem.movie,
                    actions = movieActions
                )
                ForYouState.SuggestedItem.NoSuggestedMovies ->
                    NoSuggestionsScreen(ForYouType.Movies, searchLikedItemScreen)
                ForYouState.SuggestedItem.NoSuggestedTvShows ->
                    NoSuggestionsScreen(ForYouType.TvShows, searchLikedItemScreen)
                is ForYouState.SuggestedItem.TvShow -> ForYouTvShowItem(
                    model = suggestedItem.tvShow,
                    actions = tvShowActions
                )
            }
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
        val toForYouHint: () -> Unit,
        val toMovieDetails: (TmdbMovieId) -> Unit,
        val toTvShowDetails: (TmdbTvShowId) -> Unit
    ) {

        companion object {

            val Empty = Actions(login = {}, toMovieDetails = {}, toForYouHint = {}, toTvShowDetails = {})
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
            movieActions = ForYouMovieItem.Actions.Empty,
            tvShowActions = ForYouTvShowItem.Actions.Empty,
            selectType = {}
        )
    }
}
