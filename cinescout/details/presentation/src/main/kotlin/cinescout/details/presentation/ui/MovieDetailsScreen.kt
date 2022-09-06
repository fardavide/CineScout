package cinescout.details.presentation.ui

import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import cinescout.design.TestTag
import cinescout.design.TextRes
import cinescout.design.string
import cinescout.design.theme.CineScoutTheme
import cinescout.design.util.collectAsStateLifecycleAware
import cinescout.details.presentation.model.MovieDetailsState
import cinescout.details.presentation.previewdata.MovieDetailsScreenPreviewDataProvider
import cinescout.details.presentation.viewmodel.MovieDetailsViewModel
import cinescout.movies.domain.model.TmdbMovieId
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import studio.forface.cinescout.design.R.string

@Composable
fun MovieDetailsScreen(movieId: TmdbMovieId, actions: MovieDetailsScreen.Actions, modifier: Modifier = Modifier) {
    val viewModel: MovieDetailsViewModel = koinViewModel(parameters = { parametersOf(movieId) })
    val state by viewModel.state.collectAsStateLifecycleAware()
    MovieDetailsScreen(state = state, actions = actions, modifier = modifier)
}

@Composable
fun MovieDetailsScreen(state: MovieDetailsState, actions: MovieDetailsScreen.Actions, modifier: Modifier = Modifier) {
    val movieTitle = when (state) {
        is MovieDetailsState.Loading,
        is MovieDetailsState.Error -> ""
        is MovieDetailsState.Data -> state.movieDetails.title
    }
    Scaffold(
        modifier = modifier
            .testTag(TestTag.MovieDetails)
            .statusBarsPadding()
            .navigationBarsPadding(),
        bottomBar = { MovieDetailsBottomBar(actions.onBack) },
        topBar = { MovieDetailsTopBar(movieTitle) }
    ) { paddingValues ->
        Surface(modifier = Modifier.padding(paddingValues)) {
            MovieDetailsContent(state = state)
        }
    }
}

@Composable
fun MovieDetailsContent(state: MovieDetailsState) {
    val text = when (state) {
        is MovieDetailsState.Data -> TextRes(state.movieDetails.title)
        is MovieDetailsState.Error -> state.message
        MovieDetailsState.Loading -> TextRes("Loading")
    }
    Text(text = string(textRes = text))
}

@Composable
private fun MovieDetailsBottomBar(onBack: () -> Unit) {
    BottomAppBar(actions = {
        IconButton(onClick = onBack) {
            Icon(
                imageVector = Icons.Rounded.ArrowBack,
                contentDescription = stringResource(id = string.back_button_description)
            )
        }
    })
}

@Composable
private fun MovieDetailsTopBar(movieTitle: String) {
    CenterAlignedTopAppBar(
        title = {
            Text(text = movieTitle)
        }
    )
}

object MovieDetailsScreen {

    const val MovieIdKey = "movie_id"

    data class Actions(
        val onBack: () -> Unit
    ) {

        companion object {

            val Empty = Actions(onBack = {})
        }
    }
}

@Composable
@Preview
@Preview(device = Devices.TABLET)
private fun MovieDetailsScreenPreview(
    @PreviewParameter(MovieDetailsScreenPreviewDataProvider::class) state: MovieDetailsState
) {
    CineScoutTheme {
        MovieDetailsScreen(state = state, actions = MovieDetailsScreen.Actions.Empty)
    }
}
