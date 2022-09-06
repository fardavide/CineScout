package cinescout.suggestions.presentation.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import cinescout.design.TestTag
import cinescout.design.TextRes
import cinescout.design.theme.CineScoutTheme
import cinescout.design.ui.CenteredErrorText
import cinescout.design.ui.CenteredProgress
import cinescout.design.ui.ErrorScreen
import cinescout.design.util.collectAsStateLifecycleAware
import cinescout.movies.domain.model.TmdbMovieId
import cinescout.suggestions.presentation.model.ForYouAction
import cinescout.suggestions.presentation.model.ForYouState
import cinescout.suggestions.presentation.previewdata.ForYouScreenPreviewDataProvider
import cinescout.suggestions.presentation.viewmodel.ForYouViewModel
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

    ForYouScreen(state = state, actions = itemActions, modifier = modifier)
}

@Composable
internal fun ForYouScreen(state: ForYouState, actions: ForYouMovieItem.Actions, modifier: Modifier = Modifier) {

    Box(
        modifier = modifier
            .testTag(TestTag.ForYou)
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when (val suggestedMovie = state.suggestedMovie) {
            is ForYouState.SuggestedMovie.Data -> ForYouMovieItem(
                model = suggestedMovie.movie,
                actions = actions
            )
            is ForYouState.SuggestedMovie.Error -> ErrorScreen(text = suggestedMovie.message)
            ForYouState.SuggestedMovie.Loading -> CenteredProgress()
            ForYouState.SuggestedMovie.NoSuggestions ->
                CenteredErrorText(text = TextRes(string.suggestions_no_suggestions))
        }
    }
}

object ForYouScreen {

    data class Actions(
        val toMovieDetails: (TmdbMovieId) -> Unit
    ) {

        companion object {

            val Empty = Actions(toMovieDetails = {})
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
            actions = ForYouMovieItem.Actions.Empty
        )
    }
}
