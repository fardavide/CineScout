package client.android.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import client.ViewState
import client.android.Get
import client.android.widget.CenteredText
import client.resource.Strings
import client.viewModel.GetSuggestedMovieViewModel
import client.viewModel.GetSuggestedMovieViewModel.Error
import co.touchlab.kermit.Logger
import entities.movies.Movie
import entities.util.exhaustive
import studio.forface.cinescout.R

@Composable
fun Suggestions(buildViewModel: Get<GetSuggestedMovieViewModel>, toSearch: () -> Unit, logger: Logger) {

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

            is ViewState.None -> {}
            is ViewState.Success -> Suggestion(movie = viewState.data)
            is ViewState.Loading -> Loading()
            is ViewState.Error -> {
                when(val error = viewState.error) {
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

@Composable
private fun Suggestion(movie: Movie) {

    CenteredText(style = MaterialTheme.typography.h4, text = movie.name.s)
}

@Composable
private fun Loading() {

    CenteredText(style = MaterialTheme.typography.h4, text = Strings.LoadingMessage)
}

@Composable
private fun NoRatedMovies(toSearch: () -> Unit) {

    Image(asset = vectorResource(id = R.drawable.ic_problem_color))
    CenteredText(style = MaterialTheme.typography.h4, text = Strings.NoRateMoviesError)
    CenteredText(style = MaterialTheme.typography.h5, text = Strings.SearchMovieAndRateForSuggestions)
    OutlinedButton(onClick = toSearch) {
        Text(text = Strings.GoToSearchAction)
    }
}

@Composable
private fun GenericError(message: String? = null) {

    Image(asset = vectorResource(id = R.drawable.ic_problem_color))
    CenteredText(style = MaterialTheme.typography.h4, text = message ?: Strings.GenericError)
}
