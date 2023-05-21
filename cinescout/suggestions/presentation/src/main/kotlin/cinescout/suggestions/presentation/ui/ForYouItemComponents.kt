package cinescout.suggestions.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import cinescout.design.theme.CineScoutTheme
import cinescout.design.theme.Dimens
import cinescout.design.theme.imageBackground
import cinescout.design.ui.CenteredProgress
import cinescout.design.ui.CsAssistChip
import cinescout.design.ui.FailureImage
import cinescout.design.ui.ImageStack
import cinescout.media.domain.model.MediaRequest
import cinescout.resources.R.drawable
import cinescout.resources.R.string
import cinescout.suggestions.presentation.model.ForYouScreenplayUiModel
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.coil.CoilImage
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Composable
internal fun ForYouItemBackdrop(request: MediaRequest.Backdrop, modifier: Modifier = Modifier) {
    CoilImage(
        modifier = modifier.imageBackground(),
        imageModel = { request },
        imageOptions = ImageOptions(contentScale = ContentScale.Crop),
        failure = { FailureImage() },
        loading = { CenteredProgress() },
        previewPlaceholder = drawable.img_backdrop
    )
}

@Composable
internal fun ForYouItemBookmarkButton(modifier: Modifier = Modifier, onClick: () -> Unit) {
    FloatingActionButton(modifier = modifier, onClick = onClick) {
        Icon(
            painter = painterResource(id = drawable.ic_bookmark),
            contentDescription = stringResource(id = string.add_to_watchlist_button_description)
        )
    }
}

@Composable
internal fun ForYouItemActors(
    actors: ImmutableList<ForYouScreenplayUiModel.Actor>,
    count: Int,
    modifier: Modifier = Modifier
) {
    ImageStack(
        modifier = modifier,
        imageModels = actors.take(count).mapNotNull { it.profileImageUrl }.toImmutableList()
    )
}

@Composable
internal fun ForYouItemGenres(genres: ImmutableList<String>, modifier: Modifier = Modifier) {
    FlowRow(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(Dimens.Margin.XSmall)) {
        for (genre in genres) {
            CsAssistChip { Text(text = genre) }
        }
    }
}

@Preview
@Composable
private fun ForYouItemGenresPreview() {
    CineScoutTheme {
        ForYouItemGenres(genres = listOf("Action", "Adventure", "Comedy").toImmutableList())
    }
}
