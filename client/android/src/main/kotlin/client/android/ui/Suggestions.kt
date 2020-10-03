package client.android.ui

import androidx.compose.animation.animate
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.Image
import androidx.compose.foundation.Text
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.IconButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.gesture.scrollorientationlocking.Orientation
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import client.Screen
import client.ViewState
import client.android.Get
import client.android.util.blend
import client.android.widget.CenteredText
import client.android.widget.ErrorMessage
import client.android.widget.ErrorScreen
import client.android.widget.LoadingScreen
import client.android.widget.MessageScreen
import client.resource.Strings
import client.viewModel.GetSuggestedMovieViewModel
import client.viewModel.GetSuggestedMovieViewModel.Error
import co.touchlab.kermit.Logger
import design.Color
import dev.chrisbanes.accompanist.coil.CoilImageWithCrossfade
import entities.Poster
import entities.movies.Movie
import studio.forface.cinescout.R
import util.exhaustive
import util.percent

@Composable
fun Suggestions(
    buildViewModel: Get<GetSuggestedMovieViewModel>,
    toMovieDetails: (Movie) -> Unit,
    toSearch: () -> Unit,
    toWatchlist: () -> Unit,
    logger: Logger
) {

    HomeScaffold(
        currentScreen = Screen.Suggestions,
        topBar = { TitleTopBar(title = Strings.SuggestionsAction) },
        toSearch = toSearch,
        toSuggestions = {},
        toWatchlist = toWatchlist,
        content = {

            val scope = rememberCoroutineScope()
            val viewModel = remember { buildViewModel(scope) }
            val state by viewModel.result.collectAsState()

            Column(
                Modifier.fillMaxSize().padding(16.dp),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                @Suppress("UnnecessaryVariable") // Needed for smart cast
                when (val viewState = state) {

                    is ViewState.None -> { }
                    is ViewState.Success -> Suggestion(
                        movie = viewState.data,
                        toMovieDetails = toMovieDetails,
                        onLike = viewModel::likeCurrent,
                        onDislike = viewModel::dislikeCurrent,
                        onSkip = viewModel::skipCurrent,
                        onAddToWatchlist = viewModel::addCurrentToWatchlist
                    )
                    is ViewState.Loading -> LoadingScreen()
                    is ViewState.Error -> {
                        when (val error = viewState.error) {
                            is Error.NoRatedMovies -> NoRatedMovies(toSearch)
                            is Error.Unknown -> {
                                val throwable = error.throwable
                                logger.e(throwable.message ?: "Error", "Suggestions", throwable)
                                ErrorScreen(throwable.message)
                            }
                        }
                    }

                }.exhaustive
            }
        }
    )
}

@Composable
private fun Suggestion(
    movie: Movie,
    toMovieDetails: (Movie) -> Unit,
    onLike: () -> Unit,
    onDislike: () -> Unit,
    onSkip: () -> Unit,
    onAddToWatchlist: () -> Unit
) {

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
            onDragStopped = {
                when {
                    x > 200.dp -> onLike()
                    x < (-200).dp -> onDislike()
                }
                x = 0.dp
            }
        )
        .clickable(onClick = { toMovieDetails(movie) }),
        backgroundColor = backgroundColor,
        elevation = 4.dp
    ) {
        Row {
            Poster(movie.poster)

            Column(Modifier.padding(horizontal = 16.dp)) {

                // Bookmark button
                IconButton(modifier = Modifier.align(Alignment.End), onClick = onAddToWatchlist) {
                    Image(asset = vectorResource(id = R.drawable.ic_bookmark_bw))
                }

                Column(Modifier.padding(vertical = 16.dp)) {
                    CenteredText(text = movie.name.s, style = MaterialTheme.typography.h5)
                    MovieBody(
                        genres = movie.genres.joinToString { it.name.s },
                        actors = movie.actors.take(4).joinToString { it.name.s },
                        textStyle = MaterialTheme.typography.subtitle1
                    )
                }
            }
        }
    }

    OutlinedButton(onClick = onSkip) {
        Text(text = Strings.SkipAction)
    }
}

@Composable
private fun Poster(poster: Poster?) {

    CoilImageWithCrossfade(
        modifier = Modifier.fillMaxHeight().aspectRatio(0.5f).clip(MaterialTheme.shapes.medium),
        contentScale = ContentScale.Crop,
        data = poster?.get(Poster.Size.W780) ?: "",
    )
}

@Composable
private fun NoRatedMovies(toSearch: () -> Unit) {

    MessageScreen {
        ErrorMessage(message = Strings.NoRateMoviesError)
        CenteredText(text = Strings.SearchMovieAndRateForSuggestions, style = MaterialTheme.typography.h5)
        OutlinedButton(onClick = toSearch) {
            Text(text = Strings.GoToSearchAction)
        }
    }
}
