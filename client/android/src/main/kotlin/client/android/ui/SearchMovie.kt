package client.android.ui

import androidx.compose.foundation.Icon
import androidx.compose.foundation.Text
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.onActive
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.ExperimentalFocus
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focusRequester
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import client.Screen
import client.ViewState
import client.android.Get
import client.android.theme.default
import client.android.ui.list.MovieList
import client.android.util.ThemedPreview
import client.android.widget.ErrorScreen
import client.android.widget.LoadingScreen
import client.resource.Strings
import client.viewModel.SearchViewModel
import co.touchlab.kermit.Logger
import domain.Test.Movie.AmericanGangster
import domain.Test.Movie.Inception
import entities.movies.Movie
import util.exhaustive

// TODO: trick for save the state, deal with it properly
private var lastQuery = mutableStateOf("")

@Composable
@OptIn(ExperimentalFocus::class)
fun SearchMovie(
    buildViewModel: Get<SearchViewModel>,
    toSuggestions: () -> Unit,
    toWatchlist: () -> Unit,
    toMovieDetails: (Movie) -> Unit,
    logger: Logger
) {

    val (query, onQueryChange) = remember { lastQuery }

    val focusRequester = remember { FocusRequester() }
    onActive {
        focusRequester.requestFocus()
    }

    HomeScaffold(
        currentScreen = Screen.Search,
        topBar = {
            SearchBar(
                query = query,
                onQueryChange = onQueryChange,
                onQueryReset = { onQueryChange("") },
                focusRequester = focusRequester
            )
        },
        toSearch = {},
        toSuggestions = toSuggestions,
        toWatchlist = toWatchlist
    ) {

        val scope = rememberCoroutineScope()
        val viewModel = remember { buildViewModel(scope) }
        val state by viewModel.result.collectAsState()

        viewModel.search(query)

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
                    val throwable = viewState.error.throwable
                    logger.e(throwable?.message ?: "Error", "SearchMovie", throwable)
                    ErrorScreen(throwable?.message)
                }
            }.exhaustive
        }
    }

}

@Composable
@OptIn(ExperimentalFocus::class)
private fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onQueryReset: () -> Unit,
    focusRequester: FocusRequester
) {
    TopBar(Modifier.height(88.dp)) {
        TextField(
            modifier = Modifier
                .padding(vertical = 8.dp, horizontal = 32.dp)
                .fillMaxWidth()
                .align(Alignment.CenterVertically)
                .focusRequester(focusRequester),
            value = query,
            onValueChange = onQueryChange,
            label = { Text(text = Strings.InsertMovieTitleHint) },
            trailingIcon = {
                Icon(
                    modifier = Modifier.clickable(onClick = onQueryReset),
                    asset = Icons.default.Clear
                )
            },
            imeAction = ImeAction.Search,
            onImeActionPerformed = { _, controller -> controller?.hideSoftwareKeyboard() }
        )
    }
}

@Composable
@Preview
private fun MoviesListPreview() {
    ThemedPreview {
        MovieList(movies = listOf(AmericanGangster, Inception), toMovieDetails = {})
    }
}

@Composable
@Preview("SearchBar with Inception query")
@OptIn(ExperimentalFocus::class)
private fun SearchBarPreview() {
    ThemedPreview {
        SearchBar(query = "Inception", onQueryChange = {}, onQueryReset = {}, focusRequester = FocusRequester())
    }
}
