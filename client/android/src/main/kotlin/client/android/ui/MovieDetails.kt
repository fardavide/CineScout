package client.android.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Icon
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.Text
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayout
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.preferredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRowFor
import androidx.compose.material.Button
import androidx.compose.material.Divider
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
import androidx.compose.ui.Layout
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.ContextAmbient
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import client.android.GetWithId
import client.android.theme.default
import client.android.widget.CenteredText
import client.android.widget.MovieTitle
import client.resource.Strings
import client.viewModel.MovieDetailsViewModel
import dev.chrisbanes.accompanist.coil.CoilImageWithCrossfade
import entities.TmdbId
import entities.model.Actor
import entities.model.Genre
import entities.model.TmdbImageUrl
import entities.model.UserRating
import entities.model.Video
import entities.movies.Movie
import studio.forface.cinescout.R


const val WatchlistButtonTestTag = "WatchlistButton test tag"
const val InWatchlistTestTag = "In watchlist"
const val NotInWatchlistTestTag = "Not in watchlist"

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

    val rating = movieWithStats?.rating ?: UserRating.Neutral

    var showDialog by remember(movieId) {
        mutableStateOf(false)
    }

    if (showDialog) {
        RateDialog(
            current = rating,
            onLike = {
                viewModel.like()
                showDialog = false
            },
            onDislike = {
                viewModel.dislike()
                showDialog = false
            },
            onRemoveRating = {
                viewModel.removeRating()
                showDialog = false
            },
            onDismiss = { showDialog = false }
        )
    }

    MainScaffold(
        bottomBar = {
            BottomBar(
                mainIcon = Icons.default.ArrowBack,
                onMainClick = onBack
            ) {

                if (movieWithStats != null) {
                    val inWatchlist = movieWithStats!!.inWatchlist

                    val onClick =
                        if (inWatchlist) viewModel::removeFromWatchlist
                        else viewModel::addToWatchlist

                    // Watchlist button
                    IconButton(
                        modifier = Modifier.testTag(WatchlistButtonTestTag),
                        onClick = onClick
                    ) {
                        if (inWatchlist) {
                            Image(
                                modifier = Modifier.testTag(InWatchlistTestTag),
                                asset = vectorResource(id = R.drawable.ic_bookmark_color)
                            )
                        } else {
                            Icon(
                                modifier = Modifier.testTag(NotInWatchlistTestTag),
                                asset = vectorResource(id = R.drawable.ic_bookmark_bw)
                            )
                        }
                    }
                }

            }
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = {
                    Row {
                        Text(text = Strings.RateMovieAction)

                        val modifier = Modifier.size(24.dp).padding(start = 4.dp, bottom = 4.dp)
                        when (rating) {
                            UserRating.Positive -> Icon(
                                modifier = modifier,
                                asset = vectorResource(id = R.drawable.ic_like_bw)
                            )
                            UserRating.Neutral -> { /* none */
                            }
                            UserRating.Negative -> Icon(
                                modifier = modifier,
                                asset = vectorResource(id = R.drawable.ic_dislike_bw)
                            )
                        }
                    }
                },
                onClick = { showDialog = true })
        },
        floatingActionButtonPosition = FabPosition.Center,
        isFloatingActionButtonDocked = true,
        autoWrap = false
    ) { paddingValues ->

        if (movie != null) {
            ScrollableColumn {
                Content(movie, paddingValues)
            }
        }
    }
}

@Composable
@OptIn(ExperimentalLayout::class)
private fun Content(movie: Movie, innerPadding: PaddingValues) {

    Column(
        Modifier.padding(innerPadding).padding(bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Header(movie)

            Column(Modifier.padding(horizontal = 16.dp)) {
                MovieBody(
                    genres = movie.genres,
                    actors = movie.actors.take(5),
                    textStyle = MaterialTheme.typography.h6
                )
                Overview(content = movie.overview)
            }
        }

        // Videos
        // TODO deal with different sites
        LazyRowFor(
            items = movie.videos.filter { it.site == Video.Site.YouTube },
            contentPadding = PaddingValues(16.dp)
        ) {
            val context = ContextAmbient.current
            Column(Modifier.clickable(onClick = { openYoutube(context, it.url) })) {
                Text(modifier = Modifier.preferredWidth(IntrinsicSize.Max), text = it.title.s)
                Spacer(Modifier.height(8.dp))
                Box(Modifier.clip(MaterialTheme.shapes.small)) {
                    CoilImageWithCrossfade(data = "https://img.youtube.com/vi/${it.key}/0.jpg")
                }
            }
        }
    }
}

private fun openYoutube(context: Context, url: String) {
    val intent = Intent(Intent.ACTION_VIEW).apply {
        data = Uri.parse(url)
        setPackage("com.google.android.youtube")
    }
    context.startActivity(intent)
}

@Composable
private fun Header(movie: Movie) {

    Layout(children = {
        MovieBackdrop(backdrop = movie.backdrop)
        MoviePoster(poster = movie.poster)
        MovieTitle(movie, MaterialTheme.typography.h4, MaterialTheme.typography.body1, centered = false)
    }, measureBlock = { measurables, constraints ->

        val placeables = measurables.map { measurable ->
            measurable.measure(constraints)
        }
        val (backdrop, poster, title) = placeables
        val totalHeight = backdrop.height + poster.height / 2

        val padding = 16.dp.toIntPx()
        layout(constraints.maxWidth, totalHeight) {

            backdrop.place(Offset.Zero)
            poster.place(padding, backdrop.height - poster.height / 2)
            title.place(poster.width + padding * 3, backdrop.height + padding)
//            title.place(0, backdrop.height)
        }
    })
}

@Composable
private fun MovieBackdrop(backdrop: TmdbImageUrl?) {
    backdrop ?: return Divider()

    Box(modifier = Modifier.fillMaxWidth()) {
        CoilImageWithCrossfade(data = backdrop.get(TmdbImageUrl.Size.Original))
    }
}

@Composable
private fun MoviePoster(poster: TmdbImageUrl?) {
    poster ?: return Divider()

    Box(Modifier.fillMaxWidth(0.25f).clip(MaterialTheme.shapes.medium)) {
        CoilImageWithCrossfade(data = poster.get(TmdbImageUrl.Size.Original))
    }
}

@Composable
fun MovieBody(genres: Collection<Genre>, actors: Collection<Actor>, textStyle: TextStyle) {

    Row {
        Column {

            Text(text = Strings.GenresTitle)
            Text(style = textStyle, text = genres.joinToString { it.name.s })
        }
    }

    Row {
        Column {

            Text(text = Strings.ActorsTitle)
            Text(style = textStyle, textAlign = TextAlign.Center, text = actors.joinToString { it.name.s })
        }
    }
}

@Composable fun Overview(content: String) {
    CenteredText(text = content)
}

@Composable
private fun RateDialog(
    current: UserRating,
    onLike: () -> Unit,
    onDislike: () -> Unit,
    onRemoveRating: () -> Unit,
    onDismiss: () -> Unit
) {

    Dialog(onDismissRequest = onDismiss) {

        Column(
            Modifier.background(MaterialTheme.colors.surface, MaterialTheme.shapes.medium).padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            when (current) {
                UserRating.Positive -> {
                    Text(style = MaterialTheme.typography.h5, text = Strings.LikedMoviesMessage)
                    Text(style = MaterialTheme.typography.subtitle1, text = Strings.ChangeRatingPrompt)
                }
                UserRating.Neutral -> {
                    Text(style = MaterialTheme.typography.h5, text = Strings.RateMoviePrompt)
                }
                UserRating.Negative -> {
                    Text(style = MaterialTheme.typography.h5, text = Strings.DislikedMoviesMessage)
                    Text(style = MaterialTheme.typography.subtitle1, text = Strings.ChangeRatingPrompt)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {

                    if (current != UserRating.Neutral)
                        TextButton(onClick = onRemoveRating) {
                            Text(text = Strings.RemoveRatingAction)
                        }

                    if (current != UserRating.Negative)
                        TextButton(onClick = onDislike) {
                            Text(text = Strings.DislikeAction)
                        }

                    if (current != UserRating.Positive)
                        Button(onClick = onLike) {
                            Text(text = Strings.LikeAction)
                        }
                }
            }
        }
    }
}

