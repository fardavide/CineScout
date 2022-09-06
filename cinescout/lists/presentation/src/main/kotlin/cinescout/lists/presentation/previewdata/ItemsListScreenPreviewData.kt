package cinescout.lists.presentation.previewdata

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import arrow.core.nonEmptyListOf
import cinescout.design.TextRes
import cinescout.lists.presentation.model.ItemsListState
import studio.forface.cinescout.design.R.string

object ItemsListScreenPreviewData {

    val EmptyWatchlist = ItemsListState.Data.Empty
    val Error = ItemsListState.Error(TextRes(string.network_error_no_network))
    val Loading = ItemsListState.Loading
    val NotEmptyWatchList = ItemsListState.Data.NotEmpty(
        nonEmptyListOf(
            ListItemUiModelPreviewData.Inception,
            ListItemUiModelPreviewData.TheWolfOfWallStreet
        )
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
