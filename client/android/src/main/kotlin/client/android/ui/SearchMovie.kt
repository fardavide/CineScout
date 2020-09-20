package client.android.ui

import androidx.compose.foundation.Box
import androidx.compose.foundation.Icon
import androidx.compose.foundation.Text
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.InnerPadding
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope.align
import androidx.compose.foundation.layout.RowScope.gravity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.material.MaterialTheme
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
import androidx.compose.ui.draw.clip
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
import client.android.util.ThemedPreview
import client.android.widget.CenteredText
import client.resource.Strings
import client.viewModel.SearchViewModel
import co.touchlab.kermit.Logger
import dev.chrisbanes.accompanist.coil.CoilImage
import domain.Test.Movie.AmericanGangster
import domain.Test.Movie.Inception
import entities.Poster
import entities.movies.Movie
import util.exhaustive

// TODO: trick for save the state, deal with it properly
private var lastQuery = mutableStateOf("")

@Composable
@OptIn(ExperimentalFocus::class)
fun SearchMovie(
    buildViewModel: Get<SearchViewModel>,
    toSuggestions: () -> Unit,
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
        topBar = { SearchBar(
            query = query,
            onQueryChange = onQueryChange,
            onQueryReset = { onQueryChange("") },
            focusRequester = focusRequester
        ) },
        toSearch = {},
        toSuggestions = toSuggestions,
        content = {

            val scope = rememberCoroutineScope()
            val viewModel = remember { buildViewModel(scope) }
            val state by viewModel.result.collectAsState()

            viewModel.search(query)

            Column(
                Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                @Suppress("UnnecessaryVariable") // Needed for smart cast
                when (val viewState = state) {

                    is ViewState.None -> {
                    }
                    is ViewState.Success -> MovieList(movies = viewState.data.toList(), toMovieDetails = toMovieDetails)
                    is ViewState.Loading -> Loading()
                    is ViewState.Error -> {
                        val throwable = viewState.error.throwable
                        logger.e(throwable?.message ?: "Error", "SearchMovie", throwable)
                        GenericError(throwable?.message)
                    }
                }.exhaustive
            }
        }
    )
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
private fun MovieList(movies: List<Movie>, toMovieDetails: (Movie) -> Unit) {

    LazyColumnFor(contentPadding = InnerPadding(top = 8.dp, bottom = 8.dp), items = movies) { movie ->
        MovieItem(movie = movie, toMovieDetails = toMovieDetails)
    }
}

@Composable
private fun MovieItem(movie: Movie, toMovieDetails: (Movie) -> Unit) {

    Box(Modifier.padding(horizontal = 32.dp)) {

        Row(
            Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .clip(MaterialTheme.shapes.large)
                .clickable(onClick = { toMovieDetails(movie) })
        ) {

            Poster(poster = movie.poster)
            Column(Modifier.padding(start = 16.dp)) {

                CenteredText(text = movie.name.s, style = MaterialTheme.typography.h6)
                MovieBody(
                    genres = movie.genres.joinToString { it.name.s },
                    actors = movie.actors.take(3).joinToString { it.name.s },
                    textStyle = MaterialTheme.typography.subtitle1
                )
            }
        }

        Divider()
    }
}

@Composable
private fun Poster(poster: Poster?) {

    CoilImage(
        modifier = Modifier.height(156.dp).clip(MaterialTheme.shapes.small),
        data = poster?.get(Poster.Size.W500) ?: "",
    )
}

@Composable
private fun Divider() {
    Row(Modifier
        .fillMaxWidth()
        .height(1.dp)
        .background(MaterialTheme.colors.onBackground.copy(alpha = 0.1f))
    ) {}
}

@Composable
private fun Loading() {

    CenteredText(text = Strings.LoadingMessage, style = MaterialTheme.typography.h4)
}


@Composable
private fun GenericError(message: String? = null) {

    CenteredText(text = message ?: Strings.GenericError, style = MaterialTheme.typography.h4)
}

@Composable
@Preview
private fun MoviesListPreview() {
    ThemedPreview() {
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
