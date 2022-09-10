package cinescout.suggestions.presentation.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import cinescout.design.TestTag
import cinescout.design.TextRes
import cinescout.design.theme.CineScoutTheme
import cinescout.design.theme.Dimens
import cinescout.design.ui.CenteredErrorText
import cinescout.design.ui.CenteredProgress
import cinescout.design.ui.ErrorScreen
import cinescout.design.ui.ErrorText
import cinescout.design.util.NoContentDescription
import cinescout.design.util.collectAsStateLifecycleAware
import cinescout.movies.domain.model.TmdbMovieId
import cinescout.suggestions.presentation.model.ForYouAction
import cinescout.suggestions.presentation.model.ForYouState
import cinescout.suggestions.presentation.previewdata.ForYouScreenPreviewDataProvider
import cinescout.suggestions.presentation.viewmodel.ForYouViewModel
import org.koin.androidx.compose.koinViewModel
import studio.forface.cinescout.design.R
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
        dismissHint = { viewModel.submit(ForYouAction.DismissHint) },
        itemActions = itemActions,
        modifier = modifier
    )
}

@Composable
internal fun ForYouScreen(
    state: ForYouState,
    actions: ForYouScreen.Actions,
    dismissHint: () -> Unit,
    itemActions: ForYouMovieItem.Actions,
    modifier: Modifier = Modifier
) {

    Box(
        modifier = modifier
            .testTag(TestTag.ForYou)
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when (state.shouldShowHint) {
            true -> ForYouHintScreen(dismiss = dismissHint)
            false -> {
                when (val suggestedMovie = state.suggestedMovie) {
                    is ForYouState.SuggestedMovie.Data -> ForYouMovieItem(
                        model = suggestedMovie.movie,
                        actions = itemActions
                    )
                    is ForYouState.SuggestedMovie.Error -> ErrorScreen(text = suggestedMovie.message)
                    ForYouState.SuggestedMovie.Loading -> CenteredProgress()
                    ForYouState.SuggestedMovie.NoSuggestions -> when (state.loggedIn) {
                        ForYouState.LoggedIn.False -> NotLoggedInScreen(actions.login)
                        ForYouState.LoggedIn.Loading -> CenteredProgress()
                        ForYouState.LoggedIn.True ->
                            CenteredErrorText(text = TextRes(string.suggestions_no_suggestions))
                    }
                }
            }
        }
    }
}

@Composable
private fun NotLoggedInScreen(login: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Dimens.Margin.XLarge, alignment = Alignment.CenterVertically)
    ) {
        BoxWithConstraints {
            Image(
                modifier = Modifier
                    .size(minOf(maxWidth / 2, maxHeight / 3))
                    .aspectRatio(1f),
                painter = painterResource(id = R.drawable.img_error),
                contentDescription = NoContentDescription
            )
        }
        ErrorText(text = TextRes(string.suggestions_for_you_not_logged_in))
        OutlinedButton(onClick = login) {
            Text(text = stringResource(id = string.suggestions_for_you_login))
        }
    }
}

object ForYouScreen {

    data class Actions(
        val login: () -> Unit,
        val toMovieDetails: (TmdbMovieId) -> Unit
    ) {

        companion object {

            val Empty = Actions(login = {}, toMovieDetails = {})
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
            dismissHint = {},
            itemActions = ForYouMovieItem.Actions.Empty
        )
    }
}
