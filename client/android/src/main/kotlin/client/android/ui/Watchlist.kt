package client.android.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import client.Screen
import client.android.Get
import client.android.ui.list.MovieList
import client.android.widget.CenteredText
import client.android.widget.ErrorMessage
import client.android.widget.LoadingScreen
import client.android.widget.MessageScreen
import client.resource.Strings
import client.viewModel.WatchlistViewModel
import co.touchlab.kermit.Logger
import domain.stats.GetMoviesInWatchlist
import entities.Either
import entities.movies.Movie
import org.koin.core.Koin
import util.exhaustive

@Composable
fun Watchlist(
    koin: Koin,
    buildViewModel: Get<WatchlistViewModel>,
    toSearch: () -> Unit,
    toSuggestions: () -> Unit,
    toMovieDetails: (Movie) -> Unit,
    logger: Logger
) {

    HomeScaffold(
        koin,
        currentScreen = Screen.Watchlist,
        topBar = { TitleTopBar(title = Strings.WatchlistAction) },
        toSearch = toSearch,
        toSuggestions = toSuggestions,
        toWatchlist = {}
    ) {

        val scope = rememberCoroutineScope()
        val viewModel = remember { buildViewModel(scope) }
        val result by viewModel.result.collectAsState()

        Column(
            Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            @Suppress("UnnecessaryVariable") // Needed for smart cast
            when (val viewState = result) {

                is Either.Left -> when (result.leftOrThrow()) {
                    is GetMoviesInWatchlist.Error.Unknown -> logger.e(result.toString(), "Watchlist")
                    is GetMoviesInWatchlist.Error.NoMovies -> NoWatchlistMovies(toSuggestions)
                }

                is Either.Right -> when (val state = result.rightOrThrow()) {
                    is GetMoviesInWatchlist.State.Loading -> LoadingScreen()
                    is GetMoviesInWatchlist.State.Success -> MovieList(
                        movies = state.movies.toList(),
                        toMovieDetails = toMovieDetails
                    )
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
