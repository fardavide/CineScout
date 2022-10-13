package cinescout.lists.presentation.previewdata

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import arrow.core.nonEmptyListOf
import cinescout.design.TextRes
import cinescout.lists.presentation.model.ItemsListState
import cinescout.lists.presentation.model.ListType
import studio.forface.cinescout.design.R.string

object ItemsListScreenPreviewData {

    val EmptyWatchlist = ItemsListState(
        items = ItemsListState.ItemsState.Data.Empty,
        type = ListType.All
    )
    val Error = ItemsListState(
        items = ItemsListState.ItemsState.Error(TextRes(string.network_error_no_network)),
        type = ListType.All
    )
    val Loading = ItemsListState(
        items = ItemsListState.ItemsState.Loading,
        type = ListType.All
    )
    val NotEmptyWatchList = ItemsListState(
        items = ItemsListState.ItemsState.Data.NotEmpty(
            nonEmptyListOf(
                ListItemUiModelPreviewData.Inception,
                ListItemUiModelPreviewData.TheWolfOfWallStreet
            )
        ),
        type = ListType.All
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
