package client.android.ui

import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import client.Screen
import client.ViewState
import client.android.Get
import client.android.ui.list.MovieList
import client.android.widget.CenteredText
import client.android.widget.ErrorMessage
import client.android.widget.ErrorScreen
import client.android.widget.LoadingScreen
import client.android.widget.MessageScreen
import client.resource.Strings
import client.viewModel.WatchlistViewModel
import co.touchlab.kermit.Logger
import entities.movies.Movie
import util.exhaustive

@Composable
fun Watchlist(
    buildViewModel: Get<WatchlistViewModel>,
    toSearch: () -> Unit,
    toSuggestions: () -> Unit,
    toMovieDetails: (Movie) -> Unit,
    logger: Logger
) {

    HomeScaffold(
        currentScreen = Screen.Watchlist,
        topBar = { TitleTopBar(title = Strings.WatchlistAction) },
        toSearch = toSearch,
        toSuggestions = toSuggestions,
        toWatchlist = {}
    ) {

        val scope = rememberCoroutineScope()
        val viewModel = remember { buildViewModel(scope) }
        val state by viewModel.result.collectAsState()

        Column(
            Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            @Suppress("UnnecessaryVariable") // Needed for smart cast
            when (val viewState = state) {

                is ViewState.None -> {}
                is ViewState.Success -> MovieList(movies = viewState.data.toList(), toMovieDetails = toMovieDetails)
                is ViewState.Loading -> LoadingScreen()
                is ViewState.Error -> {
                    when (val error = viewState.error) {
                        is WatchlistViewModel.Error.NoMovies -> NoWatchlistMovies(toSuggestions)
                        is WatchlistViewModel.Error.Unknown -> {
                            val throwable = error.throwable
                            logger.e(throwable.message ?: "Error", "Suggestions", throwable)
                            ErrorScreen(throwable.message)
                        }
                    }
                }
            }.exhaustive
        }
    }
}

@Composable
private fun NoWatchlistMovies(toSuggestions: () -> Unit) {

    MessageScreen {
        ErrorMessage(message = Strings.NoWatchlistMoviesError)
        CenteredText(text = Strings.GetSuggestionsAndAddToWatchlist, style = MaterialTheme.typography.h5)
        OutlinedButton(onClick = toSuggestions) {
            Text(text = Strings.GoToSuggestionsAction)
        }
    }
}
