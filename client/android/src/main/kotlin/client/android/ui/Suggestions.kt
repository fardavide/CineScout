package client.android.ui

import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import client.viewModel.GetSuggestedMovieViewModel
import client.viewModel.GetSuggestedMovieViewModel.Error
import co.touchlab.kermit.Logger
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import client.ViewState
import client.android.widget.CenteredText
import client.resource.Strings
import entities.movies.Movie
import entities.util.exhaustive

@Composable
fun Suggestions(viewModel: GetSuggestedMovieViewModel, toSearch: () -> Unit, logger: Logger) {

    val state by viewModel.result.collectAsState()

    Column(
        Modifier.fillMaxSize(),
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

    CenteredText(style = MaterialTheme.typography.h4, text = Strings.NoRateMoviesError)
    CenteredText(style = MaterialTheme.typography.h5, text = Strings.SearchMovieAndRateForSuggestions)
    OutlinedButton(onClick = toSearch) {
        Text(text = Strings.GoToSearchAction)
    }
}

@Composable
private fun GenericError(message: String? = null) {

    CenteredText(style = MaterialTheme.typography.h4, text = message ?: Strings.GenericError)
}
