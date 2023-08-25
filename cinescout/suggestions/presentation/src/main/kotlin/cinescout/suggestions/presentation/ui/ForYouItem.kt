package cinescout.suggestions.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.min
import cinescout.design.TestTag
import cinescout.design.theme.CineScoutTheme
import cinescout.design.theme.Dimens
import cinescout.design.ui.CsCard
import cinescout.media.domain.model.asBackdropRequest
import cinescout.screenplay.domain.model.id.ScreenplayIds
import cinescout.suggestions.presentation.model.ForYouScreenplayUiModel
import cinescout.suggestions.presentation.sample.ForYouScreenplayUiModelSample
import cinescout.utils.compose.Adaptive
import cinescout.utils.compose.WindowHeightSizeClass
import cinescout.utils.compose.WindowSizeClass
import cinescout.utils.compose.WindowWidthSizeClass

@Composable
internal fun ForYouItem(
    model: ForYouScreenplayUiModel,
    actions: ForYouItem.Actions,
    modifier: Modifier = Modifier
) {
    CsCard(
        modifier = modifier.testTag(TestTag.ForYouItem),
        onclick = { actions.toDetails(model.screenplayIds) }
    ) {
        Adaptive { windowSizeClass ->
            when (val mode = ForYouItem.Mode.forClass(windowSizeClass)) {
                is ForYouItem.Mode.Horizontal -> ForYouItem.Horizontal(
                    modifier = Modifier,
                    model = model,
                    actions = actions,
                    spacing = mode.spacing
                )

                ForYouItem.Mode.Vertical -> ForYouItem.Vertical(
                    modifier = Modifier,
                    model = model,
                    actions = actions
                )
            }
        }
    }
}

object ForYouItem {

    @Composable
    internal fun Vertical(
        model: ForYouScreenplayUiModel,
        actions: Actions,
        modifier: Modifier = Modifier
    ) {
        Column(modifier = modifier) {
            BoxWithConstraints(contentAlignment = Alignment.BottomEnd) {
                ForYouItemBackdrop(
                    modifier = Modifier
                        .width(maxWidth)
                        .height(maxWidth * 0.55f),
                    request = model.screenplayIds.tmdb.asBackdropRequest()
                )
                ForYouItemBookmarkButton(
                    modifier = Modifier.padding(Dimens.Margin.large),
                    onClick = { actions.addToWatchlist(model.screenplayIds) }
                )
            }
            Column(
                modifier = Modifier.padding(horizontal = Dimens.Margin.medium, vertical = Dimens.Margin.large),
                verticalArrangement = Arrangement.spacedBy(Dimens.Margin.small)
            ) {
                ForYouItemActors(
                    actors = model.actors,
                    count = 5
                )
                ForYouItemGenres(genres = model.genres)
                Text(
                    text = model.title,
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = model.releaseDate,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }

    @Composable
    internal fun Horizontal(
        model: ForYouScreenplayUiModel,
        actions: Actions,
        spacing: Dp,
        modifier: Modifier = Modifier
    ) {
        Row(modifier = modifier) {
            BoxWithConstraints(modifier = Modifier.wrapContentHeight(), contentAlignment = Alignment.BottomEnd) {
                val backdropMaxWidth = maxWidth * 0.6f
                val backdropHeight = min(maxHeight, backdropMaxWidth)
                val backdropWidth = min(backdropHeight * 1.40f, backdropMaxWidth)
                ForYouItemBackdrop(
                    modifier = Modifier
                        .width(backdropWidth)
                        .height(backdropHeight),
                    request = model.screenplayIds.tmdb.asBackdropRequest()
                )
                ForYouItemBookmarkButton(
                    modifier = Modifier.padding(Dimens.Margin.large),
                    onClick = { actions.addToWatchlist(model.screenplayIds) }
                )
            }
            Column(
                modifier = Modifier.padding(horizontal = Dimens.Margin.medium, vertical = Dimens.Margin.large),
                verticalArrangement = Arrangement.spacedBy(spacing)
            ) {
                ForYouItemActors(
                    actors = model.actors,
                    count = 5
                )
                ForYouItemGenres(genres = model.genres)
                Text(
                    text = model.title,
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = model.releaseDate,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }

    sealed interface Mode {

        data class Horizontal(val spacing: Dp) : Mode
        object Vertical : Mode

        companion object {

            fun forClass(windowSizeClass: WindowSizeClass): Mode {
                val spacing = when (windowSizeClass.height) {
                    WindowHeightSizeClass.Compact -> Dimens.Margin.xxSmall
                    WindowHeightSizeClass.Medium -> Dimens.Margin.small
                    WindowHeightSizeClass.Expanded -> Dimens.Margin.medium
                }
                return when (windowSizeClass.width) {
                    WindowWidthSizeClass.Compact -> when (windowSizeClass.height) {
                        WindowHeightSizeClass.Compact -> Horizontal(spacing = spacing)
                        WindowHeightSizeClass.Medium,
                        WindowHeightSizeClass.Expanded -> Vertical
                    }
                    WindowWidthSizeClass.Medium -> Horizontal(spacing = spacing)
                    WindowWidthSizeClass.Expanded -> when (windowSizeClass.height) {
                        WindowHeightSizeClass.Compact,
                        WindowHeightSizeClass.Medium -> Horizontal(spacing = spacing)
                        WindowHeightSizeClass.Expanded -> Vertical
                    }
                }
            }
        }
    }

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
private fun VerticalForYouItemPreview() {
    val model = ForYouScreenplayUiModelSample.Inception
    CineScoutTheme {
        Card {
            ForYouItem.Vertical(model = model, actions = ForYouItem.Actions.Empty)
        }
    }
}

@Composable
@Preview(device = Devices.TABLET)
private fun HorizontalForYouItemPreview() {
    val model = ForYouScreenplayUiModelSample.Inception
    CineScoutTheme {
        Card {
            ForYouItem.Horizontal(model = model, actions = ForYouItem.Actions.Empty, spacing = Dimens.Margin.medium)
        }
    }
}
