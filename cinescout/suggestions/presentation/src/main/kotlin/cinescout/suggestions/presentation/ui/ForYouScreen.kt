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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
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
import cinescout.suggestions.presentation.model.ForYouMovieUiModel
import cinescout.suggestions.presentation.model.ForYouState
import cinescout.suggestions.presentation.previewdata.ForYouMovieUiModelPreviewData
import cinescout.suggestions.presentation.viewmodel.ForYouViewModel
import coil.compose.AsyncImage
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import studio.forface.cinescout.design.R.string
import kotlin.math.roundToInt

@Composable
fun ForYouScreen(modifier: Modifier = Modifier) {
    val viewModel: ForYouViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateLifecycleAware()
    ForYouScreen(state = state, modifier = modifier)
}

@Composable
fun ForYouScreen(state: ForYouState, modifier: Modifier = Modifier) {
    val context = LocalContext.current

    Box(
        modifier = modifier
            .testTag(TestTag.ForYou)
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when (val suggestedMovie = state.suggestedMovie) {
            is ForYouState.SuggestedMovie.Data -> MovieItem(
                model = suggestedMovie.movie,
                openMovie = { movieId -> openMovieExternally(context, movieId) }
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
private fun MovieItem(model: ForYouMovieUiModel, openMovie: (TmdbMovieId) -> Unit) {
    val scope = rememberCoroutineScope()
    val xOffset = remember { Animatable(0f) }
    val draggableState = rememberDraggableState(onDelta = { delta ->
        scope.launch { xOffset.snapTo(xOffset.value + delta) }
    })

    Card(
        modifier = Modifier
            .draggable(
                draggableState,
                orientation = Orientation.Horizontal,
                onDragStopped = {
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
            backdrop = {
                AsyncImage(
                    model = model.backdropUrl,
                    contentDescription = NoContentDescription,
                    contentScale = ContentScale.Crop
                )
            },
            poster = {
                AsyncImage(
                    model = model.posterUrl,
                    modifier = Modifier.clip(MaterialTheme.shapes.medium),
                    contentDescription = NoContentDescription
                )
            },
            infoBox = {
                Column(
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.45f),
                            shape = MaterialTheme.shapes.medium
                        )
                        .padding(Dimens.Margin.Small)
                ) {
                    Text(text = model.title, maxLines = 2, style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.height(Dimens.Margin.Small))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = model.releaseYear, style = MaterialTheme.typography.labelMedium)
                        Row(modifier = Modifier.padding(top = Dimens.Margin.Small, end = Dimens.Margin.Small)) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = NoContentDescription
                            )
                            Spacer(modifier = Modifier.width(Dimens.Margin.Small))
                            Text(text = model.rating, style = MaterialTheme.typography.labelLarge)
                        }
                    }
                }
            },
            actors = {
                LazyRow {
                    items(model.actors) { actor ->
                        AsyncImage(
                            model = actor.profileImageUrl,
                            modifier = Modifier
                                .padding(Dimens.Margin.XSmall)
                                .size(Dimens.Icon.Large)
                                .clip(CircleShape),
                            contentDescription = NoContentDescription,
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            },
            buttons = {
                TextButton(onClick = { openMovie(model.tmdbMovieId) }) {
                    Text(text = stringResource(id = string.suggestions_for_you_open_details))
                }
            }
        )
    }
}

@Composable
private fun MovieLayout(
    backdrop: @Composable () -> Unit,
    poster: @Composable () -> Unit,
    infoBox: @Composable () -> Unit,
    actors: @Composable () -> Unit,
    buttons: @Composable RowScope.() -> Unit
) {
    val spacing = Dimens.Margin.Medium
    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (backdropRef, posterRef, infoBoxRef, actorsRef, buttonsRef) = createRefs()

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
            modifier = Modifier.constrainAs(actorsRef) {
                top.linkTo(infoBoxRef.bottom, margin = spacing)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        ) { actors() }

        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.fillMaxWidth().constrainAs(buttonsRef) {
                bottom.linkTo(parent.bottom, margin = spacing)
                start.linkTo(parent.start)
                end.linkTo(parent.end, margin = spacing)
            }
        ) { buttons() }
    }
}

@Composable
@Preview(showBackground = true)
fun MovieItemPreview() {
    CineScoutTheme {
        MovieItem(model = ForYouMovieUiModelPreviewData.Inception, openMovie = {})
    }
}
