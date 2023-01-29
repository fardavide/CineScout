package cinescout.suggestions.presentation.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import cinescout.design.theme.Dimens
import cinescout.design.theme.imageBackground
import cinescout.design.util.NoContentDescription
import cinescout.screenplay.domain.model.TmdbScreenplayId
import cinescout.suggestions.presentation.model.ForYouScreenplayUiModel
import cinescout.suggestions.presentation.sample.ForYouMovieUiModelSample
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.coil.CoilImage
import studio.forface.cinescout.design.R

@Composable
internal fun ForYouItem(
    model: ForYouScreenplayUiModel,
    actions: ForYouItem.Actions,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier.padding(Dimens.Margin.Small)) {
        ForYouItemLayout(
            backdrop = { ForYouItemBackdrop(model.backdropUrl) },
            poster = { ForYouItemPoster(model.posterUrl) },
            infoBox = { ForYouItemInfoBox(model.title, model.releaseYear, model.rating) },
            genres = { ForYouItemGenres(model.genres) },
            actors = { ForYouItemActors(model.actors) },
            buttons = { ForYouItemButtons(itemId = model.tmdbScreenplayId, actions = actions) }
        )
    }
}

@Composable
internal fun ForYouItemBackdrop(url: String?) {
    CoilImage(
        modifier = Modifier.imageBackground(),
        imageModel = { url },
        imageOptions = ImageOptions(contentScale = ContentScale.Crop),
        failure = {
            Image(
                painter = painterResource(id = R.drawable.ic_warning_30),
                contentDescription = NoContentDescription
            )
        },
        previewPlaceholder = R.drawable.img_backdrop
    )
}

@Composable
internal fun ForYouItemPoster(url: String?) {
    CoilImage(
        modifier = Modifier
            .clip(MaterialTheme.shapes.medium)
            .imageBackground(),
        imageModel = { url },
        imageOptions = ImageOptions(contentScale = ContentScale.Inside),
        failure = {
            Image(
                painter = painterResource(id = R.drawable.ic_warning_30),
                contentDescription = NoContentDescription
            )
        },
        previewPlaceholder = R.drawable.img_poster
    )
}

@Composable
internal fun ForYouItemInfoBox(title: String, releaseYear: String, rating: String) {
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
internal fun ForYouItemGenres(genres: List<String>) {
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
internal fun ForYouItemActors(actors: List<ForYouScreenplayUiModel.Actor>) {
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
                        painter = painterResource(id = R.drawable.ic_warning_30),
                        contentDescription = NoContentDescription
                    )
                },
                previewPlaceholder = R.drawable.ic_user_color
            )
        }
    }
}

@Composable
internal fun ForYouItemButtons(itemId: TmdbScreenplayId, actions: ForYouItem.Actions) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.Margin.Small),
        horizontalArrangement = Arrangement.End
    ) {
        ElevatedButton(onClick = { actions.addToWatchlist(itemId) }) {
            Text(text = stringResource(id = R.string.suggestions_for_you_add_watchlist))
        }
        Spacer(modifier = Modifier.width(Dimens.Margin.Small))
        OutlinedButton(onClick = { actions.toDetails(itemId) }) {
            Text(text = stringResource(id = R.string.suggestions_for_you_open_details))
        }
    }
}

internal object ForYouItem {

    data class Actions(
        val addToWatchlist: (TmdbScreenplayId) -> Unit,
        val toDetails: (TmdbScreenplayId) -> Unit
    ) {

        companion object {

            val Empty = Actions(
                addToWatchlist = {},
                toDetails = {}
            )
        }
    }
}

@Composable
@Preview
private fun ForYouItemPreview() {
    ForYouItem(model = ForYouMovieUiModelSample.Inception, actions = ForYouItem.Actions.Empty)
}
