package cinescout.suggestions.presentation.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
fun ForYouScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val viewModel: ForYouViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateLifecycleAware()

    val actions = ForYouMovieItem.Actions(
        addMovieToWatchlist = { movieId -> viewModel.submit(ForYouAction.AddToWatchlist(movieId)) },
        dislikeMovie = { movieId -> viewModel.submit(ForYouAction.Dislike(movieId)) },
        likeMovie = { movieId -> viewModel.submit(ForYouAction.Like(movieId)) },
        openMovie = { movieId -> openMovieExternally(context, movieId) }
    )

    ForYouScreen(state = state, actions = actions, modifier = modifier)
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

private fun openMovieExternally(context: Context, movieId: TmdbMovieId) {
    val uri = Uri.parse("http://www.themoviedb.org/movie/${movieId.value}")
    val intent = Intent(Intent.ACTION_VIEW, uri)
    context.startActivity(intent)
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
