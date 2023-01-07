package cinescout.suggestions.presentation.ui

import android.content.res.Configuration
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import cinescout.design.theme.CineScoutTheme
import cinescout.design.theme.Dimens
import cinescout.design.theme.imageBackground
import cinescout.design.util.NoContentDescription
import cinescout.suggestions.presentation.model.ForYouTvShowUiModel
import cinescout.suggestions.presentation.sample.ForYouTvShowUiModelSample
import cinescout.tvshows.domain.model.TmdbTvShowId
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.coil.CoilImage
import kotlinx.coroutines.launch
import studio.forface.cinescout.design.R.drawable
import studio.forface.cinescout.design.R.string
import kotlin.math.absoluteValue
import kotlin.math.roundToInt

@Composable
internal fun ForYouTvShowItem(
    model: ForYouTvShowUiModel,
    actions: ForYouTvShowItem.Actions,
    modifier: Modifier = Modifier,
    xOffset: Animatable<Float, AnimationVector1D> = remember(model.tmdbTvShowId.value) { Animatable(0f) }
) {
    val scope = rememberCoroutineScope()
    val draggableState = rememberDraggableState(onDelta = { delta ->
        scope.launch { xOffset.snapTo(xOffset.value + delta) }
    })

    Card(
        modifier = modifier
            .draggable(
                draggableState,
                orientation = Orientation.Horizontal,
                onDragStopped = {
                    when {
                        xOffset.value > ForYouTvShowItem.DragThreshold -> actions.likeTvShow(model.tmdbTvShowId)
                        xOffset.value < -ForYouTvShowItem.DragThreshold -> actions.dislikeTvShow(model.tmdbTvShowId)
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
    ) {
        ForYouTvShowItemLayout(
            backdrop = { Backdrop(model.backdropUrl) },
            poster = { Poster(model.posterUrl) },
            infoBox = { InfoBox(model.title, model.firstAirDate, model.rating) },
            genres = { Genres(model.genres) },
            actors = { Actors(model.actors) },
            buttons = { Buttons(actions, model.tmdbTvShowId) },
            overlay = { Overlay(xOffset.value) }
        )
    }
}

@Composable
private fun Backdrop(url: String?) {
    CoilImage(
        modifier = Modifier.imageBackground(),
        imageModel = { url },
        imageOptions = ImageOptions(contentScale = ContentScale.Crop),
        failure = {
            Image(
                painter = painterResource(id = drawable.ic_warning_30),
                contentDescription = NoContentDescription
            )
        },
        previewPlaceholder = drawable.img_backdrop
    )
}

@Composable
private fun Poster(url: String?) {
    CoilImage(
        modifier = Modifier
            .clip(MaterialTheme.shapes.medium)
            .imageBackground(),
        imageModel = { url },
        imageOptions = ImageOptions(contentScale = ContentScale.Inside),
        failure = {
            Image(
                painter = painterResource(id = drawable.ic_warning_30),
                contentDescription = NoContentDescription
            )
        },
        previewPlaceholder = drawable.img_poster
    )
}

@Composable
private fun InfoBox(title: String, releaseYear: String, rating: String) {
    Column(
        modifier = Modifier
            .background(
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.65f),
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
                        color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.55f),
                        shape = MaterialTheme.shapes.small
                    )
                    .padding(Dimens.Margin.Small),
                text = genre,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onTertiary
            )
        }
    }
}

@Composable
private fun Actors(actors: List<ForYouTvShowUiModel.Actor>) {
    LazyRow {
        items(actors) { actor ->
            CoilImage(
                modifier = Modifier
                    .padding(Dimens.Margin.XSmall)
                    .size(Dimens.Icon.Large)
                    .clip(CircleShape)
                    .imageBackground(),
                imageModel = { actor.profileImageUrl },
                imageOptions = ImageOptions(contentScale = ContentScale.Crop),
                failure = {
                    Image(
                        painter = painterResource(id = drawable.ic_warning_30),
                        contentDescription = NoContentDescription
                    )
                },
                previewPlaceholder = drawable.ic_user_color
            )
        }
    }
}

@Composable
private fun Buttons(
    actions: ForYouTvShowItem.Actions,
    tvShowId: TmdbTvShowId
) {
    Button(onClick = { actions.addTvShowToWatchlist(tvShowId) }) {
        Text(text = stringResource(id = string.suggestions_for_you_add_watchlist))
    }
    Spacer(modifier = Modifier.width(Dimens.Margin.Small))
    FilledTonalButton(onClick = { actions.toTvShowDetails(tvShowId) }) {
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
    val alpha = xOffset.absoluteValue / ForYouTvShowItem.DragThreshold / 2
    Box(
        Modifier
            .fillMaxSize()
            .background(color = color.copy(alpha = alpha.coerceAtMost(1f)))
    )
}

internal object ForYouTvShowItem {

    data class Actions(
        val addTvShowToWatchlist: (TmdbTvShowId) -> Unit,
        val dislikeTvShow: (TmdbTvShowId) -> Unit,
        val likeTvShow: (TmdbTvShowId) -> Unit,
        val toTvShowDetails: (TmdbTvShowId) -> Unit
    ) {

        companion object {

            val Empty = Actions(
                addTvShowToWatchlist = {},
                dislikeTvShow = {},
                likeTvShow = {},
                toTvShowDetails = {}
            )
        }
    }

    const val DragThreshold = 300
}

@Composable
@Preview(device = Devices.DEFAULT)
@Preview(device = Devices.DEFAULT, uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL)
@Preview(device = Devices.TABLET)
@Preview(device = "spec:width=1280dp,height=800dp,orientation=portrait")
private fun ForYouTvShowItemPreview() {
    CineScoutTheme {
        ForYouTvShowItem(model = ForYouTvShowUiModelSample.Grimm, actions = ForYouTvShowItem.Actions.Empty)
    }
}
