package cinescout.lists.presentation.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import arrow.core.NonEmptyList
import cinescout.design.TestTag
import cinescout.design.theme.CineScoutTheme
import cinescout.design.ui.CenteredProgress
import cinescout.design.ui.ErrorScreen
import cinescout.design.util.collectAsStateLifecycleAware
import cinescout.lists.presentation.model.WatchlistItemUiModel
import cinescout.lists.presentation.model.WatchlistState
import cinescout.lists.presentation.previewdata.WatchlistScreenPreviewDataProvider
import cinescout.lists.presentation.viewmodel.WatchlistViewModel
import org.koin.androidx.compose.koinViewModel
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
    LazyColumn {
        items(items = items, key = { it.tmdbId.value }) { item ->
            Text(text = item.title)
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
