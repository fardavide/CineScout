package cinescout.lists.presentation.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import arrow.core.NonEmptyList
import cinescout.design.TestTag
import cinescout.design.theme.CineScoutTheme
import cinescout.design.theme.Dimens
import cinescout.design.theme.imageBackground
import cinescout.design.ui.CenteredProgress
import cinescout.design.ui.ErrorScreen
import cinescout.design.util.NoContentDescription
import cinescout.design.util.collectAsStateLifecycleAware
import cinescout.lists.presentation.model.WatchlistItemUiModel
import cinescout.lists.presentation.model.WatchlistState
import cinescout.lists.presentation.previewdata.WatchlistScreenPreviewDataProvider
import cinescout.lists.presentation.viewmodel.WatchlistViewModel
import coil.compose.AsyncImage
import org.koin.androidx.compose.koinViewModel
import studio.forface.cinescout.design.R.drawable
import studio.forface.cinescout.design.R.string

@Composable
fun WatchlistScreen(modifier: Modifier = Modifier) {
    val viewModel: WatchlistViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateLifecycleAware()
    WatchlistScreen(state = state, modifier = modifier)
}

@Composable
fun WatchlistScreen(state: WatchlistState, modifier: Modifier = Modifier) {
    Box(modifier = modifier
        .fillMaxSize()
        .testTag(TestTag.Watchlist)) {
        when (state) {
            is WatchlistState.Error -> ErrorScreen(text = state.message)
            WatchlistState.Loading -> CenteredProgress()
            is WatchlistState.Data -> WatchlistContent(data = state)
        }
    }
}

@Composable
private fun WatchlistContent(data: WatchlistState.Data) {
    when (data) {
        WatchlistState.Data.Empty -> EmptyWatchlistContent()
        is WatchlistState.Data.NotEmpty -> NotEmptyWatchlistContent(items = data.items)
    }
}

@Composable
private fun EmptyWatchlistContent() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            text = stringResource(id = string.lists_watchlist_empty),
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun NotEmptyWatchlistContent(items: NonEmptyList<WatchlistItemUiModel>) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = Dimens.Component.XXLarge),
        contentPadding = PaddingValues(horizontal = Dimens.Margin.XSmall)
    ) {
        items(items = items, key = { it.tmdbId.value }) { item ->
            WatchlistItem(model = item)
        }
    }
}

@Composable
private fun WatchlistItem(model: WatchlistItemUiModel) {
    Box(modifier = Modifier.padding(Dimens.Margin.XSmall)) {
        ElevatedCard {
            Column {
                val imageWidth = Dimens.Component.XXLarge
                val imageHeight = imageWidth * 1.35f
                AsyncImage(
                    modifier = Modifier
                        .width(imageWidth)
                        .height(imageHeight)
                        .imageBackground(),
                    model = model.posterUrl,
                    contentScale = ContentScale.FillWidth,
                    contentDescription = NoContentDescription,
                    error = painterResource(id = drawable.ic_warning_30)
                )
                Text(
                    modifier = Modifier.padding(vertical = Dimens.Margin.XXSmall, horizontal = Dimens.Margin.Small),
                    text = model.title,
                    style = MaterialTheme.typography.labelSmall,
                    maxLines = 2
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
@Preview(showSystemUi = true, device = Devices.TABLET)fun WatchlistScreenPreview(
    @PreviewParameter(WatchlistScreenPreviewDataProvider::class) state: WatchlistState
) {
    CineScoutTheme {
        WatchlistScreen(state = state)
    }
}
