package cinescout.suggestions.presentation.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import cinescout.design.TestTag
import cinescout.design.TextRes
import cinescout.design.theme.CineScoutTheme
import cinescout.design.theme.Dimens
import cinescout.design.ui.CenteredErrorText
import cinescout.design.ui.CenteredProgress
import cinescout.design.util.NoContentDescription
import cinescout.design.util.collectAsStateLifecycleAware
import cinescout.movies.domain.model.TmdbMovieId
import cinescout.suggestions.presentation.model.ForYouAction
import cinescout.suggestions.presentation.model.ForYouMovieUiModel
import cinescout.suggestions.presentation.model.ForYouState
import cinescout.suggestions.presentation.previewdata.ForYouMovieUiModelPreviewData
import cinescout.suggestions.presentation.viewmodel.ForYouViewModel
import coil.compose.AsyncImage
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import studio.forface.cinescout.design.R.drawable
import studio.forface.cinescout.design.R.string
import kotlin.math.absoluteValue
import kotlin.math.roundToInt

@Composable
fun ForYouScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val viewModel: ForYouViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateLifecycleAware()

    val actions = MovieItem.Actions(
        addMovieToWatchlist = { movieId -> viewModel.submit(ForYouAction.AddToWatchlist(movieId)) },
        dislikeMovie = { movieId -> viewModel.submit(ForYouAction.Dislike(movieId)) },
        likeMovie = { movieId -> viewModel.submit(ForYouAction.Like(movieId)) },
        openMovie = { movieId -> openMovieExternally(context, movieId) }
    )

    ForYouScreen(state = state, actions = actions, modifier = modifier)
}

@Composable
fun ForYouScreen(state: ForYouState, actions: MovieItem.Actions, modifier: Modifier = Modifier) {

    Box(
        modifier = modifier
            .testTag(TestTag.ForYou)
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when (val suggestedMovie = state.suggestedMovie) {
            is ForYouState.SuggestedMovie.Data -> MovieItem(
                model = suggestedMovie.movie,
                actions = actions
            )
            is ForYouState.SuggestedMovie.Error -> CenteredErrorText(text = suggestedMovie.message)
            ForYouState.SuggestedMovie.Loading -> CenteredProgress()
            ForYouState.SuggestedMovie.NoSuggestions ->
                CenteredErrorText(text = TextRes(string.suggestions_no_suggestions))
        }
    }
}

private fun openMovieExternally(context: Context, movieId: TmdbMovieId) {
    val uri = Uri.parse("http://www.themoviedb.org/movie/${movieId.value}")
    val intent = Intent(Intent.ACTION_VIEW, uri)
    context.startActivity(intent)
}

@Composable
private fun MovieItem(model: ForYouMovieUiModel, actions: MovieItem.Actions) {
    val scope = rememberCoroutineScope()
    val xOffset = remember(model.tmdbMovieId.value) { Animatable(0f) }
    val draggableState = rememberDraggableState(onDelta = { delta ->
        scope.launch { xOffset.snapTo(xOffset.value + delta) }
    })

    Card(
        modifier = Modifier
            .draggable(
                draggableState,
                orientation = Orientation.Horizontal,
                onDragStopped = {
                    when {
                        xOffset.value > MovieItem.DragThreshold -> actions.likeMovie(model.tmdbMovieId)
                        xOffset.value < -MovieItem.DragThreshold -> actions.dislikeMovie(model.tmdbMovieId)
                    }
                    xOffset.animateTo(
                        targetValue = 0f,
                        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
                    )
                }
            )
            .rotate(xOffset.value / 50)
            .offset { IntOffset(x = xOffset.value.roundToInt(), y = 0) }
            .padding(Dimens.Margin.Small)
            .fillMaxHeight()
    ) {
        MovieLayout(
            backdrop = { Backdrop(model.backdropUrl) },
            poster = { Poster(model.posterUrl) },
            infoBox = { InfoBox(model.title, model.releaseYear, model.rating) },
            genres = { Genres(model.genres) },
            actors = { Actors(model.actors) },
            buttons = { Buttons(actions, model.tmdbMovieId) },
            overlay = { Overlay(xOffset.value) }
        )
    }
}

@Composable
private fun Backdrop(url: String?) {
    AsyncImage(
        modifier = Modifier.imageBackground(),
        model = url,
        contentDescription = NoContentDescription,
        contentScale = ContentScale.Crop,
        error = painterResource(id = drawable.ic_warning_30)
    )
}

@Composable
private fun Poster(url: String?) {
    AsyncImage(
        modifier = Modifier
            .clip(MaterialTheme.shapes.medium)
            .imageBackground(),
        model = url,
        contentDescription = NoContentDescription,
        error = painterResource(id = drawable.ic_warning_30)
    )
}

@Composable
private fun InfoBox(title: String, releaseYear: String, rating: String) {
    Column(
        modifier = Modifier
            .background(
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.45f),
                shape = MaterialTheme.shapes.medium
            )
            .padding(Dimens.Margin.Small)
    ) {
        Text(text = title, maxLines = 2, style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(Dimens.Margin.Small))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = releaseYear, style = MaterialTheme.typography.labelMedium)
            Row(modifier = Modifier.padding(top = Dimens.Margin.Small, end = Dimens.Margin.Small)) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = NoContentDescription
                )
                Spacer(modifier = Modifier.width(Dimens.Margin.Small))
                Text(text = rating, style = MaterialTheme.typography.labelLarge)
            }
        }
    }
}

@Composable
private fun Genres(genres: List<String>) {
    LazyRow {
        items(genres) { genre ->
            Text(
                modifier = Modifier
                    .padding(Dimens.Margin.XXSmall)
                    .background(
                        color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.35f),
                        shape = MaterialTheme.shapes.small
                    )
                    .padding(Dimens.Margin.Small),
                text = genre,
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}

@Composable
private fun Actors(actors: List<ForYouMovieUiModel.Actor>) {
    LazyRow {
        items(actors) { actor ->
            AsyncImage(
                model = actor.profileImageUrl,
                modifier = Modifier
                    .padding(Dimens.Margin.XSmall)
                    .size(Dimens.Icon.Large)
                    .clip(CircleShape)
                    .imageBackground(),
                contentDescription = NoContentDescription,
                contentScale = ContentScale.Crop,
                error = painterResource(id = drawable.ic_warning_30)
            )
        }
    }
}

@Composable
private fun Buttons(
    actions: MovieItem.Actions,
    movieId: TmdbMovieId
) {
    TextButton(onClick = { actions.addMovieToWatchlist(movieId) }) {
        Text(text = stringResource(id = string.suggestions_for_you_add_watchlist))
    }
    TextButton(onClick = { actions.openMovie(movieId) }) {
        Text(text = stringResource(id = string.suggestions_for_you_open_details))
    }
}

@Composable
private fun Overlay(xOffset: Float) {
    val color = when {
        xOffset > 0 -> MaterialTheme.colorScheme.primary
        xOffset < 0 -> MaterialTheme.colorScheme.error
        else -> Color.Transparent
    }
    val alpha = xOffset.absoluteValue / MovieItem.DragThreshold / 2
    Box(
        Modifier
            .fillMaxSize()
            .background(color = color.copy(alpha = alpha.coerceAtMost(1f)))
    )
}

@Composable
private fun MovieLayout(
    backdrop: @Composable () -> Unit,
    poster: @Composable () -> Unit,
    infoBox: @Composable () -> Unit,
    genres: @Composable () -> Unit,
    actors: @Composable () -> Unit,
    buttons: @Composable RowScope.() -> Unit,
    overlay: @Composable () -> Unit
) {
    val spacing = Dimens.Margin.Medium
    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (backdropRef, posterRef, infoBoxRef, genresRef, actorsRef, buttonsRef, overlayRef) = createRefs()

        Box(
            modifier = Modifier.constrainAs(backdropRef) {
                height = Dimension.percent(0.35f)
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        ) { backdrop() }

        Box(
            modifier = Modifier.constrainAs(posterRef) {
                width = Dimension.percent(0.3f)
                height = Dimension.ratio("1:1.5")
                top.linkTo(backdropRef.bottom)
                bottom.linkTo(backdropRef.bottom)
                start.linkTo(parent.start, margin = spacing)
            }
        ) { poster() }

        Box(
            modifier = Modifier.constrainAs(infoBoxRef) {
                width = Dimension.fillToConstraints
                top.linkTo(backdropRef.bottom, margin = spacing)
                start.linkTo(posterRef.end, margin = spacing)
                end.linkTo(parent.end, margin = spacing)
            }
        ) { infoBox() }

        Box(
            modifier = Modifier.constrainAs(genresRef) {
                top.linkTo(infoBoxRef.bottom, margin = spacing)
                start.linkTo(parent.start)
                bottom.linkTo(actorsRef.top)
                end.linkTo(parent.end)
            }
        ) { genres() }

        Box(
            modifier = Modifier.constrainAs(actorsRef) {
                top.linkTo(genresRef.bottom, margin = spacing)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        ) { actors() }

        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(buttonsRef) {
                    bottom.linkTo(parent.bottom, margin = spacing)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end, margin = spacing)
                }
        ) { buttons() }

        Box(
            modifier = Modifier.constrainAs(overlayRef) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)
            }
        ) { overlay() }
    }
}

@Composable
private fun Modifier.imageBackground() =
    background(color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.1f))

object MovieItem {

    data class Actions(
        val addMovieToWatchlist: (TmdbMovieId) -> Unit,
        val dislikeMovie: (TmdbMovieId) -> Unit,
        val likeMovie: (TmdbMovieId) -> Unit,
        val openMovie: (TmdbMovieId) -> Unit
    ) {

        companion object {

            val Empty = Actions(
                addMovieToWatchlist = {},
                dislikeMovie = {},
                likeMovie = {},
                openMovie = {}
            )
        }
    }

    const val DragThreshold = 300
}

@Composable
@Preview(showBackground = true)
fun MovieItemPreview() {
    CineScoutTheme {
        MovieItem(model = ForYouMovieUiModelPreviewData.Inception, actions = MovieItem.Actions.Empty)
    }
}
