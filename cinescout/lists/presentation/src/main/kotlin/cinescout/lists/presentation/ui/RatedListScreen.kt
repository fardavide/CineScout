package cinescout.lists.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import cinescout.design.theme.Dimens
import cinescout.design.ui.ErrorText
import cinescout.design.util.collectAsStateLifecycleAware
import cinescout.lists.presentation.model.ItemsListState
import cinescout.lists.presentation.model.ListType
import cinescout.lists.presentation.model.RatedListAction
import cinescout.lists.presentation.previewdata.ItemsListScreenPreviewDataProvider
import cinescout.lists.presentation.viewmodel.RatedListViewModel
import org.koin.androidx.compose.koinViewModel
import studio.forface.cinescout.design.R.string

@Composable
fun RatedListScreen(actions: ItemsListScreen.Actions, modifier: Modifier = Modifier) {
    val viewModel: RatedListViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateLifecycleAware()
    RatedListScreen(
        state = state,
        actions = actions,
        selectType = { viewModel.submit(RatedListAction.SelectListType(it)) },
        modifier = modifier
    )
}

@Composable
fun RatedListScreen(
    state: ItemsListState,
    actions: ItemsListScreen.Actions,
    selectType: (ListType) -> Unit,
    modifier: Modifier = Modifier
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth().padding(Dimens.Margin.Medium),
            horizontalArrangement = Arrangement.Center
        ) {
            ListTypeSelector(type = state.type, onTypeSelected = selectType)
        }
        ItemsListScreen(
            state = state,
            actions = actions,
            emptyListContent = { ErrorText(text = TextRes(string.lists_rated_empty)) },
            modifier = modifier.testTag(TestTag.Rated)
        )
    }
}

@Composable
@Preview(showBackground = true)
@Preview(showSystemUi = true, device = Devices.TABLET)
private fun RatedListScreenPreview(
    @PreviewParameter(ItemsListScreenPreviewDataProvider::class) state: ItemsListState
) {
    CineScoutTheme {
        RatedListScreen(state = state, actions = ItemsListScreen.Actions.Empty, selectType = {})
    }
}
