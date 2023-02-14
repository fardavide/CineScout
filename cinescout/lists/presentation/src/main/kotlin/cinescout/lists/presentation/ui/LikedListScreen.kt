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
import cinescout.lists.presentation.model.ItemsListState
import cinescout.lists.presentation.model.LikedListAction
import cinescout.lists.presentation.model.ListType
import cinescout.lists.presentation.previewdata.ItemsListScreenPreviewDataProvider
import cinescout.lists.presentation.viewmodel.LikedListViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun LikedListScreen(actions: ItemsListScreen.Actions, modifier: Modifier = Modifier) {
    val viewModel: LikedListViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateLifecycleAware()
    LikedListScreen(
        state = state,
        actions = actions,
        selectType = { viewModel.submit(LikedListAction.SelectListType(it)) },
        modifier = modifier
    )
}

@Composable
fun LikedListScreen(
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
                ListType.All -> string.lists_liked_all_empty
                ListType.Movies -> string.lists_liked_movies_empty
                ListType.TvShows -> string.lists_liked_tv_shows_empty
            }
            ErrorText(text = TextRes(messageRes))
        },
        modifier = modifier.testTag(TestTag.Liked)
    )
}

@Composable
@Preview(showBackground = true)
@Preview(showSystemUi = true, device = Devices.TABLET)
private fun LikedListScreenPreview(
    @PreviewParameter(ItemsListScreenPreviewDataProvider::class) state: ItemsListState
) {
    CineScoutTheme {
        LikedListScreen(state = state, actions = ItemsListScreen.Actions.Empty, selectType = {})
    }
}
