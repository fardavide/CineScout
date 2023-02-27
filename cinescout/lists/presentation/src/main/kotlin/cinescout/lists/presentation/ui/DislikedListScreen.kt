package cinescout.lists.presentation.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import cinescout.design.R.string
import cinescout.design.TestTag
import cinescout.design.TextRes
import cinescout.design.theme.CineScoutTheme
import cinescout.design.ui.ErrorText
import cinescout.design.util.collectAsStateLifecycleAware
import cinescout.lists.presentation.model.DislikedListAction
import cinescout.lists.presentation.model.ListType
import cinescout.lists.presentation.previewdata.ItemsListScreenPreviewDataProvider
import cinescout.lists.presentation.state.ItemsListState
import cinescout.lists.presentation.viewmodel.DislikedListViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun DislikedListScreen(actions: ItemsListScreen.Actions, modifier: Modifier = Modifier) {
    val viewModel: DislikedListViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateLifecycleAware()
    DislikedListScreen(
        state = state,
        actions = actions,
        selectType = { viewModel.submit(DislikedListAction.SelectListType(it)) },
        modifier = modifier
    )
}

@Composable
fun DislikedListScreen(
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
                ListType.All -> string.lists_disliked_all_empty
                ListType.Movies -> string.lists_disliked_movies_empty
                ListType.TvShows -> string.lists_disliked_tv_shows_empty
            }
            ErrorText(text = TextRes(messageRes))
        },
        modifier = modifier.testTag(TestTag.Disliked)
    )
}

@Composable
@Preview(showBackground = true)
@Preview(showSystemUi = true, device = Devices.TABLET)
private fun DislikedListScreenPreview(
    @PreviewParameter(ItemsListScreenPreviewDataProvider::class) state: ItemsListState
) {
    CineScoutTheme {
        DislikedListScreen(state = state, actions = ItemsListScreen.Actions.Empty, selectType = {})
    }
}
