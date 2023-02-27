package cinescout.lists.presentation.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.PreviewParameter
import cinescout.design.AdaptivePreviews
import cinescout.design.R.string
import cinescout.design.TestTag
import cinescout.design.TextRes
import cinescout.design.theme.CineScoutTheme
import cinescout.design.ui.ErrorText
import cinescout.design.util.collectAsStateLifecycleAware
import cinescout.lists.presentation.model.ListType
import cinescout.lists.presentation.model.WatchlistAction
import cinescout.lists.presentation.previewdata.ItemsListScreenPreviewDataProvider
import cinescout.lists.presentation.state.ItemsListState
import cinescout.lists.presentation.viewmodel.WatchlistViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun WatchlistScreen(actions: ItemsListScreen.Actions, modifier: Modifier = Modifier) {
    val viewModel: WatchlistViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateLifecycleAware()
    WatchlistScreen(
        state = state,
        actions = actions,
        selectType = { viewModel.submit(WatchlistAction.SelectListType(it)) },
        modifier = modifier
    )
}

@Composable
fun WatchlistScreen(
    state: ItemsListState,
    actions: ItemsListScreen.Actions,
    selectType: (ListType) -> Unit,
    modifier: Modifier = Modifier
) {
    ItemsListScreen(
        state = state,
        actions = actions,
        selectType = selectType,
        emptyListContent = {
            val messageRes = when (state.type) {
                ListType.All -> string.lists_watchlist_all_empty
                ListType.Movies -> string.lists_watchlist_movies_empty
                ListType.TvShows -> string.lists_watchlist_tv_shows_empty
            }
            ErrorText(text = TextRes(messageRes))
        },
        modifier = modifier.testTag(TestTag.Watchlist)
    )
}

@Composable
@AdaptivePreviews.WithSystemUi
fun WatchlistScreenPreview(
    @PreviewParameter(ItemsListScreenPreviewDataProvider::class) state: ItemsListState
) {
    CineScoutTheme {
        WatchlistScreen(
            state = state,
            actions = ItemsListScreen.Actions.Empty,
            selectType = {}
        )
    }
}
