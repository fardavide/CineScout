package cinescout.suggestions.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import cinescout.design.theme.CineScoutTheme
import cinescout.design.theme.Dimens
import cinescout.design.theme.imageBackground
import cinescout.design.ui.CenteredProgress
import cinescout.design.ui.FailureImage
import cinescout.design.util.NoContentDescription
import cinescout.media.domain.model.MediaRequest
import cinescout.media.domain.model.asBackdropRequest
import cinescout.media.domain.model.asPosterRequest
import cinescout.resources.R.drawable
import cinescout.resources.R.string
import cinescout.screenplay.domain.model.ScreenplayIds
import cinescout.suggestions.presentation.model.ForYouScreenplayUiModel
import cinescout.suggestions.presentation.preview.ForYouScreenplayUiModelPreviewProvider
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.coil.CoilImage
import kotlinx.collections.immutable.ImmutableList

@Composable
internal fun ForYouItem(
    model: ForYouScreenplayUiModel,
    actions: ForYouItem.Actions,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .padding(Dimens.Margin.Small)
            .clickable { actions.toDetails(model.screenplayIds) }
    ) {
        ForYouItemLayout(
            backdrop = { ForYouItemBackdrop(model.screenplayIds.tmdb.asBackdropRequest()) },
            poster = { ForYouItemPoster(model.screenplayIds.tmdb.asPosterRequest()) },
            infoBox = { ForYouItemInfoBox(model.title, model.releaseDate, model.rating) },
            genres = { ForYouItemGenres(model.genres) },
            actors = { ForYouItemActors(model.actors) },
            openDetailsButton = {
                ForYouOpenDetailsButton(onClick = { actions.toDetails(model.screenplayIds) })
            },
            bookmarkButton = {
                ForYouBookmarkButton(onClick = { actions.addToWatchlist(model.screenplayIds) })
            }
        )
    }
}

@Composable
internal fun ForYouItemBackdrop(request: MediaRequest.Backdrop) {
    CoilImage(
        modifier = Modifier.imageBackground().fillMaxWidth(),
        imageModel = { request },
        imageOptions = ImageOptions(contentScale = ContentScale.Crop),
        failure = { FailureImage() },
        loading = { CenteredProgress() },
        previewPlaceholder = drawable.img_backdrop
    )
}

@Composable
internal fun ForYouItemPoster(request: MediaRequest.Poster) {
    CoilImage(
        modifier = Modifier
            .clip(MaterialTheme.shapes.medium)
            .imageBackground(),
        imageModel = { request },
        imageOptions = ImageOptions(contentScale = ContentScale.Inside),
        failure = { FailureImage() },
        loading = { CenteredProgress() },
        previewPlaceholder = drawable.img_poster
    )
}

@Composable
internal fun ForYouItemInfoBox(
    title: String,
    releaseYear: String,
    rating: String
) {
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
internal fun ForYouItemGenres(genres: ImmutableList<String>) {
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
internal fun ForYouItemActors(actors: ImmutableList<ForYouScreenplayUiModel.Actor>) {
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
                failure = { FailureImage() },
                previewPlaceholder = drawable.ic_user_color
            )
        }
    }
}

@Composable
internal fun ForYouOpenDetailsButton(onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.Margin.Small),
        horizontalArrangement = Arrangement.End
    ) {
        TextButton(onClick = onClick) {
            Text(text = stringResource(id = string.suggestions_for_you_open_details).uppercase())
        }
    }
}

@Composable
internal fun ForYouBookmarkButton(onClick: () -> Unit) {
    IconButton(
        modifier = Modifier.background(MaterialTheme.colorScheme.surface, shape = MaterialTheme.shapes.small),
        onClick = onClick
    ) {
        Icon(
            painter = painterResource(id = drawable.ic_bookmark),
            contentDescription = NoContentDescription
        )
    }
}

internal object ForYouItem {

    data class Actions(
        val addToWatchlist: (ScreenplayIds) -> Unit,
        val toDetails: (ScreenplayIds) -> Unit
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
private fun ForYouItemPreview(
    @PreviewParameter(ForYouScreenplayUiModelPreviewProvider::class) uiModel: ForYouScreenplayUiModel
) {
    CineScoutTheme {
        ForYouItem(model = uiModel, actions = ForYouItem.Actions.Empty)
    }
}
