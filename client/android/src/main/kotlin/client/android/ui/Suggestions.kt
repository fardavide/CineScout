package client.android.ui

import androidx.compose.animation.animate
import androidx.compose.foundation.Image
import androidx.compose.foundation.Text
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.gesture.scrollorientationlocking.Orientation
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import client.Screen
import client.ViewState
import client.android.Get
import client.android.util.blend
import client.android.widget.CenteredText
import client.resource.Strings
import client.viewModel.GetSuggestedMovieViewModel
import client.viewModel.GetSuggestedMovieViewModel.Error
import co.touchlab.kermit.Logger
import design.Color
import entities.movies.Movie
import entities.util.exhaustive
import entities.util.percent
import studio.forface.cinescout.R

@Composable
fun Suggestions(buildViewModel: Get<GetSuggestedMovieViewModel>, toSearch: () -> Unit, logger: Logger) {

    HomeScaffold(
        currentScreen = Screen.Suggestions,
        topBar = { TitleTopBar(title = Strings.SuggestionsAction) },
        toSearch = toSearch,
        toSuggestions = {},
        content = {

            val scope = rememberCoroutineScope()
            val viewModel = remember { buildViewModel(scope) }
            val state by viewModel.result.collectAsState()

            Column(
                Modifier.fillMaxSize().padding(16.dp),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalGravity = Alignment.CenterHorizontally
            ) {

                @Suppress("UnnecessaryVariable") // Needed for smart cast
                when (val viewState = state) {

                    is ViewState.None -> { }
                    is ViewState.Success -> Suggestion(movie = viewState.data)
                    is ViewState.Loading -> Loading()
                    is ViewState.Error -> {
                        when (val error = viewState.error) {
                            is Error.NoRatedMovies -> NoRatedMovies(toSearch)
                            is Error.Unknown -> {
                                val throwable = error.throwable
                                logger.e(throwable.message ?: "Error", "Suggestions", throwable)
                                GenericError(throwable.message)
                            }
                        }
                    }

                }.exhaustive
            }
        }
    )
}

@Composable
private fun Suggestion(movie: Movie) {

    var x by remember { mutableStateOf(0.dp) }
    val backgroundColor = run {
        val base = MaterialTheme.colors.surface
        val balance = (x.value / 4).coerceAtLeast(-100f).coerceAtMost(100f)
        when {
            balance > 0f -> base.blend(Color.CambridgeBlue, balance.percent)
            balance < 0f -> base.blend(Color.Bittersweet, (-balance).percent)
            else -> base
        }
    }

    Card(Modifier
        .fillMaxWidth()
        .fillMaxHeight(0.5f)
        .offset(x = animate(x))
        .draggable(
            Orientation.Horizontal,
            onDrag = { velocity -> x += velocity.toDp() },
            onDragStopped = { x = 0.dp }
        ),
        backgroundColor = backgroundColor,
        elevation = 4.dp
    ) {
        CenteredText(text = movie.name.s, style = MaterialTheme.typography.h4)
    }
}

@Composable
private fun Loading() {

    CenteredText(text = Strings.LoadingMessage, style = MaterialTheme.typography.h4)
}

@Composable
private fun NoRatedMovies(toSearch: () -> Unit) {

    Image(asset = vectorResource(id = R.drawable.ic_problem_color))
    CenteredText(text = Strings.NoRateMoviesError, style = MaterialTheme.typography.h4)
    CenteredText(text = Strings.SearchMovieAndRateForSuggestions, style = MaterialTheme.typography.h5)
    OutlinedButton(onClick = toSearch) {
        Text(text = Strings.GoToSearchAction)
    }
}

@Composable
private fun GenericError(message: String? = null) {

    Image(asset = vectorResource(id = R.drawable.ic_problem_color))
    CenteredText(text = message ?: Strings.GenericError, style = MaterialTheme.typography.h4)
}
