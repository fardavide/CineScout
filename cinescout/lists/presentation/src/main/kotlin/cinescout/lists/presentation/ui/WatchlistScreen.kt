package cinescout.lists.presentation.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import cinescout.design.TestTag
import cinescout.design.TextRes
import cinescout.design.theme.CineScoutTheme
import cinescout.design.ui.ErrorText
import cinescout.design.util.collectAsStateLifecycleAware
import cinescout.lists.presentation.model.ItemsListState
import cinescout.lists.presentation.previewdata.ItemsListScreenPreviewDataProvider
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
fun WatchlistScreen(state: ItemsListState, modifier: Modifier = Modifier) {
    ItemsListScreen(
        state = state,
        emptyListContent = { ErrorText(text = TextRes(string.lists_watchlist_empty)) },
        modifier = modifier.testTag(TestTag.Watchlist)
    )
}

@Composable
@Preview(showBackground = true)
@Preview(showSystemUi = true, device = Devices.TABLET)
fun WatchlistScreenPreview(
    @PreviewParameter(ItemsListScreenPreviewDataProvider::class) state: ItemsListState
) {
    CineScoutTheme {
        WatchlistScreen(state = state)
    }
}
