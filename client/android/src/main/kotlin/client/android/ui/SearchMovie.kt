package client.android.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.onCommit
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import client.ViewState
import client.android.Get
import client.android.widget.CenteredText
import client.resource.Strings
import client.viewModel.SearchViewModel
import co.touchlab.kermit.Logger
import entities.movies.Movie
import entities.util.exhaustive

@Composable
fun SearchMovie(buildViewModel: Get<SearchViewModel>, query: String, logger: Logger) {

    val scope = rememberCoroutineScope()
    val viewModel = remember { buildViewModel(scope) }
    val state by viewModel.result.collectAsState()

    onCommit(query) {
        viewModel.search(query)
    }

    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalGravity = Alignment.CenterHorizontally
    ) {

        @Suppress("UnnecessaryVariable") // Needed for smart cast
        when (val viewState = state) {
            is ViewState.None -> {}
            is ViewState.Success -> MovieList(movies = viewState.data)
            is ViewState.Loading -> Loading()
            is ViewState.Error -> {
                val throwable = viewState.error.throwable
                logger.e(throwable?.message ?: "Error", "SearchMovie", throwable)
                GenericError(throwable?.message)
            }
        }.exhaustive
    }
}

@Composable
private fun MovieList(movies: Collection<Movie>) {

    for (movie in movies)
        CenteredText(style = MaterialTheme.typography.h4, text = movie.name.s)
}

@Composable
private fun Loading() {

    CenteredText(style = MaterialTheme.typography.h4, text = Strings.LoadingMessage)
}


@Composable
private fun GenericError(message: String? = null) {

    CenteredText(style = MaterialTheme.typography.h4, text = message ?: Strings.GenericError)
}

