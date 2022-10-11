package cinescout.lists.presentation.previewdata

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import arrow.core.nonEmptyListOf
import cinescout.design.TextRes
import cinescout.lists.presentation.model.ItemsListState
import studio.forface.cinescout.design.R.string

object ItemsListScreenPreviewData {

    val EmptyWatchlist = ItemsListState(
        items = ItemsListState.ItemsState.Data.Empty,
        type = ItemsListState.Type.All
    )
    val Error = ItemsListState(
        items = ItemsListState.ItemsState.Error(TextRes(string.network_error_no_network)),
        type = ItemsListState.Type.All
    )
    val Loading = ItemsListState(
        items = ItemsListState.ItemsState.Loading,
        type = ItemsListState.Type.All
    )
    val NotEmptyWatchList = ItemsListState(
        items = ItemsListState.ItemsState.Data.NotEmpty(
            nonEmptyListOf(
                ListItemUiModelPreviewData.Inception,
                ListItemUiModelPreviewData.TheWolfOfWallStreet
            )
        ),
        type = ItemsListState.Type.All
    )
}

class ItemsListScreenPreviewDataProvider : PreviewParameterProvider<ItemsListState> {

    override val values = sequenceOf(
        ItemsListScreenPreviewData.Loading,
        ItemsListScreenPreviewData.Error,
        ItemsListScreenPreviewData.EmptyWatchlist,
        ItemsListScreenPreviewData.NotEmptyWatchList
    )
}
