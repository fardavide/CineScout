package client.android.ui

import androidx.compose.foundation.Box
import androidx.compose.foundation.Icon
import androidx.compose.foundation.Image
import androidx.compose.foundation.Text
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.ExtendedFloatingActionButton
import androidx.compose.material.FabPosition
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
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
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import client.android.Get
import client.android.GetWithId
import client.android.theme.default
import client.android.widget.CenteredText
import client.resource.Strings
import client.viewModel.MovieDetailsViewModel
import dev.chrisbanes.accompanist.coil.CoilImageWithCrossfade
import entities.Poster
import entities.TmdbId
import studio.forface.cinescout.R

@Composable
fun MovieDetails(buildViewModel: GetWithId<MovieDetailsViewModel>, movieId: TmdbId, onBack: () -> Unit) {

    val scope = rememberCoroutineScope()
    val viewModel = remember(movieId) {
        buildViewModel(scope, movieId)
    }

    val state by viewModel.result.collectAsState()
    var movieWithStats by remember(movieId) { mutableStateOf(state.data) }
    if (state.data != null) movieWithStats = state.data
    val movie = movieWithStats?.movie

    var showDialog by remember(movieId) {
        mutableStateOf(false)
    }

    if (showDialog) {
        RateDialog(
            onLike = {
                viewModel.like()
                showDialog = false
            },
            onDislike = {
                viewModel.dislike()
                showDialog = false
            },
            onDismiss = { showDialog = false }
        )
    }

    MainScaffold(
        topBar = { if (movie != null) TitleTopBar(movie.name.s) else Strings.LoadingMessage },
        bottomBar = {
            BottomBar {

                // Back button
                IconButton(onClick = onBack) { Icon(Icons.default.ArrowBack) }

                if (movieWithStats != null) {
                    val inWatchlist = movieWithStats!!.inWatchlist

                    val onClick =
                        if (inWatchlist) viewModel::removeFromWatchlist
                        else viewModel::addToWatchlist

                    val vectorId =
                        if (inWatchlist) R.drawable.ic_bookmark_color
                        else R.drawable.ic_bookmark_bw

                    // Bookmark button
                    IconButton(onClick) {
                        Image(
                            modifier = Modifier.padding(8.dp),
                            asset = vectorResource(id = vectorId)
                        )
                    }
                }

            }
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text(text = Strings.RateMovieAction) },
                onClick = { showDialog = true })
        },
        floatingActionButtonPosition = FabPosition.Center,
        isFloatingActionButtonDocked = true
    ) {

        Column(
            Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            if (movie != null) {
                MoviePoster(poster = movie.poster)
                MovieTitle(title = movie.name.s)
                MovieBody(
                    genres = movie.genres.joinToString { it.name.s },
                    actors = movie.actors.take(5).joinToString { it.name.s },
                    textStyle = MaterialTheme.typography.h6
                )
            }
        }
    }
}

@Composable private fun MoviePoster(poster: Poster?) {
    poster ?: return

    Box(Modifier.fillMaxHeight(0.3f).clip(MaterialTheme.shapes.medium)) {
        CoilImageWithCrossfade(data = poster.get(Poster.Size.Original))
    }
}

@Composable private fun MovieTitle(title: String) {

    CenteredText(text = title, style = MaterialTheme.typography.h4)
}

@Composable
fun MovieBody(genres: String, actors: String, textStyle: TextStyle) {

    Row {
        Column {

            Text(text = Strings.GenresTitle)
            Text(style = textStyle, text = genres)
        }
    }

    Row {
        Column {

            Text(text = Strings.ActorsTitle)
            Text(style = textStyle, textAlign = TextAlign.Center, text = actors)
        }
    }
}

@Composable
private fun RateDialog(
    onLike: () -> Unit,
    onDislike: () -> Unit,
    onDismiss: () -> Unit
) {

    Dialog(onDismissRequest = onDismiss) {

        Column(
            Modifier.background(MaterialTheme.colors.surface, MaterialTheme.shapes.medium).padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Text(style = MaterialTheme.typography.h5, text = Strings.RateMoviePrompt)

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                TextButton(onClick = onDislike) {
                    Text(text = Strings.DislikeAction)
                }
                Spacer(Modifier.size(8.dp))
                Button(onClick = onLike) {
                    Text(text = Strings.LikeAction)
                }
            }
        }
    }
}

